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
import java.net.URI

/**
  * Abstract data type that holds connection information.
  *
  * @param uri       Unique Resource Identifier describing the data location
  * @param dfasdlRef An option to the `DFASDLReference` that points to the DFASDL for this connection.
  * @param username  Username (optional)
  * @param password  Password (optional)
  * @param checksum An optional SHA256 checksum of the input data
  */
final case class ConnectionInformation(
    uri: URI,
    dfasdlRef: Option[DFASDLReference],
    username: Option[String] = None,
    password: Option[String] = None,
    checksum: Option[String] = None,
    languageTag: Option[String] = None
)

object ConnectionInformation {

  implicit def ConnectionInformationCodecJson: CodecJson[ConnectionInformation] =
    CodecJson(
      (c: ConnectionInformation) ⇒
        ("languageTag" := c.languageTag) ->:
          ("checksum" := c.checksum) ->:
          ("password" := c.password) ->:
          ("username" := c.username) ->:
          ("dfasdlRef" := c.dfasdlRef) ->:
          ("uri" := c.uri.toString) ->:
        jEmptyObject,
      c ⇒
        for {
          uri         ← (c --\ "uri").as[String]
          dfasdlRef   ← (c --\ "dfasdlRef").as[Option[DFASDLReference]]
          username    ← (c --\ "username").as[Option[String]]
          password    ← (c --\ "password").as[Option[String]]
          checksum    ← (c --\ "checksum").as[Option[String]]
          languageTag ← (c --\ "languageTag").as[Option[String]]
        } yield
          ConnectionInformation(new URI(uri), dfasdlRef, username, password, checksum, languageTag)
    )

}
