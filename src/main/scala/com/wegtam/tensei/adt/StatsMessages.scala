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

import scalaz._

sealed trait StatsMessages

/**
  * Container object for the stats messages to keep the namespace clean.
  */
object StatsMessages {

  /**
    * A start message for the calculation of statistics regarding the delivered source.
    *
    * @param source      The source specifies the DFASDL that must be used from the cookbook for the analysis.
    * @param cookbook    The cookbook that holds the DFASDL for the analysis.
    * @param sourceIds   A list of IDs that are relevant for the analysis.
    * @param percent     Defines the amount of that data that should be used from the source for the analysis.
    */
  final case class CalculateStatistics(
      source: ConnectionInformation,
      cookbook: Cookbook,
      sourceIds: List[String],
      percent: Int = 100
  ) extends StatsMessages

  /**
    * The results of the calculation regarding the delivered source.
    *
    * @param results     A list of results of the statistical calculation for the single fields.
    * @param source      The source specifies the DFASDL that must be used from the cookbook for the analysis.
    * @param cookbook    The cookbook that holds the DFASDL for the analysis.
    * @param sourceIds   A list of IDs that are relevant for the analysis.
    * @param percent     Defines the amount of that data that should be used from the source for the analysis.
    */
  final case class CalculateStatisticsResult(
      results: String \/ List[StatsResult],
      source: ConnectionInformation,
      cookbook: Cookbook,
      sourceIds: List[String],
      percent: Int = 100
  ) extends StatsMessages
}

sealed trait StatsResult

object StatsResult {

  /**
    * The basic statistical result of a statistical analyzer after the analysis of all rows.
    * Depending on the Analyzer, these basic statistics mean:
    * - NumericAnalyzer: Basic statistics of the numerical value.
    * - StringAnalyzer : Basic statistics of the length of the strings.
    *
    * @param total     The total amount of data that was analyzed by the analyzer including eventual errors.
    * @param quantity  The total amount of data that were considered in the statistical analysis without errors.
    * @param min       The minimum value that occured in the analyzed data.
    * @param max       The maximum value that occured in the analyzed data.
    * @param mean      The mean value of the analyzed data.
    * @param errors    Errors that occured during the analysis of the data.
    */
  final case class BasicStatisticsResult(
      total: Long = 0,
      quantity: Option[Long] = None,
      min: Option[Double] = None,
      max: Option[Double] = None,
      mean: Option[Double] = None,
      errors: Option[StatisticErrors] = None
  ) extends StatsResult

  /**
    * A class for the different errors that can occur during the basic statistical analysis of the data.
    *
    * @param formatErrors     Number of format errors during the casting of the data.
    * @param nullErrors       Number of null pointer exception errors during the casting of the data.
    * @param unexpectedErrors Number of unexpected errors that are not categorisable.
    */
  final case class StatisticErrors(
      formatErrors: Long = 0,
      nullErrors: Long = 0,
      unexpectedErrors: Long = 0
  ) extends StatsResult

  /**
    * A statistical result of a numerical field.
    *
    * @param elementId The ID of the element in the DFASDL.
    * @param basic The basic statistical calculations for a numerical field.
    */
  final case class StatsResultNumeric(elementId: String, basic: BasicStatisticsResult)
      extends StatsResult

  /**
    * A statistical result of a string field.
    *
    * @param elementId The ID of the element in the DFASDL.
    * @param basic     The basic statistical calculations for a string field.
    */
  final case class StatsResultString(elementId: String, basic: BasicStatisticsResult)
      extends StatsResult

}
