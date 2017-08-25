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

/**
  * Options that can be passed to the schema extractor.
  *
  * @param csvHeader    A flag that indicates if a possible CSV file has a header line.
  * @param csvSeparator The separator character used in a CSV file.
  * @param encoding     The encoding of the file specified as the [[java.nio.charset.Charset]] name.
  * @todo This generic options class will be a dumping ground for all kinds of options. Research a way to clean/split this up more properly.
  */
final case class ExtractSchemaOptions(
    csvHeader: Boolean,
    csvSeparator: Option[String],
    encoding: Option[String]
)

object ExtractSchemaOptions {

  /**
    * Create options for a schema extraction from a CSV file.
    *
    * @param hasHeaderLine Set this to `true` if the first line of the file contains the column headers.
    * @param separator The character that is used to separate columns (`,;\t`).
    * @param encoding The encoding of the file specified as the [[java.nio.charset.Charset]] name.
    * @return Options for the schema extractor.
    */
  def createCsvOptions(hasHeaderLine: Boolean,
                       separator: String,
                       encoding: String): ExtractSchemaOptions = ExtractSchemaOptions(
    csvHeader = hasHeaderLine,
    csvSeparator = if (separator.isEmpty) None else Option(separator),
    encoding = if (encoding.isEmpty) None else Option(encoding)
  )

  /**
    * Create options for a schema extraction from a database.
    *
    * @return Options for the schema extractor.
    */
  def createDatabaseOptions(): ExtractSchemaOptions = ExtractSchemaOptions(
    csvHeader = false,
    csvSeparator = None,
    encoding = None
  )

}
