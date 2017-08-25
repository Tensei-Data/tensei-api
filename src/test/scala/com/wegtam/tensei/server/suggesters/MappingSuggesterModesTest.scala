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

package com.wegtam.tensei.server.suggesters

import argonaut._, Argonaut._

import com.wegtam.tensei.DefaultSpec

class MappingSuggesterModesTest extends DefaultSpec {
  describe("MappingSuggesterModes") {
    describe("CodecJson") {
      describe("decode") {
        it("must decode all known modes correctly") {
          Parse.decodeOption[MappingSuggesterModes](""""Simple"""").get must be(
            MappingSuggesterModes.Simple
          )
          Parse.decodeOption[MappingSuggesterModes](""""SimpleWithTransformers"""").get must be(
            MappingSuggesterModes.SimpleWithTransformers
          )
          Parse.decodeOption[MappingSuggesterModes](""""SimpleSemantics"""").get must be(
            MappingSuggesterModes.SimpleSemantics
          )
          Parse
            .decodeOption[MappingSuggesterModes](""""SimpleSemanticsWithTransformers"""")
            .get must be(MappingSuggesterModes.SimpleSemanticsWithTransformers)
          Parse.decodeOption[MappingSuggesterModes](""""AdvancedSemantics"""").get must be(
            MappingSuggesterModes.AdvancedSemantics
          )
        }

        it("must throw an IllegalArgumentException upon an unknown mode") {
          an[IllegalArgumentException] shouldBe thrownBy(
            Parse.decode[MappingSuggesterModes](""""SomeUnknownMode"""")
          )
        }
      }

      describe("encode") {
        it("must encode all known modes correctly") {
          MappingSuggesterModes.Simple.asJson.nospaces must be(""""Simple"""")
          MappingSuggesterModes.SimpleWithTransformers.asJson.nospaces must be(
            """"SimpleWithTransformers""""
          )
          MappingSuggesterModes.SimpleSemantics.asJson.nospaces must be(""""SimpleSemantics"""")
          MappingSuggesterModes.SimpleSemanticsWithTransformers.asJson.nospaces must be(
            """"SimpleSemanticsWithTransformers""""
          )
          MappingSuggesterModes.AdvancedSemantics.asJson.nospaces must be(
            """"AdvancedSemantics""""
          )
        }
      }
    }
  }
}
