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
  * A reference to a specific DFASDL element.
  * This is needed to reference elements with the same id accross multiple
  * DFASDL documents.
  *
  * @param dfasdlId The ID of the DFASDL that includes the element.
  * @param elementId The ID of the element within the DFASDL.
  */
@SuppressWarnings(Array("org.wartremover.warts.Null"))
final case class ElementReference(dfasdlId: String, elementId: String) {

  require(dfasdlId != null, "The DFASDL ID must not be null!")
  require(dfasdlId.length > 0, "The DFASDL ID must not be empty!")
  require(elementId != null, "The element ID must not be null!")
  require(elementId.length > 0, "The element ID must not be empty!")

}

object ElementReference {

  implicit def ElementReferenceCodecJson: CodecJson[ElementReference] =
    CodecJson(
      (r: ElementReference) ⇒
        ("dfasdlId" := r.dfasdlId) ->: ("elementId" := r.elementId) ->: jEmptyObject,
      c ⇒
        for {
          did ← (c --\ "dfasdlId").as[String]
          eid ← (c --\ "elementId").as[String]
        } yield ElementReference(dfasdlId = did, elementId = eid)
    )

}
