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

import java.time.{ LocalDate, Period }

/**
  * This class describes the content of a tensei license file.
  *
  * @param id              The unique id of the license.
  * @param licensee        The name of the licensee (usually a company name).
  * @param agents          The number of agents that the licensee is allowed to use.
  * @param users           The number of users that the licensee is allowed to create and use in the frontend. A value of `Int.MaxValue` indicates unlimited.
  * @param configurations  The number of transformation configurations that the licensee is allowed to create and use in the frontend. A value of `Int.MaxValue` indicates unlimited.
  * @param cronjobs        The number of cronjobs that the licensee is allowed to create and use in the frontend. A value of `Int.MaxValue` indicates unlimited.
  * @param triggers        The number of triggers that the licensee is allowed to create and use in the frontend. A value of `Int.MaxValue` indicates unlimited.
  * @param expirationDate  The expiration date of the license.
  */
final case class TenseiLicense(
    id: String,
    licensee: String,
    agents: Int,
    users: Int,
    configurations: Int,
    cronjobs: Int,
    triggers: Int,
    expirationDate: LocalDate
) {

  /**
    * Returns a `java.time.Period` that indicates when this license will expire.
    *
    * @return The period until this license will expire.
    */
  def expiresIn: Period =
    Period.between(LocalDate.now(), expirationDate)

}

/**
  * The companion object holds the implicit json codecs for argonaut and several helper functions.
  */
object TenseiLicense {

  /**
    * A helper codec for decoding and encoding a `java.time.LocalDate`.
    *
    * @return The implicit definition for argonaut how to de- and encode a local date.
    */
  implicit def LocalDateCodecJson: CodecJson[LocalDate] =
    CodecJson(
      (d: LocalDate) ⇒ jString(d.toString),
      cursor ⇒
        for {
          dateString ← cursor.as[String]
        } yield LocalDate.parse(dateString)
    )

  /**
    * The argonaut codec for decoding and encoding a tensei license.
    *
    * @return The implicit definition for argonaut.
    */
  implicit def TenseiLicenseCodecJson: CodecJson[TenseiLicense] =
    CodecJson(
      (l: TenseiLicense) ⇒
        ("playload" := generatePayload())
          ->: ("expiration-date" := l.expirationDate)
          ->: ("triggers" := l.triggers)
          ->: ("cronjobs" := l.cronjobs)
          ->: ("configurations" := l.configurations)
          ->: ("users" := l.users)
          ->: ("agents" := l.agents)
          ->: ("licensee" := l.licensee)
          ->: ("id" := l.id)
          ->: jEmptyObject,
      cursor ⇒
        for {
          id             ← (cursor --\ "id").as[String]
          licensee       ← (cursor --\ "licensee").as[String]
          agents         ← (cursor --\ "agents").as[Int]
          users          ← (cursor --\ "users").as[Int]
          configurations ← (cursor --\ "configurations").as[Int]
          cronjobs       ← (cursor --\ "cronjobs").as[Int]
          triggers       ← (cursor --\ "triggers").as[Int]
          expirationDate ← (cursor --\ "expiration-date").as[LocalDate]
        } yield
          TenseiLicense(id = id,
                        licensee = licensee,
                        agents = agents,
                        users = users,
                        configurations = configurations,
                        cronjobs = cronjobs,
                        triggers = triggers,
                        expirationDate = expirationDate)
    )

  /**
    * Create a professional license for the given licensee and expiration date.
    * It throws an `IllegalArgumentException` if the given expiration date lies in the past.
    *
    * @param licensee        The name of the licensee (usually a company name).
    * @param expirationDate  The expiration date of the license.
    * @return A tensei professional license.
    */
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def createProfessionalLicense(licensee: String, expirationDate: LocalDate): TenseiLicense = {
    if (expirationDate.compareTo(LocalDate.now()) < 0)
      throw new IllegalArgumentException("Expiration date lies in the past!")

    TenseiLicense(
      id = java.util.UUID.randomUUID().toString,
      licensee = licensee,
      agents = 1,
      users = 1,
      configurations = 3,
      cronjobs = 3,
      triggers = 3,
      expirationDate = expirationDate
    )
  }

  /**
    * Create an enterprise license for the given licensee and expiration date.
    * It throws an `IllegalArgumentException` if the given expiration date lies in the past.
    *
    * @param licensee        The name of the licensee (usually a company name).
    * @param expirationDate  The expiration date of the license.
    * @return A tensei enterprise license.
    */
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def createEnterpriseLicense(licensee: String, expirationDate: LocalDate): TenseiLicense = {
    if (expirationDate.compareTo(LocalDate.now()) < 0)
      throw new IllegalArgumentException("Expiration date lies in the past!")

    TenseiLicense(
      id = java.util.UUID.randomUUID().toString,
      licensee = licensee,
      agents = 5,
      users = Int.MaxValue,
      configurations = Int.MaxValue,
      cronjobs = Int.MaxValue,
      triggers = Int.MaxValue,
      expirationDate = expirationDate
    )
  }

  /**
    * Create a test (or demo) license which is currently equal to a professional
    * license except that is expires within a month.
    *
    * @param licensee The name of the licensee (usually a company name).
    * @return A tensei license for test or demo systems that expires within a month.
    */
  def createTestLicense(licensee: String): TenseiLicense =
    createProfessionalLicense(licensee, LocalDate.now().plusMonths(1L))

  /**
    * Generates the playload field for the license using a predefined payload length.
    *
    * @return A random string with the desired length.
    */
  def generatePayload(): String = generatePayload(4096)

  /**
    * Generates the playload field for the license.
    *
    * @param length The desired length of the payload field.
    * @return A random string with the desired length.
    */
  def generatePayload(length: Int): String = scala.util.Random.alphanumeric.take(length).mkString

}
