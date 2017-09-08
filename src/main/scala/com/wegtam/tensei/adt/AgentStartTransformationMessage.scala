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
  * Message that starts a complete transformation process
  *
  * @param sources           A list of connection informations for the source data.
  * @param target            Connection information for the target data
  * @param cookbook          The cookbook containing the dfasdls and mapping and transformation recipes for the data.
  * @param uniqueIdentifier  An optional Unique Identifier that is used to identify the message.
  */
final case class AgentStartTransformationMessage(
    sources: List[ConnectionInformation],
    target: ConnectionInformation,
    cookbook: Cookbook,
    uniqueIdentifier: Option[String] = None
) {

  /**
    * Checks if at least one of the sources defines a checksum.
    *
    * @return `true` if at least one of the sources defines a checksum and `false` otherwise.
    */
  def hasChecksums: Boolean = sources.exists(c => c.checksum.exists(_.length > 0))
}

object AgentStartTransformationMessage {
  implicit def AgentStartTransformationMessageCodecJson
    : CodecJson[AgentStartTransformationMessage] =
    CodecJson(
      (a: AgentStartTransformationMessage) =>
        ("uniqueIdentifier" := a.uniqueIdentifier) ->:
          ("cookbook" := a.cookbook) ->:
          ("sources" := a.sources) ->:
          ("target" := a.target) ->:
        jEmptyObject,
      c =>
        for {
          sources          <- (c --\ "sources").as[List[ConnectionInformation]]
          target           <- (c --\ "target").as[ConnectionInformation]
          cookbook         <- (c --\ "cookbook").as[Cookbook]
          uniqueIdentifier <- (c --\ "uniqueIdentifier").as[Option[String]]
        } yield AgentStartTransformationMessage(sources, target, cookbook, uniqueIdentifier)
    )

}
