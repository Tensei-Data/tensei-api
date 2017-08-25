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

package com.wegtam.tensei.agent

import argonaut._, Argonaut._

import com.wegtam.tensei.DefaultSpec

class TenseiAgentStateTest extends DefaultSpec {
  describe("TenseiAgentState") {
    describe("CodecJson") {
      describe("decode") {
        it("must decode all known states correctly") {
          Parse.decodeOption[TenseiAgentState](""""Aborting"""").get must be(
            TenseiAgentState.Aborting
          )
          Parse.decodeOption[TenseiAgentState](""""CleaningUp"""").get must be(
            TenseiAgentState.CleaningUp
          )
          Parse.decodeOption[TenseiAgentState](""""Idle"""").get must be(TenseiAgentState.Idle)
          Parse.decodeOption[TenseiAgentState](""""InitializingResources"""").get must be(
            TenseiAgentState.InitializingResources
          )
          Parse.decodeOption[TenseiAgentState](""""Working"""").get must be(
            TenseiAgentState.Working
          )
        }

        it("must throw an IllegalArgumentException upon an unknown state") {
          an[IllegalArgumentException] shouldBe thrownBy(
            Parse.decode[TenseiAgentState](""""SomeUnknownState"""")
          )
        }
      }

      describe("encode") {
        it("must encode all known states correctly") {
          TenseiAgentState.Aborting.asJson.nospaces must be(""""Aborting"""")
          TenseiAgentState.CleaningUp.asJson.nospaces must be(""""CleaningUp"""")
          TenseiAgentState.Idle.asJson.nospaces must be(""""Idle"""")
          TenseiAgentState.InitializingResources.asJson.nospaces must be(
            """"InitializingResources""""
          )
          TenseiAgentState.Working.asJson.nospaces must be(""""Working"""")
        }
      }
    }
  }
}
