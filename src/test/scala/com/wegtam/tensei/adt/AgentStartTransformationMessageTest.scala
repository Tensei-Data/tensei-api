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
import java.net.URI

import com.wegtam.tensei.adt.Recipe.MapAllToAll

import scalaz._

class AgentStartTransformationMessageTest extends DefaultSpec {

  describe("JsonCodec") {
    describe("encode") {
      it("must properly encode an object to json") {
        val con1 = new ConnectionInformation(new URI("http://www.example.com"),
                                             Some(DFASDLReference("MY-COOKBOOK", "SOURCE-01")),
                                             Some("user01"),
                                             Some("pw01"),
                                             None,
                                             None)
        val con2 = new ConnectionInformation(new URI("http://www.example.com"),
                                             Some(DFASDLReference("MY-COOKBOOK", "TARGET-ID")),
                                             Some("user02"),
                                             Some("pw02"),
                                             None,
                                             Some("de_DE"))

        val params = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params)
        val t      = new TransformationDescription("com.example.transformers.foo", o)
        val sourceElements = List(
          ElementReference("DFASDL", "source01"),
          ElementReference("DFASDL", "source02")
        )
        val targetElements = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m = new MappingTransformation(sourceElements, targetElements, List(t))
        val r = List(new Recipe("my-recipe", MapAllToAll, List(m)))
        val sources = List(
          DFASDL(
            "SOURCE-01",
            "<dfasdl><str id=\"source01\"/><str id=\"source02\"/><str id=\"source03\"/></dfasdl>"
          ),
          DFASDL("SOURCE-02", "<dfasdl><str id=\"source04\"/><str id=\"source05\"/></dfasdl>")
        )
        val target = Some(
          DFASDL(
            "TARGET-ID",
            "<dfasdl><str id=\"target01\"/><str id=\"target02\"/><str id=\"target03\"/></dfasdl>"
          )
        )
        val as = new AgentStartTransformationMessage(List(con1),
                                                     con2,
                                                     Cookbook("MY-COOKBOOK", sources, target, r))

        val expectedJson =
          """
            |{
            |  "target" : {
            |    "username" : "user02",
            |    "uri" : "http://www.example.com",
            |    "dfasdlRef" : {
            |      "cookbook-id" : "MY-COOKBOOK",
            |      "dfasdl-id" : "TARGET-ID"
            |    },
            |    "checksum" : null,
            |    "password" : "pw02",
            |    "languageTag" : "de_DE"
            |  },
            |  "sources" : [
            |    {
            |      "username" : "user01",
            |      "uri" : "http://www.example.com",
            |      "dfasdlRef" : {
            |        "cookbook-id" : "MY-COOKBOOK",
            |        "dfasdl-id" : "SOURCE-01"
            |      },
            |      "checksum" : null,
            |      "password" : "pw01",
            |      "languageTag" : null
            |    }
            |  ],
            |  "cookbook" : {
            |    "id" : "MY-COOKBOOK",
            |    "sources" : [
            |      {
            |        "version" : "1.0-SNAPSHOT",
            |        "content" : "<dfasdl><str id=\"source01\"/><str id=\"source02\"/><str id=\"source03\"/></dfasdl>",
            |        "id" : "SOURCE-01"
            |      },
            |      {
            |        "version" : "1.0-SNAPSHOT",
            |        "content" : "<dfasdl><str id=\"source04\"/><str id=\"source05\"/></dfasdl>",
            |        "id" : "SOURCE-02"
            |      }
            |    ],
            |    "target" : {
            |      "version" : "1.0-SNAPSHOT",
            |      "content" : "<dfasdl><str id=\"target01\"/><str id=\"target02\"/><str id=\"target03\"/></dfasdl>",
            |      "id" : "TARGET-ID"
            |    },
            |    "recipes" : [
            |      {
            |        "id" : "my-recipe",
            |        "mode" : "MapAllToAll",
            |        "mappings" : [
            |          {
            |            "targets" : [
            |              {"elementId": "target01", "dfasdlId": "DFASDL"},
            |              {"elementId": "target02", "dfasdlId": "DFASDL"},
            |              {"elementId": "target03", "dfasdlId": "DFASDL"}
            |            ],
            |            "mappingKey" : null,
            |            "sources" : [
            |              {"elementId": "source01", "dfasdlId": "DFASDL"},
            |              {"elementId": "source02", "dfasdlId": "DFASDL"}
            |            ],
            |            "transformations" : [
            |              {
            |                "transformerClassName" : "com.example.transformers.foo",
            |                "options" : {
            |                  "srcType" : "java.lang.String",
            |                  "dstType" : "java.lang.Long",
            |                  "params" : [
            |                    [
            |                      "one",
            |                      "1"
            |                    ],
            |                    [
            |                      "two",
            |                      "Zwei"
            |                    ],
            |                    [
            |                      "three",
            |                      "3.14f"
            |                    ]
            |                  ]
            |                }
            |              }
            |            ],
            |            "atomicTransformations" : [
            |
            |            ]
            |          }
            |        ]
            |      }
            |    ]
            |  }
            |  ,"uniqueIdentifier":null
            |}
          """.stripMargin

        as.asJson.nospaces mustEqual Parse.parseOption(expectedJson).get.nospaces
      }
    }

    describe("decode") {
      it("must properly decode json to an object") {
        val jsonString =
          """
            |{
            |  "target" : {
            |    "username" : "username02",
            |    "uri" : "http://www.example2.com",
            |    "dfasdlRef" : {
            |      "cookbook-id" : "ANOTHER-COOKBOOK",
            |      "dfasdl-id" : "TARGET-ID"
            |    },
            |    "checksum" : null,
            |    "password" : "passw02"
            |  },
            |  "sources" : [
            |    {
            |      "username" : "username01",
            |      "uri" : "http://www.example.com",
            |      "dfasdlRef" : {
            |        "cookbook-id" : "ANOTHER-COOKBOOK",
            |        "dfasdl-id" : "SOURCE-02"
            |      },
            |      "checksum" : null,
            |      "password" : "passw01",
            |      "languageTag" : "en_US"
            |    }
            |  ],
            |  "cookbook" : {
            |    "id" : "ANOTHER-COOKBOOK",
            |    "sources" : [
            |      {
            |        "version" : "1.0-SNAPSHOT",
            |        "content" : "<dfasdl><str id=\"source01\"/><str id=\"source02\"/><str id=\"source03\"/></dfasdl>",
            |        "id" : "SOURCE-01"
            |      },
            |      {
            |        "version" : "1.0-SNAPSHOT",
            |        "content" : "<dfasdl><str id=\"source04\"/><str id=\"source05\"/></dfasdl>",
            |        "id" : "SOURCE-02"
            |      }
            |    ],
            |    "target" : {
            |      "version" : "1.0-SNAPSHOT",
            |      "content" : "<dfasdl><str id=\"target01\"/><str id=\"target02\"/><str id=\"target03\"/></dfasdl>",
            |      "id" : "TARGET-ID"
            |    },
            |    "recipes" : [
            |      {
            |        "id" : "my-recipe2",
            |        "mode" : "MapAllToAll",
            |        "mappings" : [
            |          {
            |            "sources" : [
            |              {"elementId": "source01", "dfasdlId": "DFASDL"},
            |              {"elementId": "source02", "dfasdlId": "DFASDL"}
            |            ],
            |            "targets" : [
            |              {"elementId": "target01", "dfasdlId": "DFASDL"},
            |              {"elementId": "target02", "dfasdlId": "DFASDL"},
            |              {"elementId": "target03", "dfasdlId": "DFASDL"}
            |            ],
            |            "transformations" : [
            |              {
            |                "transformerClassName" : "com.example.transformers.foo",
            |                "options" : {
            |                  "srcType" : "java.lang.String",
            |                  "dstType" : "java.lang.Long",
            |                  "params" : [
            |                    [
            |                      "one",
            |                      "1"
            |                    ],
            |                    [
            |                      "two",
            |                      "Zwei"
            |                    ],
            |                    [
            |                      "three",
            |                      "3.14f"
            |                    ]
            |                  ]
            |                }
            |              }
            |            ],
            |            "atomicTransformations": []
            |          }
            |        ]
            |      }
            |    ]
            |  }
            |}
          """.stripMargin

        val con1 =
          new ConnectionInformation(new URI("http://www.example.com"),
                                    Some(DFASDLReference("ANOTHER-COOKBOOK", "SOURCE-02")),
                                    Some("username01"),
                                    Some("passw01"),
                                    None,
                                    Some("en_US"))
        val con2 =
          new ConnectionInformation(new URI("http://www.example2.com"),
                                    Some(DFASDLReference("ANOTHER-COOKBOOK", "TARGET-ID")),
                                    Some("username02"),
                                    Some("passw02"),
                                    None)

        val params = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params)
        val t      = new TransformationDescription("com.example.transformers.foo", o)
        val sourceElements = List(
          ElementReference("DFASDL", "source01"),
          ElementReference("DFASDL", "source02")
        )
        val targetElements = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m = new MappingTransformation(sourceElements, targetElements, List(t))
        val r = List(new Recipe("my-recipe2", MapAllToAll, List(m)))
        val sources = List(
          DFASDL(
            "SOURCE-01",
            "<dfasdl><str id=\"source01\"/><str id=\"source02\"/><str id=\"source03\"/></dfasdl>"
          ),
          DFASDL("SOURCE-02", "<dfasdl><str id=\"source04\"/><str id=\"source05\"/></dfasdl>")
        )
        val target = Some(
          DFASDL(
            "TARGET-ID",
            "<dfasdl><str id=\"target01\"/><str id=\"target02\"/><str id=\"target03\"/></dfasdl>"
          )
        )

        val expected =
          new AgentStartTransformationMessage(List(con1),
                                              con2,
                                              Cookbook("ANOTHER-COOKBOOK", sources, target, r))

        Parse.decodeEither[AgentStartTransformationMessage](jsonString) match {
          case -\/(failure) ⇒ fail(failure)
          case \/-(success) ⇒ success must be(expected)
        }
      }
    }
  }

  describe("hasChecksums") {
    describe("with checksums") {
      it("must return true") {
        val con1 =
          new ConnectionInformation(new URI("http://www.example.com"),
                                    Some(DFASDLReference("ANOTHER-COOKBOOK", "SOURCE-01")),
                                    Some("username01"),
                                    Some("passw01"),
                                    None)
        val con2 = new ConnectionInformation(
          new URI("http://www.example.com"),
          Some(DFASDLReference("ANOTHER-COOKBOOK", "SOURCE-02")),
          Some("username01"),
          Some("passw01"),
          Some("7b6f0635cabc5c86934586fa2338b238530733e5581bba9bf8409e32fe7f0c8c")
        )
        val con3 =
          new ConnectionInformation(new URI("http://www.example2.com"),
                                    Some(DFASDLReference("ANOTHER-COOKBOOK", "TARGET-ID")),
                                    Some("username02"),
                                    Some("passw02"),
                                    None)

        val params = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params)
        val t      = new TransformationDescription("com.example.transformers.foo", o)
        val sourceElements = List(
          ElementReference("DFASDL", "source01"),
          ElementReference("DFASDL", "source02")
        )
        val targetElements = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m = new MappingTransformation(sourceElements, targetElements, List(t))
        val r = List(new Recipe("my-recipe2", MapAllToAll, List(m)))
        val sources = List(
          DFASDL(
            "SOURCE-01",
            "<dfasdl><str id=\"source01\"/><str id=\"source02\"/><str id=\"source03\"/></dfasdl>"
          ),
          DFASDL("SOURCE-02", "<dfasdl><str id=\"source04\"/><str id=\"source05\"/></dfasdl>")
        )
        val target = Some(
          DFASDL(
            "TARGET-ID",
            "<dfasdl><str id=\"target01\"/><str id=\"target02\"/><str id=\"target03\"/></dfasdl>"
          )
        )

        val message =
          new AgentStartTransformationMessage(List(con1, con2),
                                              con3,
                                              Cookbook("ANOTHER-COOKBOOK", sources, target, r))

        message.hasChecksums must be(true)
      }
    }

    describe("without checksums") {
      it("must return false") {
        val con1 =
          new ConnectionInformation(new URI("http://www.example.com"),
                                    Some(DFASDLReference("ANOTHER-COOKBOOK", "SOURCE-02")),
                                    Some("username01"),
                                    Some("passw01"),
                                    None)
        val con2 =
          new ConnectionInformation(new URI("http://www.example2.com"),
                                    Some(DFASDLReference("ANOTHER-COOKBOOK", "SOURCE-03")),
                                    Some("username02"),
                                    Some("passw02"),
                                    None)
        val con3 =
          new ConnectionInformation(new URI("http://www.example2.com"),
                                    Some(DFASDLReference("ANOTHER-COOKBOOK", "TARGET-ID")),
                                    Some("username02"),
                                    Some("passw02"),
                                    None)

        val params = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params)
        val t      = new TransformationDescription("com.example.transformers.foo", o)
        val sourceElements = List(
          ElementReference("DFASDL", "source01"),
          ElementReference("DFASDL", "source02")
        )
        val targetElements = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m = new MappingTransformation(sourceElements, targetElements, List(t))
        val r = List(new Recipe("my-recipe2", MapAllToAll, List(m)))
        val sources = List(
          DFASDL(
            "SOURCE-01",
            "<dfasdl><str id=\"source01\"/><str id=\"source02\"/><str id=\"source03\"/></dfasdl>"
          ),
          DFASDL("SOURCE-02", "<dfasdl><str id=\"source04\"/><str id=\"source05\"/></dfasdl>")
        )
        val target = Some(
          DFASDL(
            "TARGET-ID",
            "<dfasdl><str id=\"target01\"/><str id=\"target02\"/><str id=\"target03\"/></dfasdl>"
          )
        )

        val message =
          new AgentStartTransformationMessage(List(con1, con2),
                                              con3,
                                              Cookbook("ANOTHER-COOKBOOK", sources, target, r))

        message.hasChecksums must be(false)
      }
    }
  }
}
