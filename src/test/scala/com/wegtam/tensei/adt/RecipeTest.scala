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
import com.wegtam.tensei.adt.Recipe.MapAllToAll

import scalaz._

class RecipeTest extends DefaultSpec {
  describe("when created without an id") {
    it("must be invalid") {
      an[IllegalArgumentException] must be thrownBy new Recipe(
        "",
        MapAllToAll,
        List(
          new MappingTransformation(List(ElementReference("DFASDL", "source")),
                                    List(ElementReference("DFASDL", "target")))
        )
      )
    }
  }

  describe("JsonCodec") {
    describe("encode") {
      it("must properly encode an object to json") {
        val params = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params)
        val t      = new TransformationDescription("http://www.example.com", o)
        val sources = List(
          ElementReference("DFASDL", "source01"),
          ElementReference("DFASDL", "source02")
        )
        val targets = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m = new MappingTransformation(sources, targets, List(t))
        val r = Recipe.createAllToAllRecipe("my-recipe", List(m))
        val expectedJson =
          """{"id":"my-recipe","mode":"MapAllToAll","mappings":[{"targets":[{"elementId":"target01","dfasdlId":"DFASDL"},{"elementId":"target02","dfasdlId":"DFASDL"},{"elementId":"target03","dfasdlId":"DFASDL"}],"transformations":[{"transformerClassName":"http://www.example.com","options":{"srcType":"java.lang.String","dstType":"java.lang.Long","params":[["one","1"],["two","Zwei"],["three","3.14f"]]}}],"atomicTransformations":[],"mappingKey":null,"sources":[{"elementId":"source01","dfasdlId":"DFASDL"},{"elementId":"source02","dfasdlId":"DFASDL"}]}]}"""
        r.asJson.nospaces mustEqual expectedJson
      }
    }

    describe("decode") {
      it("must properly decode json to an object") {
        val jsonString =
          """{"id":"my-recipe","mode":"MapAllToAll","mappings":[{"sources":[{"elementId":"source01","dfasdlId":"DFASDL"},{"elementId":"source02","dfasdlId":"DFASDL"}],"targets":[{"elementId":"target01","dfasdlId":"DFASDL"},{"elementId":"target02","dfasdlId":"DFASDL"},{"elementId":"target03","dfasdlId":"DFASDL"}],"transformations":[{"transformerClassName":"http://www.example.com","options":{"srcType":"java.lang.String","dstType":"java.lang.Long","params":[["one","1"],["two","Zwei"],["three","3.14f"]]}}],"atomicTransformations":[]}]}"""
        val params = List(("one", "1"), ("two", "Zwei"), ("three", "3.14f"))
        val o      = new TransformerOptions(classOf[String], classOf[java.lang.Long], params)
        val t      = new TransformationDescription("http://www.example.com", o)
        val sources = List(
          ElementReference("DFASDL", "source01"),
          ElementReference("DFASDL", "source02")
        )
        val targets = List(
          ElementReference("DFASDL", "target01"),
          ElementReference("DFASDL", "target02"),
          ElementReference("DFASDL", "target03")
        )
        val m        = new MappingTransformation(sources, targets, List(t))
        val expected = Recipe.createAllToAllRecipe("my-recipe", List(m))
        Parse.decodeEither[Recipe](jsonString) match {
          case -\/(failure) => fail(failure)
          case \/-(success) => success must be(expected)
        }
      }
    }
  }
}
