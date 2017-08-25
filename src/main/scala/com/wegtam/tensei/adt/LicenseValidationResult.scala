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
  * All possible results of a license validation.
  */
sealed trait LicenseValidationResult

/**
  * A companion object to keep the namespace clean.
  */
object LicenseValidationResult {

  /**
    * The license is not valid.
    *
    * @param reason An option to a reason why the license is invalid.
    */
  final case class Invalid(reason: Option[InvalidLicenseReason]) extends LicenseValidationResult

  /**
    * The license is valid.
    */
  case object Valid extends LicenseValidationResult

}

/**
  * All possible reasons for an invalid license.
  */
sealed trait InvalidLicenseReason

/**
  * A companion object to hold the invalid license reasons to keep the namespace clean.
  */
object InvalidLicenseReason {

  /**
    * This indicates that the license data was damaged e.g. not complete and
    * we were unable to process it.
    */
  case object Damaged extends InvalidLicenseReason

  /**
    * The license has expired.
    */
  case object Expired extends InvalidLicenseReason

  /**
    * The signature on the license is invalid.
    */
  case object InvalidSignature extends InvalidLicenseReason

  /**
    * The license was missing a signature.
    */
  case object Unsigned extends InvalidLicenseReason

}
