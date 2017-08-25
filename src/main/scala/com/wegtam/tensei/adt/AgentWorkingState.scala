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

import com.wegtam.tensei.agent.{ ParserState, ProcessorState, TenseiAgentState }

/**
  * The "working" state of an agent.
  *
  * @param id                The ID of the agent.
  * @param state             The current state of the agent.
  * @param parser            The current state of the parser.
  * @param processor         The current state of the processor.
  * @param runtimeStats      Some statistics regarding the agent's jvm runtime. The map should use the agent's hostname as the key value.
  * @param uniqueIdentifier  An option to the id of the currently running transformation configuration.
  */
final case class AgentWorkingState(
    id: String,
    state: TenseiAgentState,
    parser: ParserState,
    processor: ProcessorState,
    runtimeStats: Map[String, RuntimeStats],
    uniqueIdentifier: Option[String] = None
) {}

object AgentWorkingState {
  implicit def AgentWorkingStateCodecJson: CodecJson[AgentWorkingState] =
    CodecJson(
      (s: AgentWorkingState) ⇒
        ("uniqueIdentifier" := s.uniqueIdentifier) ->: ("runtime" := s.runtimeStats) ->: ("processor" := s.processor) ->: ("parser" := s.parser) ->: ("state" := s.state) ->: ("id" := s.id) ->: jEmptyObject,
      c ⇒
        for {
          id               ← (c --\ "id").as[String]
          agentState       ← (c --\ "state").as[TenseiAgentState]
          parserState      ← (c --\ "parser").as[ParserState]
          processorState   ← (c --\ "processor").as[ProcessorState]
          runtime          ← (c --\ "runtime").as[Map[String, RuntimeStats]]
          uniqueIdentifier ← (c --\ "uniqueIdentifier").as[Option[String]]
        } yield
          AgentWorkingState(id = id,
                            state = agentState,
                            parser = parserState,
                            processor = processorState,
                            runtimeStats = runtime,
                            uniqueIdentifier = uniqueIdentifier)
    )
}
