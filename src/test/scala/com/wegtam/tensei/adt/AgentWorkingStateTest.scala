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

class AgentWorkingStateTest extends DefaultSpec {
  describe("AgentWorkingState") {
    describe("CodecJson") {
      describe("decode") {
        describe("without current transformation id") {
          it("must decode a proper json string") {
            val json =
              """
                |{
                |  "id":"MY-AGENT",
                |  "state":"Working",
                |  "parser":"Parsing",
                |  "processor":"Idle",
                |  "runtime":{
                |    "1": {
                |      "total":200,
                |      "max":100,
                |      "free":50,
                |      "processors":1,
                |      "load":null
                |    }
                |  }
                |}
              """.stripMargin
            val expectedWorkingState = AgentWorkingState("MY-AGENT",
                                                         TenseiAgentState.Working,
                                                         ParserState.Parsing,
                                                         ProcessorState.Idle,
                                                         Map("1" → RuntimeStats(50, 100, 200)),
                                                         None)

            Parse.decodeOption[AgentWorkingState](json).get must be(expectedWorkingState)
          }
        }

        describe("with current transformation id") {
          it("must decode a proper json string") {
            val json =
              """
                |{
                |  "id":"MY-AGENT",
                |  "state":"Working",
                |  "parser":"Parsing",
                |  "processor":"Idle",
                |  "runtime":{
                |    "1": {
                |      "total":200,
                |      "max":100,
                |      "free":50,
                |      "processors":1,
                |      "load":null
                |    }
                |  },
                |  "uniqueIdentifier":"de305d54-75b4-431b-adb2-eb6b9e546013"
                |}
              """.stripMargin
            val expectedWorkingState = AgentWorkingState(
              "MY-AGENT",
              TenseiAgentState.Working,
              ParserState.Parsing,
              ProcessorState.Idle,
              Map("1" → RuntimeStats(50, 100, 200)),
              Option("de305d54-75b4-431b-adb2-eb6b9e546013")
            )

            Parse.decodeOption[AgentWorkingState](json).get must be(expectedWorkingState)
          }
        }
      }

      describe("encode") {
        describe("without current transformation id") {
          it("must encode to proper json") {
            val state = AgentWorkingState("MY-AGENT",
                                          TenseiAgentState.Working,
                                          ParserState.Parsing,
                                          ProcessorState.Idle,
                                          Map("1" → RuntimeStats(50, 100, 200)))
            val expectedJson =
              """{"uniqueIdentifier":null,"state":"Working","parser":"Parsing","id":"MY-AGENT","processor":"Idle","runtime":{"1":{"processors":1,"load":null,"total":200,"max":100,"free":50}}}"""
            state.asJson.nospaces must be(expectedJson)
          }
        }

        describe("with current transformation id") {
          it("must encode to proper json") {
            val state = AgentWorkingState(
              "MY-AGENT",
              TenseiAgentState.Working,
              ParserState.Parsing,
              ProcessorState.Idle,
              Map("1" → RuntimeStats(50, 100, 200)),
              Option("de305d54-75b4-431b-adb2-eb6b9e546013")
            )
            val expectedJson =
              """{"uniqueIdentifier":"de305d54-75b4-431b-adb2-eb6b9e546013","state":"Working","parser":"Parsing","id":"MY-AGENT","processor":"Idle","runtime":{"1":{"processors":1,"load":null,"total":200,"max":100,"free":50}}}"""
            state.asJson.nospaces must be(expectedJson)
          }
        }
      }
    }
  }
}
