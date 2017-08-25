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

class ParserStateTest extends DefaultSpec {
  describe("ParserState") {
    describe("CodecJson") {
      describe("decode") {
        it("must decode all known states correctly") {
          Parse.decodeOption[ParserState](""""Idle"""").get must be(ParserState.Idle)
          Parse.decodeOption[ParserState](""""ValidatingSyntax"""").get must be(
            ParserState.ValidatingSyntax
          )
          Parse.decodeOption[ParserState](""""ValidatingAccess"""").get must be(
            ParserState.ValidatingAccess
          )
          Parse.decodeOption[ParserState](""""ValidatingChecksums"""").get must be(
            ParserState.ValidatingChecksums
          )
          Parse.decodeOption[ParserState](""""PreparingSourceData"""").get must be(
            ParserState.PreparingSourceData
          )
          Parse.decodeOption[ParserState](""""InitializingSubParsers"""").get must be(
            ParserState.InitializingSubParsers
          )
          Parse.decodeOption[ParserState](""""Parsing"""").get must be(ParserState.Parsing)
        }

        it("must throw an IllegalArgumentException upon an unknown state") {
          an[IllegalArgumentException] shouldBe thrownBy(
            Parse.decode[ParserState](""""SomeUnknownState"""")
          )
        }
      }

      describe("encode") {
        it("must encode all known states correctly") {
          ParserState.Idle.asJson.nospaces must be(""""Idle"""")
          ParserState.ValidatingSyntax.asJson.nospaces must be(""""ValidatingSyntax"""")
          ParserState.ValidatingAccess.asJson.nospaces must be(""""ValidatingAccess"""")
          ParserState.ValidatingChecksums.asJson.nospaces must be(""""ValidatingChecksums"""")
          ParserState.PreparingSourceData.asJson.nospaces must be(""""PreparingSourceData"""")
          ParserState.InitializingSubParsers.asJson.nospaces must be(
            """"InitializingSubParsers""""
          )
          ParserState.Parsing.asJson.nospaces must be(""""Parsing"""")
        }
      }
    }
  }
}
