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

class RuntimeStats$Test extends DefaultSpec {
  describe("RuntimeStats") {
    describe("CodecJson") {
      describe("decode") {
        it("must decode a proper json string") {
          val expected =
            RuntimeStats(freeMemory = 61096024, maxMemory = 239075328, totalMemory = 102760448)
          Parse
            .decodeOption[RuntimeStats](
              """{"processors":1,"load":null,"total": 102760448, "max": 239075328, "free": 61096024}"""
            )
            .get must be(expected)
          val expectedOSData = RuntimeStats(freeMemory = 61096024,
                                            maxMemory = 239075328,
                                            totalMemory = 102760448,
                                            processors = 4,
                                            systemLoad = Option(2.6d))
          Parse
            .decodeOption[RuntimeStats](
              """{"processors":4,"load":2.6,"total": 102760448, "max": 239075328, "free": 61096024}"""
            )
            .get must be(expectedOSData)
        }
      }

      describe("encode") {
        it("must encode to a proper json string") {
          val stats =
            RuntimeStats(freeMemory = 61096024, maxMemory = 239075328, totalMemory = 102760448)
          stats.asJson.nospaces must be(
            """{"processors":1,"load":null,"total":102760448,"max":239075328,"free":61096024}"""
          )
          val statsWithOSData = RuntimeStats(freeMemory = 61096024,
                                             maxMemory = 239075328,
                                             totalMemory = 102760448,
                                             processors = 12,
                                             systemLoad = Option(8.9d))
          statsWithOSData.asJson.nospaces must be(
            """{"processors":12,"load":8.9,"total":102760448,"max":239075328,"free":61096024}"""
          )
        }
      }
    }
  }
}
