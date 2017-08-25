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

import java.net.URI

import com.wegtam.tensei.DefaultSpec
import com.wegtam.tensei.adt.StatsMessages.CalculateStatisticsResult
import com.wegtam.tensei.adt.StatsResult.{
  BasicStatisticsResult,
  StatsResultNumeric,
  StatsResultString
}

import scalaz._, Scalaz._

class StatsMessagesTest extends DefaultSpec {
  describe("StatsMessages") {
    describe("CalculateStatisticsResult") {
      val dfasdl = DFASDL("SIMPLE-DFASDL", "")
      val refs = List(
        ElementReference("DFASDL", "alter"),
        ElementReference("DFASDL", "name")
      )
      val mapping  = MappingTransformation(refs, refs)
      val recipe   = new Recipe("COPY-COLUMNS", Recipe.MapOneToOne, List(mapping))
      val cookbook = Cookbook("COOKBOOK", List(dfasdl), Option(dfasdl), List(recipe))
      val source =
        ConnectionInformation(new URI(""), Option(DFASDLReference(cookbook.id, dfasdl.id)))

      describe("with numerical and string results") {
        it("must work") {
          val numericResult = new StatsResultNumeric(
            "alter",
            new BasicStatisticsResult(3, Option(3), Option(1.0), Option(5.0), Option(3.0))
          )
          val stringResult = new StatsResultString(
            "name",
            new BasicStatisticsResult(3, Option(3), Option(1.0), Option(5.0), Option(3.0))
          )
          new CalculateStatisticsResult(List(numericResult, stringResult).right[String],
                                        source = source,
                                        cookbook,
                                        List("alter", "name"))
        }
      }
    }
  }
}
