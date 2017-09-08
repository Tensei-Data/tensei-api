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
  * A mapping transformation describes the mapping of the data hold in elements specified by one or more
  * source elements to elements in the target tree.
  *
  * The simplest possible case is the mapping of one source elemenht to one target element without any transformations.
  *
  * If more than one element is specified in MapAllToAll mode:
  * All given sources are always mapped to all given targets using
  * the specified transformations. This means that you have to define aggregation transformations if you use
  * multiple sources (for example a concatenation transformation for multiple string sources).
  *
  * @param sources A list of source elements.
  * @param targets A list of target elements.
  * @param transformations A list of transformations descriptions.
  * @param atomicTransformations A list of atomic transformation descriptions.
  * @param mappingKey The key field that is used for mapping from different data sources.
  */
final case class MappingTransformation(
    sources: List[ElementReference],
    targets: List[ElementReference],
    transformations: List[TransformationDescription] = List(),
    atomicTransformations: List[AtomicTransformationDescription] = List(),
    mappingKey: Option[MappingKeyFieldDefinition] = None
) {

  require(targets.nonEmpty, "You have to define at least 1 target id!")

}

object MappingTransformation {

  implicit def MappingTransformationCodecJson: CodecJson[MappingTransformation] =
    CodecJson(
      (m: MappingTransformation) =>
        ("mappingKey" := m.mappingKey) ->:
          ("atomicTransformations" := m.atomicTransformations) ->:
          ("transformations" := m.transformations) ->:
          ("targets" := m.targets) ->:
          ("sources" := m.sources) ->:
        jEmptyObject,
      c =>
        for {
          sources         <- (c --\ "sources").as[List[ElementReference]]
          targets         <- (c --\ "targets").as[List[ElementReference]]
          transformations <- (c --\ "transformations").as[List[TransformationDescription]]
          atomicTransformations <- (c --\ "atomicTransformations")
            .as[List[AtomicTransformationDescription]]
          mappingKeys <- (c --\ "mappingKey").as[Option[MappingKeyFieldDefinition]]
        } yield
          MappingTransformation(sources,
                                targets,
                                transformations,
                                atomicTransformations,
                                mappingKeys)
    )

}
