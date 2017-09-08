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

import java.nio.charset.Charset

import com.wegtam.tensei.DefaultSpec
import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks

import scala.collection.JavaConverters._

class ExtractSchemaOptionsTest extends DefaultSpec with PropertyChecks {

  private val charsets = Charset.availableCharsets().keySet().asScala.toVector :+ ""
  private val csvOptions = for {
    header    <- Gen.oneOf(false, true)
    separator <- Gen.alphaNumStr
    encoding  <- Gen.oneOf(charsets)
  } yield (header, separator, encoding)

  describe("#createCsvOptions") {
    it("must create proper options") {
      forAll(csvOptions) { o =>
        val (h, s, e) = o
        val expectedOptions = ExtractSchemaOptions(
          csvHeader = h,
          csvSeparator = if (s.isEmpty) None else Option(s),
          encoding = if (e.isEmpty) None else Option(e)
        )
        ExtractSchemaOptions.createCsvOptions(h, s, e) mustEqual expectedOptions
      }
    }
  }

  describe("#createDatabaseOptions") {
    it("must create proper options") {
      val expectedOptions =
        ExtractSchemaOptions(csvHeader = false, csvSeparator = None, encoding = None)
      ExtractSchemaOptions.createDatabaseOptions() mustEqual expectedOptions
    }
  }

}
