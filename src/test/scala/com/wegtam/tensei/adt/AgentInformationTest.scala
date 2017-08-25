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

import com.wegtam.tensei.DefaultSpec
import com.wegtam.tensei.agent.{ ParserState, ProcessorState, TenseiAgentState }

class AgentInformationTest extends DefaultSpec {
  describe("AgentInformation") {
    describe("CodecJson") {
      describe("decode") {
        it("must decode proper json") {
          val json =
            """
              |{
              |  "path":"some-actor-path",
              |  "auth":"Connected",
              |  "id":"AGENT-ID",
              |  "updated":1417704004071,
              |  "workingState":{
              |    "state":"Working",
              |    "parser":"Parsing",
              |    "currentTransformationId":null,
              |    "id":"AGENT-ID",
              |    "processor":"Idle",
              |    "runtime":{
              |      "agent1.example.com": {
              |        "free":111101592,
              |        "max":1900019712,
              |        "total":128974848,
              |        "processors":1,
              |        "load":null
              |      }
              |    }
              |  }
              |}
            """.stripMargin

          val expected = new AgentInformation(
            id = "AGENT-ID",
            auth = AgentAuthorizationState.Connected,
            path = "some-actor-path",
            lastUpdated = 1417704004071L,
            workingState = Option(
              new AgentWorkingState(
                id = "AGENT-ID",
                state = TenseiAgentState.Working,
                parser = ParserState.Parsing,
                processor = ProcessorState.Idle,
                runtimeStats = Map(
                  "agent1.example.com" → new RuntimeStats(freeMemory = 111101592,
                                                          maxMemory = 1900019712,
                                                          totalMemory = 128974848)
                )
              )
            )
          )

          Parse.decodeOption[AgentInformation](json).get must be(expected)
        }
      }

      describe("encode") {
        it("must encode to proper json") {
          val info = new AgentInformation(
            id = "AGENT-ID",
            auth = AgentAuthorizationState.Connected,
            path = "some-actor-path",
            lastUpdated = 1417704004072L,
            workingState = Option(
              new AgentWorkingState(
                id = "AGENT-ID",
                state = TenseiAgentState.Working,
                parser = ParserState.Parsing,
                processor = ProcessorState.Idle,
                runtimeStats = Map(
                  "agent42.example.com" → new RuntimeStats(freeMemory = 111101592,
                                                           maxMemory = 1900019712,
                                                           totalMemory = 128974848)
                )
              )
            )
          )

          val expected =
            """{"path":"some-actor-path","auth":"Connected","id":"AGENT-ID","updated":1417704004072,"workingState":{"uniqueIdentifier":null,"state":"Working","parser":"Parsing","id":"AGENT-ID","processor":"Idle","runtime":{"agent42.example.com":{"processors":1,"load":null,"total":128974848,"max":1900019712,"free":111101592}}}}"""

          info.asJson.nospaces must be(expected)
        }
      }
    }
  }
}
