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

import scalaz._

class ElementReferenceTest extends DefaultSpec {
  describe("ElementReference") {
    describe("Constraints") {
      describe("dfasdlId") {
        it("must not be empty") {
          an[IllegalArgumentException] must be thrownBy ElementReference(dfasdlId = "",
                                                                         elementId = "ID")
        }

        it("must not be null") {
          an[IllegalArgumentException] must be thrownBy ElementReference(dfasdlId = null,
                                                                         elementId = "ID")
        }
      }

      describe("elementId") {
        it("must not be empty") {
          an[IllegalArgumentException] must be thrownBy ElementReference(dfasdlId = "ID",
                                                                         elementId = "")
        }

        it("must not be null") {
          an[IllegalArgumentException] must be thrownBy ElementReference(dfasdlId = "ID",
                                                                         elementId = null)
        }
      }
    }

    describe("JsonCodec") {
      describe("decode") {
        it("must decode a proper json string correctly") {
          val expectedRef =
            ElementReference(dfasdlId = "MY-DFASDL-ID", elementId = "MY-ELEMENT-ID")
          val json =
            s"""
              |{
              |  "dfasdlId": "${expectedRef.dfasdlId}",
              |  "elementId": "${expectedRef.elementId}"
              |}
            """.stripMargin

          Parse.decodeEither[ElementReference](json) match {
            case -\/(failure) ⇒ fail(failure)
            case \/-(success) ⇒
              success must be(expectedRef)
              success.dfasdlId must be(expectedRef.dfasdlId)
              success.elementId must be(expectedRef.elementId)
          }
        }
      }

      describe("encode") {
        it("must produce a correct json string") {
          val ref = ElementReference(dfasdlId = "MY-DFASDL-ID", elementId = "MY-ELEMENT-ID")
          val expectedJson =
            s"""
               |{
               |  "elementId": "${ref.elementId}",
               |  "dfasdlId": "${ref.dfasdlId}"
               |}
            """.stripMargin

          val encodedRef = ref.asJson
          Parse.parse(expectedJson) match {
            case -\/(failure) ⇒ fail(failure)
            case \/-(success) ⇒
              encodedRef must be(success)
              encodedRef.nospaces must be(success.nospaces) // TODO We check the string here because we're unsure if the test above is enough.
          }
        }
      }
    }
  }
}
