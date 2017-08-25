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
  * Informations about an agent.
  *
  * @param id            The unique name of the agent.
  * @param path          The actor path to the agent.
  * @param auth          The authorization state of the agent.
  * @param lastUpdated   The timestamp marking the last information update.
  * @param workingState  The working state of the agent with information of the parser status.
  */
final case class AgentInformation(
    id: String,
    path: String,
    auth: AgentAuthorizationState,
    lastUpdated: Long,
    workingState: Option[AgentWorkingState] = None
)

object AgentInformation {

  implicit def AgentInformationCodecJson: CodecJson[AgentInformation] =
    CodecJson(
      (i: AgentInformation) ⇒
        ("workingState" := i.workingState) ->: ("updated" := jNumber(i.lastUpdated)) ->: ("auth" := i.auth) ->: ("path" := i.path) ->: ("id" := i.id) ->: jEmptyObject,
      c ⇒
        for {
          id           ← (c --\ "id").as[String]
          path         ← (c --\ "path").as[String]
          auth         ← (c --\ "auth").as[AgentAuthorizationState]
          updated      ← (c --\ "updated").as[Long]
          workingState ← (c --\ "workingState").as[Option[AgentWorkingState]]
        } yield
          AgentInformation(id = id,
                           path = path,
                           auth = auth,
                           lastUpdated = updated,
                           workingState = workingState)
    )

}
