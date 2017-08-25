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

class TransformerOptionsTest extends DefaultSpec {
  describe("when creating a new transformer options object") {
    describe("it must only allow certain value types") {
      it("String must be valid") {
        val t = new TransformerOptions(classOf[String], classOf[String])
        t.srcType must be(classOf[String])
        t.dstType must be(classOf[String])
      }

      it("Array[Byte] must be valid") {
        val t = new TransformerOptions(classOf[Array[Byte]], classOf[Array[Byte]])
        t.srcType must be(classOf[Array[Byte]])
        t.dstType must be(classOf[Array[Byte]])
      }

      it("Boolean and String must be valid") {
        val t = new TransformerOptions(classOf[java.lang.Boolean], classOf[String])
        t.srcType must be(classOf[java.lang.Boolean])
        t.dstType must be(classOf[String])
      }

      it("Integer and Long must be valid") {
        val t = new TransformerOptions(classOf[Integer], classOf[java.lang.Long])
        t.srcType must be(classOf[Integer])
        t.dstType must be(classOf[java.lang.Long])
      }

      it("Character and Byte must be valid") {
        val t = new TransformerOptions(classOf[Character], classOf[java.lang.Byte])
        t.srcType must be(classOf[Character])
        t.dstType must be(classOf[java.lang.Byte])
      }
    }

    describe("when given a non empty list of parameters") {
      it("must accept a list of Tuple2(String, Any)") {
        val params = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val t      = new TransformerOptions(classOf[String], classOf[String], params)
        t.params must be(params)
      }
    }
  }

  describe("JsonCodec") {
    describe("encode") {
      it("must properly encode an object to json") {
        val params = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val t      = new TransformerOptions(classOf[String], classOf[String], params)
        val expectedJson =
          """{"srcType":"java.lang.String","dstType":"java.lang.String","params":[["one","1"],["two","Zwei"],["three","3.14f"]]}"""
        t.asJson.nospaces must be(expectedJson)
      }
    }

    describe("decode") {
      it("must properly decode json to an object") {
        val params   = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val expected = new TransformerOptions(classOf[String], classOf[String], params)
        val jsonString =
          """{"srcType":"java.lang.String","dstType":"java.lang.String","params":[["one","1"],["two","Zwei"],["three","3.14f"]]}"""
        val decoded: Option[TransformerOptions] =
          Parse.decodeOption[TransformerOptions](jsonString)
        decoded.isDefined must be(true)
        decoded.get must be(expected)
        decoded.get.srcType must be(expected.srcType)
        decoded.get.dstType must be(expected.dstType)
        decoded.get.params must be(expected.params)
      }
    }
  }
}
