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

import com.wegtam.tensei.DefaultSpec

class StatusMessageTest extends DefaultSpec {
  class TestStatusMessage(reporter: Option[String] = None,
                          message: String,
                          statusType: StatusType = StatusType.MinorError,
                          cause: Option[StatusMessage] = None)
      extends StatusMessage(reporter, message, statusType, cause)

  describe("StatusMessage") {
    describe("EncodeJson") {
      describe("using simple status messages") {
        it("must encode proper json") {
          val e = new TestStatusMessage(message = "I am a status message!")
          e.asJson.nospaces must be(
            """{"reporter":null,"message":"I am a status message!","statusType":"MinorError","cause":null}"""
          )
        }
      }

      describe("using chained status messages") {
        it("must encode proper json") {
          val foo = new TestStatusMessage(message = "I am an status message!",
                                          statusType = StatusType.MajorError)
          val bar = new TestStatusMessage(message = "I am caused by the previous status!",
                                          statusType = StatusType.FatalError,
                                          cause = Option(foo))
          bar.asJson.nospaces must be(
            """{"reporter":null,"message":"I am caused by the previous status!","statusType":"FatalError","cause":{"reporter":null,"message":"I am an status message!","statusType":"MajorError","cause":null}}"""
          )
        }
      }
    }
  }
}
