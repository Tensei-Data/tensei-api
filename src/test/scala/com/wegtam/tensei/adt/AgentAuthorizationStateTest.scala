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

class AgentAuthorizationStateTest extends DefaultSpec {
  describe("AgentAuthorizationState") {
    describe("CodecJson") {
      describe("decode") {
        it("must decode all known states correctly") {
          Parse.decodeOption[AgentAuthorizationState](""""Connected"""").get must be(
            AgentAuthorizationState.Connected
          )
          Parse.decodeOption[AgentAuthorizationState](""""Disconnected"""").get must be(
            AgentAuthorizationState.Disconnected
          )
          Parse.decodeOption[AgentAuthorizationState](""""Unauthorized"""").get must be(
            AgentAuthorizationState.Unauthorized
          )
        }

        it("must throw an IllegalArgumentException upon an unknown state") {
          an[IllegalArgumentException] shouldBe thrownBy(
            Parse.decode[AgentAuthorizationState](""""SomeUnknownState"""")
          )
        }
      }

      describe("encode") {
        it("must encode all known states correctly") {
          AgentAuthorizationState.Connected.asJson.nospaces must be(""""Connected"""")
          AgentAuthorizationState.Disconnected.asJson.nospaces must be(""""Disconnected"""")
          AgentAuthorizationState.Unauthorized.asJson.nospaces must be(""""Unauthorized"""")
        }
      }
    }
  }
}
