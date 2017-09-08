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
  * Defines a "key field" for an element id. This is necessary if we are mapping elements from
  * multiple data sources together.
  * The source element values are extracted from the sequence rows where the key field values
  * are equal.
  * A key field has to have the same name in all relevant data sources.
  *
  * @param name The name of the field that is used as a key field.
  */
final case class MappingKeyFieldDefinition(name: String)

object MappingKeyFieldDefinition {

  implicit def MappingKeyFieldDefinitionCodecJson: CodecJson[MappingKeyFieldDefinition] =
    CodecJson(
      (m: MappingKeyFieldDefinition) => ("name" := m.name) ->: jEmptyObject,
      cursor =>
        for {
          name <- (cursor --\ "name").as[String]
        } yield MappingKeyFieldDefinition(name)
    )

}
