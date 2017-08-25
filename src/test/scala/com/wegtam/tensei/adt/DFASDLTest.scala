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

class DFASDLTest extends DefaultSpec {
  describe("DFASDL") {
    describe("CodecJson") {
      describe("encode") {
        it("must properly encode an object to json") {
          val dfasdl = DFASDL(
            "ID-01",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><dfasdl xmlns=\"http://www.dfasdl.org/DFASDL\" semantic=\"niem\"><num id=\"source01\" stop-sign=\":\"/><elem id=\"full-name\"><str id=\"source02\" stop-sign=\",\"/><str id=\"source03\" stop-sign=\",\"/></elem></dfasdl>",
            "0.5.0"
          )

          val expectedJson =
            """
              |{
              |  "version" : "0.5.0",
              |  "content" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?><dfasdl xmlns=\"http://www.dfasdl.org/DFASDL\" semantic=\"niem\"><num id=\"source01\" stop-sign=\":\"/><elem id=\"full-name\"><str id=\"source02\" stop-sign=\",\"/><str id=\"source03\" stop-sign=\",\"/></elem></dfasdl>",
              |  "id" : "ID-01"
              |}
            """.stripMargin

          dfasdl.asJson.nospaces must be(Parse.parseOption(expectedJson).get.nospaces)
        }
      }

      describe("decode") {
        it("must properly decode json to an object") {
          val json =
            """
              |{
              |  "version" : "2.0",
              |  "content" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?><dfasdl xmlns=\"http://www.dfasdl.org/DFASDL\" semantic=\"niem\"><num id=\"source01\" stop-sign=\":\"/><elem id=\"full-name\"><str id=\"source02\" stop-sign=\",\"/><str id=\"source03\" stop-sign=\",\"/></elem></dfasdl>",
              |  "id" : "ID-01"
              |}
            """.stripMargin

          val expectedDfasdl = DFASDL(
            "ID-01",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><dfasdl xmlns=\"http://www.dfasdl.org/DFASDL\" semantic=\"niem\"><num id=\"source01\" stop-sign=\":\"/><elem id=\"full-name\"><str id=\"source02\" stop-sign=\",\"/><str id=\"source03\" stop-sign=\",\"/></elem></dfasdl>",
            "2.0"
          )

          val actualDfasdl = Parse.decodeOption[DFASDL](json)
          actualDfasdl.isDefined must be(true)
          actualDfasdl.get must be(expectedDfasdl)
          actualDfasdl.get.id must be(expectedDfasdl.id)
          actualDfasdl.get.content must be(expectedDfasdl.content)
          actualDfasdl.get.version must be(expectedDfasdl.version)
        }
      }
    }

    describe("autogenerateMissingIds") {
      describe("on an empty dfasdl") {
        it("must return the original dfasdl") {
          val dfasdl         = DFASDL("ID-01", "", "1.0")
          val returnedDfasdl = DFASDL.autogenerateMissingIds(dfasdl)
          returnedDfasdl must equal(dfasdl)
          returnedDfasdl.content must equal(dfasdl.content)
        }
      }

      describe("on a dfasdl without missing ids") {
        it("must return the original dfasdl") {
          val dfasdl = DFASDL(
            "ID-01",
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><dfasdl xmlns=\"http://www.dfasdl.org/DFASDL\" semantic=\"niem\"><num id=\"source01\" stop-sign=\":\"/><elem id=\"full-name\"><str id=\"source02\" stop-sign=\",\"/><str id=\"source03\" stop-sign=\",\"/></elem></dfasdl>",
            "1.0"
          )
          val returnedDfasdl = DFASDL.autogenerateMissingIds(dfasdl)
          returnedDfasdl must equal(dfasdl)
          returnedDfasdl.content must equal(dfasdl.content)
        }
      }

      describe("on a dfasdl with missing ids") {
        describe("without auto-generated ids") {
          it("must auto-generate the missing ids") {
            val dfasdl = DFASDL(
              "ID-01",
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><dfasdl xmlns=\"http://www.dfasdl.org/DFASDL\" semantic=\"niem\"><num id=\"source01\" stop-sign=\":\"/><elem id=\"full-name\"><str stop-sign=\",\"/><str stop-sign=\",\"/></elem></dfasdl>",
              "1.0"
            )
            val expectedDfasdl = DFASDL(
              "ID-01",
              s"""<?xml version="1.0" encoding="UTF-8" standalone="no"?><dfasdl xmlns="http://www.dfasdl.org/DFASDL" semantic="niem"><num id="source01" stop-sign=":"/><elem id="full-name"><str id="${DFASDL.AUTO_ID_PREFIX}-1" stop-sign=","/><str id="${DFASDL.AUTO_ID_PREFIX}-2" stop-sign=","/></elem></dfasdl>""",
              "1.0"
            )
            val returnedDfasdl = DFASDL.autogenerateMissingIds(dfasdl)
            returnedDfasdl must equal(expectedDfasdl)
            returnedDfasdl.content must equal(expectedDfasdl.content)
          }
        }

        describe("without auto-generated ids and with empty ids") {
          it("must auto-generate the missing and empty ids") {
            val dfasdl = DFASDL(
              "ID-01",
              "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><dfasdl xmlns=\"http://www.dfasdl.org/DFASDL\" semantic=\"niem\"><num id=\"source01\" stop-sign=\":\"/><elem id=\"full-name\"><str stop-sign=\",\"/><str id=\"\" stop-sign=\",\"/></elem></dfasdl>",
              "1.0"
            )
            val expectedDfasdl = DFASDL(
              "ID-01",
              s"""<?xml version="1.0" encoding="UTF-8" standalone="no"?><dfasdl xmlns="http://www.dfasdl.org/DFASDL" semantic="niem"><num id="source01" stop-sign=":"/><elem id="full-name"><str id="${DFASDL.AUTO_ID_PREFIX}-1" stop-sign=","/><str id="${DFASDL.AUTO_ID_PREFIX}-2" stop-sign=","/></elem></dfasdl>""",
              "1.0"
            )
            val returnedDfasdl = DFASDL.autogenerateMissingIds(dfasdl)
            returnedDfasdl must equal(expectedDfasdl)
            returnedDfasdl.content must equal(expectedDfasdl.content)
          }
        }

        describe("with auto-generated ids") {
          it("must auto-generate the missing ids using the last proper auto-generated value") {
            val dfasdl = DFASDL(
              "ID-01",
              s"""<?xml version="1.0" encoding="UTF-8" standalone="no"?><dfasdl xmlns="http://www.dfasdl.org/DFASDL" semantic="niem"><num id="${DFASDL.AUTO_ID_PREFIX}-16" stop-sign=":"/><elem id="full-name"><str stop-sign=","/><str stop-sign=","/></elem></dfasdl>""",
              "1.0"
            )
            val expectedDfasdl = DFASDL(
              "ID-01",
              s"""<?xml version="1.0" encoding="UTF-8" standalone="no"?><dfasdl xmlns="http://www.dfasdl.org/DFASDL" semantic="niem"><num id="${DFASDL.AUTO_ID_PREFIX}-16" stop-sign=":"/><elem id="full-name"><str id="${DFASDL.AUTO_ID_PREFIX}-17" stop-sign=","/><str id="${DFASDL.AUTO_ID_PREFIX}-18" stop-sign=","/></elem></dfasdl>""",
              "1.0"
            )
            val returnedDfasdl = DFASDL.autogenerateMissingIds(dfasdl)
            returnedDfasdl must equal(expectedDfasdl)
            returnedDfasdl.content must equal(expectedDfasdl.content)
          }
        }

        describe("with auto-generated ids and with empty ids") {
          it("must auto-generate the missing ids using the last proper auto-generated value") {
            val dfasdl = DFASDL(
              "ID-01",
              s"""<?xml version="1.0" encoding="UTF-8" standalone="no"?><dfasdl xmlns="http://www.dfasdl.org/DFASDL" semantic="niem"><num id="${DFASDL.AUTO_ID_PREFIX}-16" stop-sign=":"/><elem id="full-name"><str id="" stop-sign=","/><str stop-sign=","/></elem></dfasdl>""",
              "1.0"
            )
            val expectedDfasdl = DFASDL(
              "ID-01",
              s"""<?xml version="1.0" encoding="UTF-8" standalone="no"?><dfasdl xmlns="http://www.dfasdl.org/DFASDL" semantic="niem"><num id="${DFASDL.AUTO_ID_PREFIX}-16" stop-sign=":"/><elem id="full-name"><str id="${DFASDL.AUTO_ID_PREFIX}-17" stop-sign=","/><str id="${DFASDL.AUTO_ID_PREFIX}-18" stop-sign=","/></elem></dfasdl>""",
              "1.0"
            )
            val returnedDfasdl = DFASDL.autogenerateMissingIds(dfasdl)
            returnedDfasdl must equal(expectedDfasdl)
            returnedDfasdl.content must equal(expectedDfasdl.content)
          }
        }
      }
    }

    describe("equals") {
      describe("if compared with itself") {
        it("must return true") {
          val dfasdl = new DFASDL(id = "FOO", content = "I am not a valid DFASDL!")

          dfasdl.equals(dfasdl) must be(true)
          (dfasdl == dfasdl) must be(true)
        }
      }

      describe("if id, content and version are equal") {
        it("must return true") {
          val one = new DFASDL(id = "FOO", content = "I am not a valid DFASDL!")
          val two = new DFASDL(id = "FOO", content = "I am not a valid DFASDL!")

          one.equals(two) must be(true)
          (one == two) must be(true)
        }
      }

      describe("if id, content or version differ") {
        it("must return false") {
          val dfasdl =
            new DFASDL(id = "FOO", content = "I am not a valid DFASDL!", version = "1.0")
          val otherId      = dfasdl.copy(id = "BAR")
          val otherContent = dfasdl.copy(content = "I am also not a valid DFASDL! :-)")
          val otherVersion = dfasdl.copy(version = "0.1-alpha")

          dfasdl.equals(otherId) must be(false)
          (dfasdl == otherId) must be(false)

          dfasdl.equals(otherContent) must be(false)
          (dfasdl == otherContent) must be(false)

          dfasdl.equals(otherVersion) must be(false)
          (dfasdl == otherVersion) must be(false)
        }
      }
    }
  }
}
