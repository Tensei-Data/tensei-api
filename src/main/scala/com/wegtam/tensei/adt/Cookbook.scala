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

/**
  * A cookbook contains a list of source dfasdl descriptions, a target dfasdl description
  * and a list of recipes describing the actual mapping of the sources into the target.
  *
  * @param id The cookbook identifier (ID).
  * @param sources A list of DFASDL descriptions.
  * @param target A DFASDL description.
  * @param recipes A list of recipes (see `Recipe`).
  */
@SuppressWarnings(Array("org.wartremover.warts.Null"))
final case class Cookbook(
    id: String,
    sources: List[DFASDL],
    target: Option[DFASDL],
    recipes: List[Recipe]
) {
  require(id != null, "The cookbook ID must not be null!")
  require(id.length > 0, "The cookbook ID must not be empty!")

  /**
    * Returns an option to the DFASDL that is referenced by the given `DFASDLReference`.
    * If the cookbook id doesn't match then `None` is returned.
    *
    * @param ref A reference to a DFASDL.
    * @return An option to the DFASDL.
    */
  def findDFASDL(ref: DFASDLReference): Option[DFASDL] =
    if (ref.cookbookId == id) {
      // We try the cheap match first.
      if (target.exists(_.id == ref.dfasdlId))
        target
      else
        sources.find(_.id == ref.dfasdlId) // Search through source dfasdls.
    } else
      None // Wrong cookbook.

  /**
    * A set of source elements that are used within mappings.
    */
  lazy val usedSourceIds: Set[ElementReference] =
    recipes.flatMap(recipe ⇒ recipe.mappings.flatMap(mapping ⇒ mapping.sources)).toSet
}

object Cookbook {

  implicit def CookbookCodecJson: CodecJson[Cookbook] =
    CodecJson(
      (c: Cookbook) ⇒
        ("recipes" := c.recipes) ->: ("target" := c.target) ->: ("sources" := c.sources) ->: ("id" := c.id) ->: jEmptyObject,
      cursor ⇒
        for {
          id      ← (cursor --\ "id").as[String]
          sources ← (cursor --\ "sources").as[List[DFASDL]]
          target  ← (cursor --\ "target").as[Option[DFASDL]]
          recipes ← (cursor --\ "recipes").as[List[Recipe]]
        } yield Cookbook(id, sources, target, recipes)
    )

}
