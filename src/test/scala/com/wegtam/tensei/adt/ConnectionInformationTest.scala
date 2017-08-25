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

import java.net.URI

import argonaut.Argonaut._
import argonaut._
import com.wegtam.tensei.DefaultSpec

class ConnectionInformationTest extends DefaultSpec {

  describe("JsonCodec") {
    describe("encode") {
      describe("with a dfasdl reference") {
        it("must properly encode an object to json") {
          val c = new ConnectionInformation(new URI("http://www.example.com"),
                                            Some(DFASDLReference("MY-COOKBOOK", "MY-DFASDL")),
                                            Some("testuser"),
                                            Some("testpw"),
                                            None,
                                            Some("en_US"))
          val expectedJson =
            """
              |{
              |  "username" : "testuser",
              |  "uri" : "http://www.example.com",
              |  "dfasdlRef" : {
              |    "cookbook-id" : "MY-COOKBOOK",
              |    "dfasdl-id" : "MY-DFASDL"
              |  },
              |  "checksum" : null,
              |  "password" : "testpw",
              |  "languageTag" : "en_US"
              |}
            """.stripMargin

          c.asJson.nospaces mustEqual Parse.parseOption(expectedJson).get.nospaces
        }
      }

      describe("without a dfasdl reference") {
        it("must properly encode an object to json") {
          val c = new ConnectionInformation(new URI("http://www.example.com"),
                                            None,
                                            Some("testuser"),
                                            Some("testpw"),
                                            None,
                                            None)
          val expectedJson =
            """{"languageTag":null,"username":"testuser","uri":"http://www.example.com","dfasdlRef":null,"checksum":null,"password":"testpw"}"""
          c.asJson.nospaces mustEqual expectedJson
        }
      }
    }

    describe("decode") {
      describe("with a dfasdl reference") {
        it("must properly decode json to an object") {
          val jsonString =
            """
              |{
              |  "username" : "user01",
              |  "uri" : "http://www.example.com",
              |  "dfasdlRef" : {
              |    "cookbook-id" : "MY-COOKBOOK",
              |    "dfasdl-id" : "MY-DFASDL"
              |  },
              |  "checksum" : null,
              |  "password" : "pw01",
              |  "languageTag" : "de_DE"
              |}
            """.stripMargin
          val expected =
            new ConnectionInformation(new URI("http://www.example.com"),
                                      Some(DFASDLReference("MY-COOKBOOK", "MY-DFASDL")),
                                      Some("user01"),
                                      Some("pw01"),
                                      None,
                                      Some("de_DE"))
          val decoded: Option[ConnectionInformation] =
            Parse.decodeOption[ConnectionInformation](jsonString)
          decoded.isDefined must be(true)
          decoded.get must be(expected)
          decoded.get.uri must be(expected.uri)
          decoded.get.dfasdlRef must be(expected.dfasdlRef)
          decoded.get.username must be(expected.username)
          decoded.get.password must be(expected.password)
          decoded.get.checksum must be(expected.checksum)
        }
      }

      describe("without a dfasdl reference") {
        it("must properly decode json to an object") {
          val jsonString =
            """{"dfasdlRef":null,"languageTag":null,"username":"user01","uri":"http://www.example.com","checksum":null,"password":"pw01"}"""
          val expected = new ConnectionInformation(new URI("http://www.example.com"),
                                                   None,
                                                   Some("user01"),
                                                   Some("pw01"),
                                                   None,
                                                   None)
          val decoded: Option[ConnectionInformation] =
            Parse.decodeOption[ConnectionInformation](jsonString)
          decoded.isDefined must be(true)
          decoded.get must be(expected)
          decoded.get.uri must be(expected.uri)
          decoded.get.dfasdlRef must be(expected.dfasdlRef)
          decoded.get.username must be(expected.username)
          decoded.get.password must be(expected.password)
          decoded.get.checksum must be(expected.checksum)
        }
      }
    }
  }

}
