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

class AtomicTransformationDescriptionTest extends DefaultSpec {
  describe("JsonCodec") {
    describe("encode") {
      it("must properly encode an object to json") {
        val params = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params)
        val t =
          new AtomicTransformationDescription(ElementReference("MY-DFASDL-ID", "MY-SOURCE-ID"),
                                              "com.example.transformers.foo",
                                              o)
        val expectedJson =
          """{"element":{"elementId":"MY-SOURCE-ID","dfasdlId":"MY-DFASDL-ID"},"transformerClassName":"com.example.transformers.foo","options":{"srcType":"java.lang.String","dstType":"java.lang.Long","params":[["one","1"],["two","Zwei"],["three","3.14f"]]}}"""
        t.asJson.nospaces mustEqual expectedJson
      }
    }

    describe("decode") {
      it("must properly decode json to an object") {
        val params = List(("one", "1"), ("two", "Zweikommasechs"), ("three", "3.14f"))
        val o      = new TransformerOptions(classOf[java.lang.Integer], classOf[java.lang.Long], params)
        val expected =
          new AtomicTransformationDescription(ElementReference("DFASDL-ID", "ANOTHER-SOURCE-ID"),
                                              "com.example.transformers.bar",
                                              o)
        val jsonString =
          """{"element":{"elementId":"ANOTHER-SOURCE-ID","dfasdlId":"DFASDL-ID"},"transformerClassName":"com.example.transformers.bar","options":{"srcType":"java.lang.Integer","dstType":"java.lang.Long","params":[["one","1"],["two","Zweikommasechs"],["three","3.14f"]]}}"""
        Parse.decodeEither[AtomicTransformationDescription](jsonString) match {
          case -\/(failure) => fail(failure)
          case \/-(success) =>
            success must be(expected)
            success.element must be(expected.element)
            success.transformerClassName must be(expected.transformerClassName)
            success.options must be(expected.options)
        }
      }
    }
  }
}
