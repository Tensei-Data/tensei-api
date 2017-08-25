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

package com.wegtam.tensei.server

import com.wegtam.tensei.adt.{ AgentInformation, AgentStartTransformationMessage, StatusMessage }

import scalaz._

/**
  * A sealed trait containing the server messages that should be available accross the cluster.
  */
sealed trait ServerMessages

/**
  * Container object holding the server messages the keep the namespace somewhat clean.
  */
object ServerMessages {

  /**
    * Notify the target actor to return it's collected agents informations.
    */
  case object ReportAgentsInformations extends ServerMessages

  /**
    * Holds the collected agents informations.
    *
    * @param agents A map containing the informations for each agent mapped by the agent id.
    */
  final case class ReportAgentsInformationsResponse(agents: Map[String, AgentInformation])
      extends ServerMessages

  /**
    * Instruct the receiver to start a given transformation.
    *
    * @param message An option to an agent start transformation message that contains all necessary informations.
    */
  final case class StartTransformationConfiguration(
      message: Option[AgentStartTransformationMessage]
  ) extends ServerMessages

  /**
    * The response from the server for a start transformation configuration message.
    *
    * @param statusMessage A return message holding either a status message or an error message.
    */
  final case class StartTransformationConfigurationResponse(statusMessage: StatusMessage \/ String,
                                                            uniqueIdentifier: Option[String])
      extends ServerMessages

}
