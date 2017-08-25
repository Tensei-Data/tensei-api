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

class ProcessorStateTest extends DefaultSpec {
  describe("ProcessorState") {
    describe("CodecJson") {
      describe("decode") {
        it("must decode all known states correctly") {
          Parse.decodeOption[ProcessorState](""""Idle"""").get must be(ProcessorState.Idle)
          Parse.decodeOption[ProcessorState](""""Sorting"""").get must be(ProcessorState.Sorting)
          Parse.decodeOption[ProcessorState](""""Processing"""").get must be(
            ProcessorState.Processing
          )
          Parse.decodeOption[ProcessorState](""""WaitingForWriterClosing"""").get must be(
            ProcessorState.WaitingForWriterClosing
          )
        }

        it("must throw an IllegalArgumentException upon an unknown state") {
          an[IllegalArgumentException] shouldBe thrownBy(
            Parse.decode[ProcessorState](""""SomeUnknownState"""")
          )
        }
      }

      describe("encode") {
        it("must encode all known states correctly") {
          ProcessorState.Idle.asJson.nospaces must be(""""Idle"""")
          ProcessorState.Sorting.asJson.nospaces must be(""""Sorting"""")
          ProcessorState.Processing.asJson.nospaces must be(""""Processing"""")
          ProcessorState.WaitingForWriterClosing.asJson.nospaces must be(
            """"WaitingForWriterClosing""""
          )
        }
      }
    }
  }
}
