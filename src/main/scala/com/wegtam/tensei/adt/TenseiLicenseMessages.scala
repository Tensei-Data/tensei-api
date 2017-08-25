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

import java.time.Period

import scalaz._

/**
  * Messages related to the handling of tensei licenses.
  */
sealed trait TenseiLicenseMessages

/**
  * Container object for the license messages to keep the namespace clean.
  */
object TenseiLicenseMessages {

  /**
    * Reports the allowed number of agents.
    *
    * @param count The allowed number of agents.
    */
  final case class AllowedNumberOfAgents(count: Int) extends TenseiLicenseMessages

  /**
    * Reports the allowed number of transformation configurations.
    *
    * @param count The allowed number of transformation configurations.
    */
  final case class AllowedNumberOfConfigurations(count: Int) extends TenseiLicenseMessages

  /**
    * Reports the allowed number of cronjobs.
    *
    * @param count The allowed number of cronjobs.
    */
  final case class AllowedNumberOfCronjobs(count: Int) extends TenseiLicenseMessages

  /**
    * Reports the allowed number of triggers.
    *
    * @param count The allowed number of triggers.
    */
  final case class AllowedNumberOfTriggers(count: Int) extends TenseiLicenseMessages

  /**
    * Reports the allowed number of users.
    *
    * @param count The allowed number of users.
    */
  final case class AllowedNumberOfUsers(count: Int) extends TenseiLicenseMessages

  /**
    * Reports the period in which the license will expire.
    *
    * @param period A period holding the amount of time in which the license will expire.
    */
  final case class LicenseExpiresIn(period: Period) extends TenseiLicenseMessages

  /**
    * Reports the license meta data.
    *
    * @param id       The unique id of the license.
    * @param licensee The name of the licensee (usually a company name).
    * @param period   A period holding the amount of time in which the license will expire.
    */
  final case class LicenseMetaData(id: String, licensee: String, period: Period)
      extends TenseiLicenseMessages

  /**
    * Reports the entities information of the installed license.
    *
    * @param agents          Number of agents.
    * @param configurations  Number of configurations.
    * @param users           Number of users.
    * @param cronjobs        Number of cronjobs.
    * @param trigger         Number of trigger.
    */
  final case class LicenseEntitiesData(
      agents: Int,
      configurations: Int,
      users: Int,
      cronjobs: Int,
      trigger: Int
  ) extends TenseiLicenseMessages

  /**
    * This message indicates that there is no license installed on the server.
    */
  case object NoLicenseInstalled extends TenseiLicenseMessages

  /**
    * Tell the server to report back the allowed number of agents.
    */
  case object ReportAllowedNumberOfAgents extends TenseiLicenseMessages

  /**
    * Tell the server to report back the allowed number of transformation configurations.
    */
  case object ReportAllowedNumberOfConfigurations extends TenseiLicenseMessages

  /**
    * Tell the server to report back the allowed number of cronjobs.
    */
  case object ReportAllowedNumberOfCronjobs extends TenseiLicenseMessages

  /**
    * Tell the server to report back the allowed number of triggers.
    */
  case object ReportAllowedNumberOfTriggers extends TenseiLicenseMessages

  /**
    * Tell the server to report back the allowed number of users.
    */
  case object ReportAllowedNumberOfUsers extends TenseiLicenseMessages

  /**
    * Tell the server to report back the expiration period of the license.
    */
  case object ReportLicenseExpirationPeriod extends TenseiLicenseMessages

  /**
    * Tell the server to report back the license meta data.
    */
  case object ReportLicenseMetaData extends TenseiLicenseMessages

  /**
    * Tell the server to report back the entity information of the license.
    * Entities: Number of agents, configurations, users, cronjobs, trigger
    */
  case object ReportLicenseEntitiesData extends TenseiLicenseMessages

  /**
    * Update the currently used license with the one wrapped into this message.
    *
    * @param encodedLicense A string holding the encoded license.
    */
  final case class UpdateLicense(encodedLicense: String) extends TenseiLicenseMessages

  /**
    * Reports the result of a `UpdateLicense` action.
    *
    * @param message Holds a message that indicates either success of failure.
    */
  final case class UpdateLicenseResult(message: String \/ String) extends TenseiLicenseMessages

  /**
    * Validate the given encoded license.
    *
    * @param encodedLicense A string holding the encoded license.
    */
  final case class ValidateLicense(encodedLicense: String) extends TenseiLicenseMessages

  /**
    * Reports the result of a `ValidateLicense` action.
    *
    * @param result The details of the validation.
    */
  final case class ValidateLicenseResult(result: LicenseValidationResult)
      extends TenseiLicenseMessages

}
