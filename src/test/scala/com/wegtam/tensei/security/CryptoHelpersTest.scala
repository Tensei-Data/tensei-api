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

package com.wegtam.tensei.security

import argonaut._, Argonaut._

import java.security.KeyFactory
import java.security.interfaces.{ RSAPrivateKey, RSAPublicKey }
import java.security.spec.{ RSAPrivateKeySpec, RSAPublicKeySpec }
import java.time.LocalDate
import javax.crypto.{ Cipher, IllegalBlockSizeException }

import com.wegtam.tensei.DefaultSpec
import com.wegtam.tensei.adt.TenseiLicense

import scalaz._

class CryptoHelpersTest extends DefaultSpec with CryptoHelpers {
  describe("CryptoHelpers") {
    describe("default AES key length") {
      it("must be 128") {
        DEFAULT_AES_KEY_LENGTH must be(128)
      }
    }

    describe("default AES variant") {
      it("must be AES/CBC/PKCS5PADDING") {
        DEFAULT_AES_VARIANT must be("AES/CBC/PKCS5PADDING")
      }
    }

    describe("default RSA key length") {
      it("must be 2048") {
        DEFAULT_RSA_KEY_LENGTH must be(2048)
      }
    }

    describe("default RSA variant") {
      it("must be RSA/ECB/PKCS1Padding") {
        DEFAULT_RSA_VARIANT must be("RSA/ECB/PKCS1Padding")
      }
    }

    describe("the signature algorithm") {
      it("must be SHA256withRSA") {
        SIGNATURE_ALGORITHM must be("SHA512withRSA")
      }
    }

    describe("generateAESKeyAndIV") {
      it("must generate an aes key and an init vector") {
        val response = generateAESKeyAndIV()
        response._1.getAlgorithm must be("AES")
        response._2.length must be(DEFAULT_AES_KEY_LENGTH / 8)
      }
    }

    describe("generateAESKeyFromParameters") {
      it("must generate the correct key") {
        val response  = generateAESKeyAndIV()
        val key       = response._1
        val generated = generateAESKeyFromParameters(key.getEncoded)

        generated.getAlgorithm must be(key.getAlgorithm)
        generated.getEncoded must be(key.getEncoded)
      }
    }

    describe("generateRSAKeyPair") {
      it("must generate a RSA key pair") {
        val keyPair = generateRSAKeyPair(512)
        keyPair.getPrivate.getAlgorithm must be("RSA")
        keyPair.getPublic.getAlgorithm must be("RSA")
      }
    }

    describe("generateRSAKeyFromParameters") {
      describe("if an illegal key type is desired") {
        it("must throw an exception") {
          val keyPair    = generateRSAKeyPair(512)
          val keyFactory = KeyFactory.getInstance("RSA")
          val keySpec    = keyFactory.getKeySpec(keyPair.getPrivate, classOf[RSAPrivateKeySpec])

          an[IllegalArgumentException] must be thrownBy generateRSAKeyFromParameters(
            keySpec.getModulus.toByteArray,
            keySpec.getPrivateExponent.toByteArray,
            classOf[String]
          )
        }
      }

      describe("if a private key is desired") {
        it("must return a private key") {
          val keyPair    = generateRSAKeyPair(512)
          val keyFactory = KeyFactory.getInstance("RSA")
          val keySpec    = keyFactory.getKeySpec(keyPair.getPrivate, classOf[RSAPrivateKeySpec])
          val response = generateRSAKeyFromParameters(keySpec.getModulus.toByteArray,
                                                      keySpec.getPrivateExponent.toByteArray,
                                                      classOf[RSAPrivateKey])
          response.getAlgorithm must be(keyPair.getPrivate.getAlgorithm)
          val responseKeySpec = keyFactory.getKeySpec(response, classOf[RSAPrivateKeySpec])
          responseKeySpec.getModulus.compareTo(keySpec.getModulus) must be(0)
          responseKeySpec.getPrivateExponent.compareTo(keySpec.getPrivateExponent) must be(0)
        }
      }

      describe("if a public key is desired") {
        it("must return a public key") {
          val keyPair    = generateRSAKeyPair(512)
          val keyFactory = KeyFactory.getInstance("RSA")
          val keySpec    = keyFactory.getKeySpec(keyPair.getPublic, classOf[RSAPublicKeySpec])
          val response = generateRSAKeyFromParameters(keySpec.getModulus.toByteArray,
                                                      keySpec.getPublicExponent.toByteArray,
                                                      classOf[RSAPublicKey])
          response.getAlgorithm must be(keyPair.getPublic.getAlgorithm)
          val responseKeySpec = keyFactory.getKeySpec(response, classOf[RSAPublicKeySpec])
          responseKeySpec.getModulus.compareTo(keySpec.getModulus) must be(0)
          responseKeySpec.getPublicExponent.compareTo(keySpec.getPublicExponent) must be(0)
        }
      }
    }

    describe("getAESCipher") {
      it("must return an AES cipher using the default AES variant") {
        getAESCipher.getAlgorithm must be(Cipher.getInstance(DEFAULT_AES_VARIANT).getAlgorithm)
      }
    }

    describe("getRSACipher") {
      it("must return a RSA cipher using the default RSA variant") {
        getRSACipher.getAlgorithm must be(Cipher.getInstance(DEFAULT_RSA_VARIANT).getAlgorithm)
      }
    }

    describe("encrypt") {
      describe(s"using an AES key with $DEFAULT_AES_KEY_LENGTH bits") {
        it("must encrypt the given data") {
          val keyAndIV   = generateAESKeyAndIV()
          val sourceData = "My voice is my passport, verify me!"

          encrypt(sourceData.getBytes, getAESCipher, keyAndIV._1, Option(keyAndIV._2)) match {
            case -\/(error)   => fail(error)
            case \/-(success) => success.length must be > 0
          }
        }

        it("must encrypt a large amount of data") {
          val keyAndIV   = generateAESKeyAndIV()
          val sourceData = scala.util.Random.alphanumeric.take(1024).mkString

          encrypt(sourceData.getBytes, getAESCipher, keyAndIV._1, Option(keyAndIV._2)) match {
            case -\/(error)   => fail(error)
            case \/-(success) => success.length must be > 0
          }
        }
      }

      describe(s"using a RSA key with $DEFAULT_RSA_KEY_LENGTH bits") {
        it("must encrypt the given data") {
          val keyPair    = generateRSAKeyPair()
          val sourceData = "My voice is my passport, verify me!"

          encrypt(sourceData.getBytes, getRSACipher, keyPair.getPublic, None) match {
            case -\/(error)   => fail(error)
            case \/-(success) => success.length must be > 0
          }
        }

        it(s"must not encrypt more than ${DEFAULT_RSA_KEY_LENGTH / 8 - 11} bytes") {
          val keyPair = generateRSAKeyPair()
          val sourceData =
            scala.util.Random.alphanumeric.take(DEFAULT_RSA_KEY_LENGTH / 8 - 10).mkString

          val response = encrypt(sourceData.getBytes, getRSACipher, keyPair.getPublic, None)
          response.swap.getOrElse(new RuntimeException("An unexpected exception")) mustBe a[
            IllegalBlockSizeException
          ]
        }
      }
    }

    describe("decrypt") {
      describe(s"using an AES key with $DEFAULT_AES_KEY_LENGTH bits") {
        it("must decrypt the given data") {
          val keyAndIV   = generateAESKeyAndIV()
          val sourceData = "My voice is my passport, verify me!"

          encrypt(sourceData.getBytes, getAESCipher, keyAndIV._1, Option(keyAndIV._2)) fold (
            error => fail(error),
            encryptedData => {
              val decoded = java.util.Base64.getDecoder.decode(encryptedData)
              decrypt(decoded, getAESCipher, keyAndIV._1, Option(keyAndIV._2)) match {
                case -\/(failure) => fail(failure)
                case \/-(success) => new String(success) must be(sourceData)
              }
            }
          )
        }
      }

      describe(s"using a RSA ky with $DEFAULT_RSA_KEY_LENGTH bits") {
        it("must decrypt the given data") {
          val keyPair    = generateRSAKeyPair()
          val sourceData = "My voice is my passport, verify me!"

          encrypt(sourceData.getBytes, getRSACipher, keyPair.getPublic, None) fold (
            error => fail(error),
            encryptedData => {
              val decoded = java.util.Base64.getDecoder.decode(encryptedData)
              decrypt(decoded, getRSACipher, keyPair.getPrivate, None) match {
                case -\/(failure) => fail(failure)
                case \/-(success) => new String(success) must be(sourceData)
              }
            }
          )
        }
      }
    }

    describe("sign") {
      it("must generate a signature") {
        val keyPair    = generateRSAKeyPair()
        val sourceData = "My voice is my passport, verify me!"

        sign(sourceData.getBytes, keyPair.getPrivate) match {
          case -\/(failure) => fail(failure)
          case \/-(success) =>
            val signature = java.util.Base64.getDecoder.decode(success)
            signature.length must be(256)
            validate(sourceData.getBytes, signature, keyPair.getPublic) must be(right = true)
        }
      }

      it("must generate a signature for large data") {
        val keyPair    = generateRSAKeyPair()
        val sourceData = scala.util.Random.alphanumeric.take(4096).mkString

        sign(sourceData.getBytes, keyPair.getPrivate) match {
          case -\/(failure) => fail(failure)
          case \/-(success) =>
            val signature = java.util.Base64.getDecoder.decode(success)
            signature.length must be(256)
            validate(sourceData.getBytes, signature, keyPair.getPublic) must be(right = true)
        }
      }

      it("must generate a signature for a license") {
        val keyPair = generateRSAKeyPair()
        val license =
          TenseiLicense.createProfessionalLicense("TEST-LICENSE", LocalDate.now().plusYears(1L))
        val licenseString = license.asJson.nospaces

        sign(licenseString.getBytes, keyPair.getPrivate) match {
          case -\/(failure) => fail(failure)
          case \/-(success) =>
            val messageString =
              List(licenseString, new String(success)).mkString("\n----- SIGNATURE -----\n")
            val messageParts = messageString.split("\n----- SIGNATURE -----\n")
            val signature    = java.util.Base64.getDecoder.decode(messageParts(1).getBytes)
            signature.length must be(256)
            validate(messageParts(0).getBytes, signature, keyPair.getPublic) must be(right = true)
        }
      }
    }

    describe("validate") {
      it("must return true for a valid signature") {
        val keyPair    = generateRSAKeyPair()
        val sourceData = "My voice is my passport, verify me!"

        val signature        = sign(sourceData.getBytes, keyPair.getPrivate).getOrElse(Array.empty[Byte])
        val decodedSignature = java.util.Base64.getDecoder.decode(signature)
        decodedSignature.length must be(256)

        withClue("The signature must be valid!")(
          validate(sourceData.getBytes, decodedSignature, keyPair.getPublic) must be(right = true)
        )
      }

      it("must return false for an invalid signature") {
        val keyPair    = generateRSAKeyPair()
        val sourceData = "My voice is my passport, verify me!"
        val signature  = scala.util.Random.alphanumeric.take(256).mkString

        withClue("The signature must not be valid!")(
          validate(sourceData.getBytes, signature.getBytes, keyPair.getPublic) must be(
            right = false
          )
        )
      }
    }
  }
}
