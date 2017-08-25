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

class DFASDLReferenceTest extends DefaultSpec {
  describe("Constraints") {
    describe("cookbookId") {
      it("must not be empty") {
        an[IllegalArgumentException] must be thrownBy DFASDLReference("", "ID")
      }

      it("must not be null") {
        an[IllegalArgumentException] must be thrownBy DFASDLReference(null, "ID")
      }
    }

    describe("dfasdlId") {
      it("must not be empty") {
        an[IllegalArgumentException] must be thrownBy DFASDLReference("ID", "")
      }

      it("must not be null") {
        an[IllegalArgumentException] must be thrownBy DFASDLReference("ID", null)
      }
    }
  }

  describe("JsonCodec") {
    describe("encode") {
      it("must properly encode an object to json") {
        val r = DFASDLReference("COOKBOOK-ID", "DFASDL-ID")

        val expectedJson = """{"cookbook-id":"COOKBOOK-ID","dfasdl-id":"DFASDL-ID"}"""

        r.asJson.nospaces must be(expectedJson)
      }
    }

    describe("decode") {
      it("must properly decode json to an object") {
        val json              = """{"cookbook-id":"MY-COOKBOOK","dfasdl-id":"MY-DFASDL"}"""
        val expectedReference = DFASDLReference("MY-COOKBOOK", "MY-DFASDL")

        val decoded = Parse.decodeOption[DFASDLReference](json)
        decoded.isDefined must be(true)
        decoded.get must be(expectedReference)
        decoded.get.cookbookId must be(expectedReference.cookbookId)
        decoded.get.dfasdlId must be(expectedReference.dfasdlId)
      }
    }
  }
}
