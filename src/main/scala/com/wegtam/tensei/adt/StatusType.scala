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

/**
  * The type of a status message.
  */
sealed trait StatusType {

  override def toString: String = this match {
    case StatusType.MinorError       ⇒ "MinorError"
    case StatusType.MajorError       ⇒ "MajorError"
    case StatusType.NoAgentAvailable ⇒ "NoAgentAvailable"
    case StatusType.FatalError       ⇒ "FatalError"
  }

}

object StatusType {

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  implicit def StatusTypeCodecJson: CodecJson[StatusType] =
    CodecJson(
      (s: StatusType) ⇒ jString(s.toString),
      c ⇒
        for {
          s ← c.as[String]
        } yield
          s match {
            case "MinorError"       ⇒ MinorError
            case "MajorError"       ⇒ MajorError
            case "NoAgentAvailable" ⇒ NoAgentAvailable
            case "FatalError"       ⇒ FatalError
            case e: String          ⇒ throw new IllegalArgumentException(s"Unknown StatusType: '$e'!")
        }
    )

  case object MinorError extends StatusType

  case object MajorError extends StatusType

  case object NoAgentAvailable extends StatusType

  case object FatalError extends StatusType

}
