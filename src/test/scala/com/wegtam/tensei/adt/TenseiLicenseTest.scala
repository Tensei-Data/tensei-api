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

import java.time.LocalDate

import scalaz._

import com.wegtam.tensei.DefaultSpec

class TenseiLicenseTest extends DefaultSpec {
  describe("TenseiLicense") {
    describe("createEnterpriseLicense") {
      describe("given valid parameters") {
        it("must create an enterprise license") {
          val licensee       = "TEST-LICENSEE"
          val expirationDate = LocalDate.now().plusYears(1L)

          val expectedLicense = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = 5,
            users = Int.MaxValue,
            configurations = Int.MaxValue,
            cronjobs = Int.MaxValue,
            triggers = Int.MaxValue,
            expirationDate = expirationDate
          )

          val actualLicense = TenseiLicense.createEnterpriseLicense(licensee, expirationDate)

          actualLicense.licensee must be(expectedLicense.licensee)
          actualLicense.agents must be(expectedLicense.agents)
          actualLicense.users must be(expectedLicense.users)
          actualLicense.configurations must be(expectedLicense.configurations)
          actualLicense.cronjobs must be(expectedLicense.cronjobs)
          actualLicense.triggers must be(expectedLicense.triggers)
          actualLicense.expirationDate must be(expectedLicense.expirationDate)
        }
      }

      describe("given the expiration date of today") {
        it("must create an enterprise license") {
          val licensee       = "TEST-LICENSEE"
          val expirationDate = LocalDate.now()

          val expectedLicense = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = 5,
            users = Int.MaxValue,
            configurations = Int.MaxValue,
            cronjobs = Int.MaxValue,
            triggers = Int.MaxValue,
            expirationDate = expirationDate
          )

          val actualLicense = TenseiLicense.createEnterpriseLicense(licensee, expirationDate)

          actualLicense.licensee must be(expectedLicense.licensee)
          actualLicense.agents must be(expectedLicense.agents)
          actualLicense.users must be(expectedLicense.users)
          actualLicense.configurations must be(expectedLicense.configurations)
          actualLicense.cronjobs must be(expectedLicense.cronjobs)
          actualLicense.triggers must be(expectedLicense.triggers)
          actualLicense.expirationDate must be(expectedLicense.expirationDate)
        }
      }

      describe("given an expiration date from the past") {
        it("must throw an IllegalArgumentException") {
          val licensee       = "TEST-LICENSEE"
          val expirationDate = LocalDate.now().minusDays(1L)

          a[IllegalArgumentException] must be thrownBy TenseiLicense.createEnterpriseLicense(
            licensee,
            expirationDate
          )
        }
      }
    }

    describe("createProfessionalLicense") {
      describe("given valid parameters") {
        it("must create a professional license") {
          val licensee       = "TEST-LICENSEE"
          val expirationDate = LocalDate.now().plusYears(1L)

          val expectedLicense = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = 1,
            users = 1,
            configurations = 3,
            cronjobs = 3,
            triggers = 3,
            expirationDate = expirationDate
          )

          val actualLicense = TenseiLicense.createProfessionalLicense(licensee, expirationDate)

          actualLicense.licensee must be(expectedLicense.licensee)
          actualLicense.agents must be(expectedLicense.agents)
          actualLicense.users must be(expectedLicense.users)
          actualLicense.configurations must be(expectedLicense.configurations)
          actualLicense.cronjobs must be(expectedLicense.cronjobs)
          actualLicense.triggers must be(expectedLicense.triggers)
          actualLicense.expirationDate must be(expectedLicense.expirationDate)
        }
      }

      describe("given the expiration date of today") {
        it("must create a professional license") {
          val licensee       = "TEST-LICENSEE"
          val expirationDate = LocalDate.now()

          val expectedLicense = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = 1,
            users = 1,
            configurations = 3,
            cronjobs = 3,
            triggers = 3,
            expirationDate = expirationDate
          )

          val actualLicense = TenseiLicense.createProfessionalLicense(licensee, expirationDate)

          actualLicense.licensee must be(expectedLicense.licensee)
          actualLicense.agents must be(expectedLicense.agents)
          actualLicense.users must be(expectedLicense.users)
          actualLicense.configurations must be(expectedLicense.configurations)
          actualLicense.cronjobs must be(expectedLicense.cronjobs)
          actualLicense.triggers must be(expectedLicense.triggers)
          actualLicense.expirationDate must be(expectedLicense.expirationDate)
        }
      }

      describe("given an expiration date from the past") {
        it("must throw an IllegalArgumentException") {
          val licensee       = "TEST-LICENSEE"
          val expirationDate = LocalDate.now().minusDays(1L)

          a[IllegalArgumentException] must be thrownBy TenseiLicense.createProfessionalLicense(
            licensee,
            expirationDate
          )
        }
      }
    }

    describe("createTestLicense") {
      it("must create a license that expires within a month") {
        val licensee       = "TEST-LICENSEE"
        val expirationDate = LocalDate.now().plusMonths(1L)

        val expectedLicense = TenseiLicense(
          id = java.util.UUID.randomUUID().toString,
          licensee = licensee,
          agents = 1,
          users = 1,
          configurations = 3,
          cronjobs = 3,
          triggers = 3,
          expirationDate = expirationDate
        )

        val actualLicense = TenseiLicense.createTestLicense(licensee)

        actualLicense.licensee must be(expectedLicense.licensee)
        actualLicense.agents must be(expectedLicense.agents)
        actualLicense.users must be(expectedLicense.users)
        actualLicense.configurations must be(expectedLicense.configurations)
        actualLicense.cronjobs must be(expectedLicense.cronjobs)
        actualLicense.triggers must be(expectedLicense.triggers)
        actualLicense.expirationDate must be(expectedLicense.expirationDate)
      }
    }

    describe("expiresIn") {
      describe("when license is not expired") {
        it("must return the period until the license expires") {
          val licensee = "TEST-LICENSEE"

          val oneYearLicense = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = 1,
            users = 1,
            configurations = 3,
            cronjobs = 3,
            triggers = 3,
            expirationDate = LocalDate.now().plusYears(1L)
          )
          oneYearLicense.expiresIn.getYears must be(1)

          val oneMonthLicense = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = 1,
            users = 1,
            configurations = 3,
            cronjobs = 3,
            triggers = 3,
            expirationDate = LocalDate.now().plusMonths(1L)
          )
          oneMonthLicense.expiresIn.getMonths must be(1)

          val oneDayLicense = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = 1,
            users = 1,
            configurations = 3,
            cronjobs = 3,
            triggers = 3,
            expirationDate = LocalDate.now().plusDays(1L)
          )
          oneDayLicense.expiresIn.getDays must be(1)
        }
      }

      describe("when the license expires today") {
        it("must return a period that is zero") {
          val licensee = "TEST-LICENSEE"

          val license = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = 1,
            users = 1,
            configurations = 3,
            cronjobs = 3,
            triggers = 3,
            expirationDate = LocalDate.now()
          )
          license.expiresIn.isZero must be(right = true)
        }
      }

      describe("when the license has expired") {
        it("must return a negative period") {
          val licensee = "TEST-LICENSEE"

          val license = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = 1,
            users = 1,
            configurations = 3,
            cronjobs = 3,
            triggers = 3,
            expirationDate = LocalDate.now().minusDays(1L)
          )
          license.expiresIn.isNegative must be(right = true)
        }
      }
    }

    describe("CodecJson") {
      describe("decode") {
        it("must decode proper json") {
          val licensee = "TEST-LICENSEE"

          val expectedLicense = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = scala.util.Random.nextInt(),
            users = scala.util.Random.nextInt(),
            configurations = scala.util.Random.nextInt(),
            cronjobs = scala.util.Random.nextInt(),
            triggers = scala.util.Random.nextInt(),
            expirationDate = LocalDate.now().plusYears(1L).plusMonths(6L)
          )
          val jsonString =
            s"""
               |{
               |  "cronjobs" : ${expectedLicense.cronjobs},
               |  "licensee" : "$licensee",
               |  "users" : ${expectedLicense.users},
               |  "agents" : ${expectedLicense.agents},
               |  "expiration-date" : "${expectedLicense.expirationDate.toString}",
               |  "triggers" : ${expectedLicense.triggers},
               |  "id" : "${expectedLicense.id}",
               |  "configurations" : ${expectedLicense.configurations}
               |}
             """.stripMargin.trim

          Parse.decodeEither[TenseiLicense](jsonString) match {
            case -\/(error) ⇒ fail(error)
            case \/-(actualLicense) ⇒
              actualLicense.licensee must be(expectedLicense.licensee)
              actualLicense.agents must be(expectedLicense.agents)
              actualLicense.users must be(expectedLicense.users)
              actualLicense.configurations must be(expectedLicense.configurations)
              actualLicense.cronjobs must be(expectedLicense.cronjobs)
              actualLicense.triggers must be(expectedLicense.triggers)
              actualLicense.expirationDate must be(expectedLicense.expirationDate)
          }
        }
      }

      describe("encode") {
        it("must encode to proper json") {
          val licensee = "TEST-LICENSEE"

          val license = TenseiLicense(
            id = java.util.UUID.randomUUID().toString,
            licensee = licensee,
            agents = 1,
            users = 1,
            configurations = 3,
            cronjobs = 3,
            triggers = 3,
            expirationDate = LocalDate.now().plusMonths(6L)
          )
          val expectedParts = List(
            s""""cronjobs" : ${license.cronjobs},""",
            s""""licensee" : "$licensee",""",
            s""""users" : ${license.users},""",
            s""""agents" : ${license.agents},""",
            s""""expiration-date" : "${license.expirationDate.toString}",""",
            s""""triggers" : ${license.triggers},""",
            s""""id" : "${license.id}",""",
            s""""configurations" : ${license.configurations}""",
            s""""playload" : """"
          )

          val actualJson = license.asJson.spaces2
          expectedParts.foreach { part ⇒
            actualJson must include(part)
          }
        }
      }
    }
  }
}
