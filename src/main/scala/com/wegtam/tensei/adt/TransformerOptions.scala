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
import scala.language.existentials

/**
  * A case class to wrap around the standard options for a transformer.
  *
  * @param srcType   The data type of the source data
  * @param dstType   The data type of the target data
  * @param params A list of tuples that hold the parameters for the transformation actor.
  */
final case class TransformerOptions(
    srcType: Class[_],
    dstType: Class[_],
    params: List[(String, String)] = List()
) {

  require(TransformationDataType.isValidDataType(srcType), s"Illegal source data type: $srcType")
  require(TransformationDataType.isValidDataType(dstType), s"Illegal target data type: $dstType")

}

object TransformerOptions {

  // Codec for encoding and decoding a whole `TransformerOptions` object.
  implicit def TransformerOptionsCodecJson: CodecJson[TransformerOptions] =
    CodecJson(
      (o: TransformerOptions) ⇒
        ("params" := o.params) ->:
          ("dstType" := jString(o.dstType.getCanonicalName)) ->:
          ("srcType" := jString(o.srcType.getCanonicalName)) ->:
        jEmptyObject,
      c ⇒
        for {
          srcType ← (c --\ "srcType").as[String]
          dstType ← (c --\ "dstType").as[String]
          params  ← (c --\ "params").as[List[(String, String)]]
        } yield TransformerOptions(Class.forName(srcType), Class.forName(dstType), params)
    )

}
