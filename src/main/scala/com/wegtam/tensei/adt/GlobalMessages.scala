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

import akka.actor.ActorRef

import scalaz._

/**
  * Global messages that can be send accross the cluster.
  */
sealed trait GlobalMessages

/**
  * Container object for the global messages to keep the namespace somewhat clean.
  */
object GlobalMessages {

  final case class AbortTransformation(ref: ActorRef) extends GlobalMessages

  final case class AbortTransformationResponse(ref: ActorRef, message: Option[String])
      extends GlobalMessages

  /**
    * This message can be send to notify someone that an error has occured.
    *
    * @param error The actual error.
    */
  final case class ErrorOccured(error: StatusMessage) extends GlobalMessages

  /**
    * Contains a bulk of error messages.
    *
    * @param errors A list of error messages.
    */
  final case class ErrorsOccured(errors: List[ErrorOccured]) extends GlobalMessages

  /**
    * This message can be send to notify someone that an exception has occured.
    *
    * @param ref       An option to the actor closest to the exception. This is usually the one that catched the exception.
    * @param exception The exception.
    */
  final case class ExceptionOccured(ref: Option[ActorRef], exception: Throwable)
      extends GlobalMessages

  /**
    * Try to extract the schema from the data source with the given connection information
    * into a dfasdl.
    *
    * @param source The connection information for the data source.
    * @param options The options for the schema extractor.
    */
  final case class ExtractSchema(
      source: ConnectionInformation,
      options: ExtractSchemaOptions
  ) extends GlobalMessages

  /**
    * The result of a schema extraction request.
    *
    * @param source The connection information that was used for the data source.
    * @param result The actual result which maybe either a `DFASDL` or a string holding an error message.
    */
  final case class ExtractSchemaResult(source: ConnectionInformation, result: String \/ DFASDL)
      extends GlobalMessages

  final case class ReportingTo(ref: ActorRef, id: Option[String] = None) extends GlobalMessages

  case object ReportToCaller extends GlobalMessages

  final case class ReportToRef(ref: ActorRef) extends GlobalMessages

  /**
    * Request the log files of the specified transformation run from an agent.
    *
    * @param agentId The id of the agent that should be queried.
    * @param uuid The agent run identifier which is usually uuid.
    * @param offset An optional offset given in number of bytes that should be skipped.
    * @param maxSize An optional number of bytes that specifies the maximum size of logs that should be sent.
    */
  final case class RequestAgentRunLogs(
      agentId: String,
      uuid: String,
      offset: Option[Long] = None,
      maxSize: Option[Long] = None
  ) extends GlobalMessages

  /**
    * Request the meta data of the specified transformation run from an agent.
    *
    * @param uuid The agent run identifier which is usually uuid.
    */
  final case class RequestAgentRunLogsMetaData(uuid: String) extends GlobalMessages

  /**
    * Report the meta data for requested run logs.
    *
    * @param agentId The id of the agent that holds the logs.
    * @param uuid The agent run identifier which is usually uuid.
    * @param size The size of the log file(s) in bytes.
    */
  final case class ReportAgentRunLogsMetaData(
      agentId: String,
      uuid: String,
      size: Long
  ) extends GlobalMessages

  /**
    * Report a line from the run logs.
    *
    * @param uuid The agent run identifier which is usually uuid.
    * @param logLine The actual log entry.
    * @param offet The offset within the log file that marks the start of the log entry.
    */
  final case class ReportAgentRunLogLine(uuid: String, logLine: String, offet: Long)
      extends GlobalMessages

  case object Restart extends GlobalMessages

  case object Shutdown extends GlobalMessages

  final case class TransformationAborted(uuid: Option[String] = None) extends GlobalMessages

  final case class TransformationCompleted(uuid: Option[String] = None) extends GlobalMessages

  final case class TransformationError(uuid: Option[String] = None, error: StatusMessage)
      extends GlobalMessages

  final case class TransformationStarted(uuid: Option[String] = None) extends GlobalMessages

  final case class UnwatchActor(ref: ActorRef) extends GlobalMessages

  final case class WatchActor(ref: ActorRef) extends GlobalMessages

}
