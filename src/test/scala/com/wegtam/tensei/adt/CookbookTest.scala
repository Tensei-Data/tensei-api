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

class CookbookTest extends DefaultSpec {
  describe("Constraints") {
    describe("id") {
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

      val params1 = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
      val o1      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params1)
      val t1      = new TransformationDescription("com.example.transformers.foo", o1)
      val sourceElements = List(
        ElementReference("DFASDL", "source01"),
        ElementReference("DFASDL", "source02")
      )
      val targetElements = List(
        ElementReference("DFASDL", "target01"),
        ElementReference("DFASDL", "target02"),
        ElementReference("DFASDL", "target03")
      )
      val m1      = new MappingTransformation(sourceElements, targetElements, List(t1))
      val recipe1 = Recipe.createAllToAllRecipe("RECIPE-01", List(m1))

      val params2 = List(("one", "1"), ("two", "2.71f"), ("three", "3.00"))
      val o2      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params2)
      val t2      = new TransformationDescription("com.example.transformers.bar", o2)
      val sourceElements2 = List(
        ElementReference("DFASDL", "source04"),
        ElementReference("DFASDL", "source05")
      )
      val targetElements2 = List(
        ElementReference("DFASDL", "target01"),
        ElementReference("DFASDL", "target02"),
        ElementReference("DFASDL", "target03")
      )
      val m2      = new MappingTransformation(sourceElements2, targetElements2, List(t2))
      val recipe2 = Recipe.createAllToAllRecipe("RECIPE-02", List(m2))

      it("must not be empty") {
        an[IllegalArgumentException] must be thrownBy Cookbook("",
                                                               sources,
                                                               target,
                                                               List(recipe1, recipe2))
      }

      it("must not be null") {
        an[IllegalArgumentException] must be thrownBy Cookbook(null,
                                                               sources,
                                                               target,
                                                               List(recipe1, recipe2))
      }
    }
  }

  describe("JsonCodec") {
    describe("encode") {
      it("must properly encode an object to json") {
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

        val params1 = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o1      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params1)
        val t1      = new TransformationDescription("com.example.transformers.foo", o1)
        val sourceElements = List(
          ElementReference("DFASDL", "source01"),
          ElementReference("DFASDL", "source02")
        )
        val targetElements = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m1      = new MappingTransformation(sourceElements, targetElements, List(t1))
        val recipe1 = Recipe.createAllToAllRecipe("RECIPE-01", List(m1))

        val params2 = List(("one", "1"), ("two", "2.71f"), ("three", "3.00"))
        val o2      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params2)
        val t2      = new TransformationDescription("com.example.transformers.bar", o2)
        val sourceElements2 = List(
          ElementReference("DFASDL", "source04"),
          ElementReference("DFASDL", "source05")
        )
        val targetElements2 = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m2      = new MappingTransformation(sourceElements2, targetElements2, List(t2))
        val recipe2 = Recipe.createAllToAllRecipe("RECIPE-02", List(m2))

        val cookbook = Cookbook("COOKBOOK-01", sources, target, List(recipe1, recipe2))

        val expectedJson =
          """
            |{
            |  "id" : "COOKBOOK-01",
            |  "sources" : [
            |    {
            |      "version" : "1.0-SNAPSHOT",
            |      "content" : "<dfasdl><str id=\"source01\"/><str id=\"source02\"/><str id=\"source03\"/></dfasdl>",
            |      "id" : "SOURCE-01"
            |    },
            |    {
            |      "version" : "1.0-SNAPSHOT",
            |      "content" : "<dfasdl><str id=\"source04\"/><str id=\"source05\"/></dfasdl>",
            |      "id" : "SOURCE-02"
            |    }
            |  ],
            |  "target" : {
            |    "version" : "1.0-SNAPSHOT",
            |    "content" : "<dfasdl><str id=\"target01\"/><str id=\"target02\"/><str id=\"target03\"/></dfasdl>",
            |    "id" : "TARGET-ID"
            |  },
            |  "recipes" : [
            |    {
            |      "id" : "RECIPE-01",
            |      "mode" : "MapAllToAll",
            |      "mappings" : [
            |        {
            |          "targets" : [
            |            {"elementId": "target01", "dfasdlId": "DFASDL"},
            |            {"elementId": "target02", "dfasdlId": "DFASDL"},
            |            {"elementId": "target03", "dfasdlId": "DFASDL"}
            |          ],
            |          "mappingKey" : null,
            |          "sources" : [
            |            {"elementId": "source01", "dfasdlId": "DFASDL"},
            |            {"elementId": "source02", "dfasdlId": "DFASDL"}
            |          ],
            |          "transformations" : [
            |            {
            |              "transformerClassName" : "com.example.transformers.foo",
            |              "options" : {
            |                "srcType" : "java.lang.String",
            |                "dstType" : "java.lang.Long",
            |                "params" : [
            |                  [
            |                    "one",
            |                    "1"
            |                  ],
            |                  [
            |                    "two",
            |                    "Zwei"
            |                  ],
            |                  [
            |                    "three",
            |                    "3.14f"
            |                  ]
            |                ]
            |              }
            |            }
            |          ],
            |          "atomicTransformations" : [
            |
            |          ]
            |        }
            |      ]
            |    },
            |    {
            |      "id" : "RECIPE-02",
            |      "mode" : "MapAllToAll",
            |      "mappings" : [
            |        {
            |          "targets" : [
            |            {"elementId": "target01", "dfasdlId": "DFASDL"},
            |            {"elementId": "target02", "dfasdlId": "DFASDL"},
            |            {"elementId": "target03", "dfasdlId": "DFASDL"}
            |          ],
            |          "mappingKey" : null,
            |          "sources" : [
            |            {"elementId": "source04", "dfasdlId": "DFASDL"},
            |            {"elementId": "source05", "dfasdlId": "DFASDL"}
            |          ],
            |          "transformations" : [
            |            {
            |              "transformerClassName" : "com.example.transformers.bar",
            |              "options" : {
            |                "srcType" : "java.lang.String",
            |                "dstType" : "java.lang.Long",
            |                "params" : [
            |                  [
            |                    "one",
            |                    "1"
            |                  ],
            |                  [
            |                    "two",
            |                    "2.71f"
            |                  ],
            |                  [
            |                    "three",
            |                    "3.00"
            |                  ]
            |                ]
            |              }
            |            }
            |          ],
            |          "atomicTransformations" : [
            |
            |          ]
            |        }
            |      ]
            |    }
            |  ]
            |}
          """.stripMargin

        cookbook.asJson.nospaces must be(Parse.parseOption(expectedJson).get.nospaces)
      }
    }

    describe("decode") {
      it("must properly decode json to an object") {
        val json =
          """
            |{
            |  "id" : "COOKBOOK-02",
            |  "sources" : [
            |    {
            |      "version" : "1.0-SNAPSHOT",
            |      "content" : "<dfasdl><str id=\"source01\"/><str id=\"source02\"/><str id=\"source03\"/></dfasdl>",
            |      "id" : "SOURCE-01"
            |    },
            |    {
            |      "version" : "1.0-SNAPSHOT",
            |      "content" : "<dfasdl><str id=\"source04\"/><str id=\"source05\"/></dfasdl>",
            |      "id" : "SOURCE-02"
            |    }
            |  ],
            |  "target" : {
            |    "version" : "1.0-SNAPSHOT",
            |    "content" : "<dfasdl><str id=\"target01\"/><str id=\"target02\"/><str id=\"target03\"/></dfasdl>",
            |    "id" : "TARGET-ID"
            |  },
            |  "recipes" : [
            |    {
            |      "id" : "RECIPE-01",
            |      "mode" : "MapAllToAll",
            |      "mappings" : [
            |        {
            |          "sources" : [
            |            {"elementId": "source02", "dfasdlId": "DFASDL"},
            |            {"elementId": "source03", "dfasdlId": "DFASDL"}
            |          ],
            |          "targets" : [
            |            {"elementId": "target01", "dfasdlId": "DFASDL"},
            |            {"elementId": "target02", "dfasdlId": "DFASDL"},
            |            {"elementId": "target03", "dfasdlId": "DFASDL"}
            |          ],
            |          "transformations" : [
            |            {
            |              "transformerClassName" : "com.example.transformers.foo",
            |              "options" : {
            |                "srcType" : "java.lang.String",
            |                "dstType" : "java.lang.Long",
            |                "params" : [
            |                  [
            |                    "one",
            |                    "1"
            |                  ],
            |                  [
            |                    "two",
            |                    "Zwei"
            |                  ],
            |                  [
            |                    "three",
            |                    "3.14f"
            |                  ]
            |                ]
            |              }
            |            }
            |          ],
            |          "atomicTransformations": []
            |        }
            |      ]
            |    },
            |    {
            |      "id" : "RECIPE-02",
            |      "mode" : "MapAllToAll",
            |      "mappings" : [
            |        {
            |          "sources" : [
            |            {"elementId": "source04", "dfasdlId": "DFASDL"},
            |            {"elementId": "source05", "dfasdlId": "DFASDL"}
            |          ],
            |          "targets" : [
            |            {"elementId": "target01", "dfasdlId": "DFASDL"},
            |            {"elementId": "target02", "dfasdlId": "DFASDL"},
            |            {"elementId": "target03", "dfasdlId": "DFASDL"}
            |          ],
            |          "transformations" : [
            |            {
            |              "transformerClassName" : "com.example.transformers.bar",
            |              "options" : {
            |                "srcType" : "java.lang.String",
            |                "dstType" : "java.lang.Long",
            |                "params" : [
            |                  [
            |                    "one",
            |                    "1"
            |                  ],
            |                  [
            |                    "two",
            |                    "2.71f"
            |                  ],
            |                  [
            |                    "three",
            |                    "3.00"
            |                  ]
            |                ]
            |              }
            |            }
            |          ],
            |          "atomicTransformations": []
            |        }
            |      ]
            |    }
            |  ]
            |}
          """.stripMargin

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

        val params1 = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o1      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params1)
        val t1      = new TransformationDescription("com.example.transformers.foo", o1)
        val sourceElements = List(
          ElementReference("DFASDL", "source02"),
          ElementReference("DFASDL", "source03")
        )
        val targetElements = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m1      = new MappingTransformation(sourceElements, targetElements, List(t1))
        val recipe1 = Recipe.createAllToAllRecipe("RECIPE-01", List(m1))

        val params2 = List(("one", "1"), ("two", "2.71f"), ("three", "3.00"))
        val o2      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params2)
        val t2      = new TransformationDescription("com.example.transformers.bar", o2)
        val sourceElements2 = List(
          ElementReference("DFASDL", "source04"),
          ElementReference("DFASDL", "source05")
        )
        val targetElements2 = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m2      = new MappingTransformation(sourceElements2, targetElements2, List(t2))
        val recipe2 = Recipe.createAllToAllRecipe("RECIPE-02", List(m2))

        val expectedCookbook = Cookbook("COOKBOOK-02", sources, target, List(recipe1, recipe2))

        Parse.decodeEither[Cookbook](json) match {
          case -\/(failure) ⇒ fail(failure)
          case \/-(success) ⇒
            success must be(expectedCookbook)
        }
      }
    }
  }

  describe("findDFASDL") {
    val sources = List(
      DFASDL(
        "SOURCE-01",
        "<dfasdl><str id=\"source01\"/><str id=\"source02\"/><str id=\"source03\"/></dfasdl>"
      ),
      DFASDL("SOURCE-02", "<dfasdl><str id=\"source04\"/><str id=\"source05\"/></dfasdl>")
    )
    val target = Some(
      DFASDL("TARGET-ID",
             "<dfasdl><str id=\"target01\"/><str id=\"target02\"/><str id=\"target03\"/></dfasdl>")
    )

    val params1 = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
    val o1      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params1)
    val t1      = new TransformationDescription("com.example.transformers.foo", o1)
    val sourceElements = List(
      ElementReference("DFASDL", "source02"),
      ElementReference("DFASDL", "source03")
    )
    val targetElements = List(
      ElementReference("DFASDL", "target01"),
      ElementReference("DFASDL", "target02"),
      ElementReference("DFASDL", "target03")
    )
    val m1      = new MappingTransformation(sourceElements, targetElements, List(t1))
    val recipe1 = Recipe.createAllToAllRecipe("RECIPE-01", List(m1))

    val params2 = List(("one", "1"), ("two", "2.71f"), ("three", "3.00"))
    val o2      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params2)
    val t2      = new TransformationDescription("com.example.transformers.bar", o2)
    val sourceElements2 = List(
      ElementReference("DFASDL", "source04"),
      ElementReference("DFASDL", "source05")
    )
    val targetElements2 = List(
      ElementReference("DFASDL", "target01"),
      ElementReference("DFASDL", "target02"),
      ElementReference("DFASDL", "target03")
    )
    val m2      = new MappingTransformation(sourceElements2, targetElements2, List(t2))
    val recipe2 = Recipe.createAllToAllRecipe("RECIPE-02", List(m2))

    val cookbook = Cookbook("MY-COOKBOOK", sources, target, List(recipe1, recipe2))

    describe("when using wrong cookbook id") {
      it("must return None") {
        val ref = DFASDLReference("ANOTHER-COOKBOOK", "SOURCE-01")

        cookbook.findDFASDL(ref) must be(None)
      }
    }

    describe("when using unknown dfasdl id") {
      it("must return None") {
        val ref = DFASDLReference("MY-COOKBOOK", "UNKNOWN-DFASDL")

        cookbook.findDFASDL(ref) must be(None)
      }
    }

    describe("when using the target dfasdl id") {
      it("must return an option to the target dfasdl") {
        val ref = DFASDLReference("MY-COOKBOOK", "TARGET-ID")

        val dfasdl = cookbook.findDFASDL(ref)
        dfasdl.isDefined must be(true)
        dfasdl must be(target)
      }
    }

    describe("when using a source dfasdl id") {
      it("must return an option to the source dfasdl") {
        val ref = DFASDLReference("MY-COOKBOOK", "SOURCE-02")

        val dfasdl = cookbook.findDFASDL(ref)
        dfasdl.isDefined must be(true)
        dfasdl.get must be(sources(1))
      }
    }
  }

  describe("usedSourceIds") {
    describe("without mappings") {
      it("must return an empty list") {
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

        val cookbook = Cookbook("MY-COOKBOOK", sources, target, List.empty[Recipe])

        cookbook.usedSourceIds must be(Set.empty[String])
      }
    }

    describe("with mappings") {
      it("must return the list of mapped source ids") {
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

        val params1 = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o1      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params1)
        val t1      = new TransformationDescription("com.example.transformers.foo", o1)
        val sourceElements = List(
          ElementReference("DFASDL", "source02"),
          ElementReference("DFASDL", "source03")
        )
        val targetElements = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m1      = new MappingTransformation(sourceElements, targetElements, List(t1))
        val recipe1 = Recipe.createAllToAllRecipe("RECIPE-01", List(m1))

        val params2 = List(("one", "1"), ("two", "2.71f"), ("three", "3.00"))
        val o2      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params2)
        val t2      = new TransformationDescription("com.example.transformers.bar", o2)
        val sourceElements2 = List(
          ElementReference("DFASDL", "source04"),
          ElementReference("DFASDL", "source05"),
          ElementReference("DFASDL", "source02")
        )
        val targetElements2 = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m2      = new MappingTransformation(sourceElements2, targetElements2, List(t2))
        val recipe2 = Recipe.createAllToAllRecipe("RECIPE-02", List(m2))

        val cookbook = Cookbook("MY-COOKBOOK", sources, target, List(recipe1, recipe2))

        val expectedSourceIds = (sourceElements ::: sourceElements2).toSet

        cookbook.usedSourceIds must be(expectedSourceIds)
      }
    }
  }
}
