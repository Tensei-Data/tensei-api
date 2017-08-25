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
import com.wegtam.tensei.adt.Recipe.{ MapAllToAll, RecipeMode }

/**
  * A recipe describes the mapping of the source data tree into the target data tree by
  * mapping transformations.
  *
  * @param id An unique id to identify the recipe.
  * @param mode Define the mapping mode for the recipe.
  * @param mappings A list of mapping transformations.
  */
@SuppressWarnings(Array("org.wartremover.warts.Null"))
final case class Recipe(
    id: String,
    mode: RecipeMode = MapAllToAll,
    mappings: List[MappingTransformation]
) {

  require(id != null, "The ID of a recipe must not be null!")
  require(id.length > 0, "The ID of a recipe must not be empty!")

}

object Recipe {

  implicit def RecipeCodecJson: CodecJson[Recipe] =
    CodecJson(
      (r: Recipe) ⇒
        ("mappings" := r.mappings) ->:
          ("mode" := r.mode) ->:
          ("id" := r.id) ->:
        jEmptyObject,
      c ⇒
        for {
          id       ← (c --\ "id").as[String]
          mode     ← (c --\ "mode").as[RecipeMode]
          mappings ← (c --\ "mappings").as[List[MappingTransformation]]
        } yield Recipe(id, mode, mappings)
    )

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  implicit def RecipeModeCodecJson: CodecJson[RecipeMode] =
    CodecJson(
      (m: RecipeMode) ⇒
        m match {
          case MapAllToAll ⇒ jString("MapAllToAll")
          case MapOneToOne ⇒ jString("MapOneToOne")
      },
      cursor ⇒
        for {
          modeName ← cursor.as[String]
        } yield {
          modeName match {
            case "MapAllToAll" ⇒ MapAllToAll
            case "MapOneToOne" ⇒ MapOneToOne
            case _             ⇒ throw new IllegalArgumentException(s"Unknown recipe mode name $modeName!")
          }
      }
    )

  sealed trait RecipeMode

  case object MapAllToAll extends RecipeMode

  case object MapOneToOne extends RecipeMode

  /**
    * Helper function to create a recipe using the `AllToAll` mapping mode.
    *
    * @param id An unique id to identify the recipe.
    * @param mappings A list of mapping transformations.
    * @return A recipe.
    */
  def createAllToAllRecipe(id: String, mappings: List[MappingTransformation]): Recipe =
    new Recipe(id, MapAllToAll, mappings)

  def createOneToOneRecipe(id: String, mappings: List[MappingTransformation]): Recipe =
    new Recipe(id, MapOneToOne, mappings)

}
