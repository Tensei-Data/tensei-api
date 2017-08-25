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

class StatusTypeTest extends DefaultSpec {
  describe("StatusType") {
    describe("CodecJson") {
      describe("decode") {
        it("must decode all known types correctly") {
          Parse.decodeOption[StatusType](""""MinorError"""").get must be(StatusType.MinorError)
          Parse.decodeOption[StatusType](""""MajorError"""").get must be(StatusType.MajorError)
          Parse.decodeOption[StatusType](""""NoAgentAvailable"""").get must be(
            StatusType.NoAgentAvailable
          )
          Parse.decodeOption[StatusType](""""FatalError"""").get must be(StatusType.FatalError)
        }

        it("must throw an IllegalArgumentException upon an unknown type") {
          an[IllegalArgumentException] shouldBe thrownBy(
            Parse.decode[StatusType](""""SomeUnknownType"""")
          )
        }
      }

      describe("encode") {
        it("must encode all known types correctly") {
          StatusType.MinorError.asJson.nospaces must be(""""MinorError"""")
          StatusType.MajorError.asJson.nospaces must be(""""MajorError"""")
          StatusType.NoAgentAvailable.asJson.nospaces must be(""""NoAgentAvailable"""")
          StatusType.FatalError.asJson.nospaces must be(""""FatalError"""")
        }
      }
    }
  }
}
