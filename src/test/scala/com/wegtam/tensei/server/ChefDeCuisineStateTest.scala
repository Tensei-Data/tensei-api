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

package com.wegtam.tensei.server

import argonaut._, Argonaut._

import com.wegtam.tensei.DefaultSpec

class ChefDeCuisineStateTest extends DefaultSpec {
  describe("ChefDeCuisineState") {
    describe("CodecJson") {
      describe("decode") {
        it("must decode all known states correctly") {
          Parse.decodeOption[ChefDeCuisineState](""""Booting"""").get must be(
            ChefDeCuisineState.Booting
          )
          Parse.decodeOption[ChefDeCuisineState](""""Initializing"""").get must be(
            ChefDeCuisineState.Initializing
          )
          Parse.decodeOption[ChefDeCuisineState](""""Running"""").get must be(
            ChefDeCuisineState.Running
          )
        }

        it("must throw an IllegalArgumentException upon an unknown state") {
          an[IllegalArgumentException] shouldBe thrownBy(
            Parse.decode[ChefDeCuisineState](""""SomeUnknownState"""")
          )
        }
      }

      describe("encode") {
        it("must encode all known states correctly") {
          ChefDeCuisineState.Booting.asJson.nospaces must be(""""Booting"""")
          ChefDeCuisineState.Initializing.asJson.nospaces must be(""""Initializing"""")
          ChefDeCuisineState.Running.asJson.nospaces must be(""""Running"""")
        }
      }
    }
  }
}
