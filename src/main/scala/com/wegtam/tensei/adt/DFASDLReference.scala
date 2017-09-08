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
  * A reference for a specific dfasdl within a specific cookbook.
  *
  * @param cookbookId The ID of the cookbook that contains the DFASDL.
  * @param dfasdlId   The ID of the DFASDL.
  */
@SuppressWarnings(Array("org.wartremover.warts.Null"))
final case class DFASDLReference(cookbookId: String, dfasdlId: String) {

  require(cookbookId != null, "The cookbook ID must not be null!")
  require(cookbookId.length > 0, "The cookbook ID must not be empty!")
  require(dfasdlId != null, "The DFASDL ID must not be null!")
  require(dfasdlId.length > 0, "The DFASDL ID must not be empty!")

}

object DFASDLReference {

  implicit def DFASDLReferenceCodecJson: CodecJson[DFASDLReference] =
    CodecJson(
      (r: DFASDLReference) =>
        ("dfasdl-id" := r.dfasdlId) ->: ("cookbook-id" := r.cookbookId) ->: jEmptyObject,
      cursor =>
        for {
          cid <- (cursor --\ "cookbook-id").as[String]
          did <- (cursor --\ "dfasdl-id").as[String]
        } yield DFASDLReference(cid, did)
    )

}
