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

class MappingTransformationTest extends DefaultSpec {
  describe("when no target id is specified") {
    it("must be invalid") {
      an[IllegalArgumentException] must be thrownBy MappingTransformation(
        List(ElementReference("dfasdl", "source")),
        List()
      )
    }
  }

  describe("JsonCodec") {
    describe("encode") {
      it("must properly encode an object to json") {
        val params = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params)
        val t      = new TransformationDescription("com.example.transformers.foo", o)
        val at = new AtomicTransformationDescription(ElementReference("DFASDL-1", "source01"),
                                                     "com.example.transformers.bar",
                                                     TransformerOptions(classOf[String],
                                                                        classOf[String],
                                                                        List()))
        val sources = List(
          ElementReference("DFASDL-1", "source01"),
          ElementReference("DFASDL-1", "source02")
        )
        val targets = List(
          ElementReference("DFASDL-2", "target01"),
          ElementReference("DFASDL-2", "target02"),
          ElementReference("DFASDL-2", "target03")
        )
        val m = new MappingTransformation(sources, targets, List(t), List(at))
        val expectedJson =
          """
            |{
            |  "targets" : [
            |    {"elementId": "target01", "dfasdlId": "DFASDL-2"},
            |    {"elementId": "target02", "dfasdlId": "DFASDL-2"},
            |    {"elementId": "target03", "dfasdlId": "DFASDL-2"}
            |  ],
            |  "mappingKey" : null,
            |  "sources" : [
            |    {"elementId": "source01", "dfasdlId": "DFASDL-1"},
            |    {"elementId": "source02", "dfasdlId": "DFASDL-1"}
            |  ],
            |  "transformations" : [
            |    {
            |      "transformerClassName" : "com.example.transformers.foo",
            |      "options" : {
            |        "srcType" : "java.lang.String",
            |        "dstType" : "java.lang.Long",
            |        "params" : [
            |          [
            |            "one",
            |            "1"
            |          ],
            |          [
            |            "two",
            |            "Zwei"
            |          ],
            |          [
            |            "three",
            |            "3.14f"
            |          ]
            |        ]
            |      }
            |    }
            |  ],
            |  "atomicTransformations" : [
            |    {
            |      "element" : {
            |        "elementId": "source01",
            |        "dfasdlId": "DFASDL-1"
            |      },
            |      "transformerClassName" : "com.example.transformers.bar",
            |      "options" : {
            |        "srcType" : "java.lang.String",
            |        "dstType" : "java.lang.String",
            |        "params" : [
            |
            |        ]
            |      }
            |    }
            |  ]
            |}
          """.stripMargin

        m.asJson.nospaces mustEqual Parse.parseOption(expectedJson).get.nospaces
      }
    }

    describe("decode") {
      it("must properly decode json to an object") {
        val jsonString =
          """
            |{
            |  "sources" : [
            |    {"elementId": "sourceABC", "dfasdlId": "DFASDL-1"},
            |    {"elementId": "source02", "dfasdlId": "DFASDL-1"}
            |  ],
            |  "targets" : [
            |    {"elementId": "target-XYZ", "dfasdlId": "DFASDL-2"},
            |    {"elementId": "target03", "dfasdlId": "DFASDL-2"}
            |  ],
            |  "transformations" : [
            |    {
            |      "transformerClassName" : "com.example.transformers.foo",
            |      "options" : {
            |        "srcType" : "java.lang.Integer",
            |        "dstType" : "java.lang.Long",
            |        "params" : [
            |          [
            |            "one",
            |            "ABC"
            |          ],
            |          [
            |            "two",
            |            "Zwei"
            |          ],
            |          [
            |            "three",
            |            "3.14f"
            |          ]
            |        ]
            |      }
            |    }
            |  ],
            |  "atomicTransformations" : [
            |    {
            |      "element" : {
            |        "dfasdlId": "DFASDL-1",
            |        "elementId": "sourceABC"
            |      },
            |      "transformerClassName" : "com.example.transformers.bar",
            |      "options" : {
            |        "srcType" : "java.lang.String",
            |        "dstType" : "java.lang.String",
            |        "params" : [
            |
            |        ]
            |      }
            |    }
            |  ]
            |}
          """.stripMargin
        val params = List(("one", "ABC"), ("two", "Zwei"), ("three", "3.14f"))
        val o      = new TransformerOptions(classOf[Integer], classOf[java.lang.Long], params)
        val t      = new TransformationDescription("com.example.transformers.foo", o)
        val at = new AtomicTransformationDescription(ElementReference("DFASDL-1", "sourceABC"),
                                                     "com.example.transformers.bar",
                                                     TransformerOptions(classOf[String],
                                                                        classOf[String],
                                                                        List()))
        val sources = List(
          ElementReference("DFASDL-1", "sourceABC"),
          ElementReference("DFASDL-1", "source02")
        )
        val targets = List(
          ElementReference("DFASDL-2", "target-XYZ"),
          ElementReference("DFASDL-2", "target03")
        )
        val expected = new MappingTransformation(sources, targets, List(t), List(at))
        Parse.decodeEither[MappingTransformation](jsonString) match {
          case -\/(failure) => fail(failure)
          case \/-(success) => success must be(expected)
        }
      }
    }
  }
}
