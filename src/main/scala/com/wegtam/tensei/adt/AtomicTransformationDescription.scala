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
  * A container for an atomic transformation that is applied for a specified source element.
  *
  * @param element The reference to the source data element.
  * @param transformerClassName The name of the class that implements the transformer.
  * @param options The options for the transformer.
  */
final case class AtomicTransformationDescription(
    element: ElementReference,
    transformerClassName: String,
    options: TransformerOptions
)

object AtomicTransformationDescription {

  implicit def AtomicTransformationDescriptionCodecJson
    : CodecJson[AtomicTransformationDescription] =
    CodecJson(
      (t: AtomicTransformationDescription) ⇒
        ("options" := t.options) ->:
          ("transformerClassName" := t.transformerClassName.toString) ->:
          ("element" := t.element) ->:
        jEmptyObject,
      c ⇒
        for {
          element              ← (c --\ "element").as[ElementReference]
          transformerClassName ← (c --\ "transformerClassName").as[String]
          options              ← (c --\ "options").as[TransformerOptions]
        } yield AtomicTransformationDescription(element, transformerClassName, options)
    )

}
