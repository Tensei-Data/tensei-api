/*
 * Copyright (C) 2014 - 2017  Contributors as noted in the AUTHORS.md file
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wegtam.tensei.adt

import argonaut._, Argonaut._

import akka.actor.ActorRef

/**
  * A base for status messages.
  *
  * @param reporter    An option to the reporter of the message, usually the actor path to the actor that produced the error.
  * @param message     The actual status message which should be a bit verbose.
  * @param statusType  The type of the status
  * @param cause       An option to another status.
  */
case class StatusMessage(
    reporter: Option[String],
    message: String,
    statusType: StatusType = StatusType.MinorError,
    cause: Option[StatusMessage]
) {

  override def toString: String =
    s"""$statusType Status: $message ${cause.fold("")(c ⇒ s"""(CAUSED BY $c) """)}(REPORTED BY ${reporter
      .getOrElse("None")})"""

  override def equals(obj: scala.Any): Boolean =
    obj match {
      case other: StatusMessage ⇒
        reporter == other.reporter && message == other.message && statusType == other.statusType && cause == other.cause
      case _ ⇒ false
    }

  override def hashCode(): Int =
    11 + 5 * reporter.hashCode + 5 * message.hashCode + 5 * statusType.hashCode() + 5 * cause
      .hashCode()

}

object StatusMessage {

  /**
    * Encode the status message into a json string.
    *
    * @return A json representation of the status message.
    */
  implicit def ErrorMessageEncodeJson: EncodeJson[StatusMessage] =
    EncodeJson(
      (m: StatusMessage) ⇒
        ("cause" := m.cause) ->: ("statusType" := m.statusType) ->: ("message" := jString(
          m.message
        )) ->: ("reporter" := m.reporter) ->: jEmptyObject
    )

  /**
    * Decodes generic status messages from a given json source.
    *
    * @return The deocded status message.
    */
  implicit def ErrorMessageDecodeJson: DecodeJson[StatusMessage] =
    DecodeJson(
      cursor ⇒
        for {
          reporter ← (cursor --\ "reporter").as[Option[String]]
          message  ← (cursor --\ "message").as[String]
          severity ← (cursor --\ "statusType").as[StatusType]
          cause    ← (cursor --\ "cause").as[Option[StatusMessage]]
        } yield new StatusMessage(reporter, message, severity, cause)
    )

  /**
    * A helper function that returns the actor path (including the actor's address) as an option of string.
    *
    * @param ref An option to an actor ref.
    * @return The option containing the path as string or `None` if no actor ref was given.
    */
  def getReporterStringFromActorRef(ref: Option[ActorRef]): Option[String] =
    ref.map(r ⇒ r.path.toSerializationFormatWithAddress(r.path.address))

}
