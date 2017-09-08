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

import java.math.BigInteger
import java.security.interfaces.{ RSAPrivateKey, RSAPublicKey }
import java.security.spec.{ RSAPrivateKeySpec, RSAPublicKeySpec }
import java.security._
import java.util.Base64
import javax.crypto.spec.{ IvParameterSpec, SecretKeySpec }
import javax.crypto.{ Cipher, KeyGenerator, NoSuchPaddingException, SecretKey }

import scalaz._
import Scalaz._

/**
  * This trait provides several helper functions for cryptography related tasks.
  */
trait CryptoHelpers {
  // The default key length for AES keys.
  val DEFAULT_AES_KEY_LENGTH = 128
  // The default variant of the AES cipher.
  val DEFAULT_AES_VARIANT = "AES/CBC/PKCS5PADDING"
  // The default key length for RSA keys.
  val DEFAULT_RSA_KEY_LENGTH = 2048
  // The default variant of the RSA cipher.
  val DEFAULT_RSA_VARIANT = "RSA/ECB/PKCS1Padding"
  // The algorithm that is used to generate signatures.
  val SIGNATURE_ALGORITHM = "SHA512withRSA"

  /**
    * Return a secure random number generator.
    *
    * @return A seeded secure random number generator.
    */
  protected def getRandomNumberGenerator: SecureRandom =
    CryptoHelpers.randomNumberGenerator

  /**
    * Decrypt the given data using the specified cipher and key.
    *
    * @param source  The data that should be decrypted.
    * @param cipher  The cipher that should be used.
    * @param key     The key that should be used.
    * @return Either an array of bytes holding the decrypted data or an exception.
    */
  def decrypt(source: Array[Byte], cipher: Cipher, key: Key): Throwable \/ Array[Byte] =
    decrypt(source, cipher, key, None)

  /**
    * Decrypt the given data using the specified cipher, key and optional init vector.
    *
    * @param source  The data that should be decrypted.
    * @param cipher  The cipher that should be used.
    * @param key     The key that should be used.
    * @param iv      An optional init vector which is needed if you want to decrypt AES encrypted data.
    * @return Either an array of bytes holding the decrypted data or an exception.
    */
  def decrypt(source: Array[Byte],
              cipher: Cipher,
              key: Key,
              iv: Option[Array[Byte]]): Throwable \/ Array[Byte] =
    try {
      iv.fold(cipher.init(Cipher.DECRYPT_MODE, key))(
        i => cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(i))
      )
      cipher.doFinal(source).right
    } catch {
      case e: Throwable => e.left
    }

  /**
    * Encrypt the given source (an array of bytes) using the provided cipher and key.
    * The encrypted data is returned in base 64 format.
    *
    * @param source  The data to encrypt.
    * @param cipher  The cipher that should be used.
    * @param key     The key that should be used to encrypt the data.
    * @return Either an array holding the base 64 encoded encrypted message or an exception.
    */
  def encrypt(source: Array[Byte], cipher: Cipher, key: Key): Throwable \/ Array[Byte] =
    encrypt(source, cipher, key, None)

  /**
    * Encrypt the given source (an array of bytes) using the provided cipher and key.
    * The encrypted data is returned in base 64 format.
    *
    * @param source  The data to encrypt.
    * @param cipher  The cipher that should be used.
    * @param key     The key that should be used to encrypt the data.
    * @param iv      An optional init vector which is needed if you want to decrypt AES encrypted data.
    * @return Either an array holding the base 64 encoded encrypted message or an exception.
    */
  def encrypt(source: Array[Byte],
              cipher: Cipher,
              key: Key,
              iv: Option[Array[Byte]] = None): Throwable \/ Array[Byte] =
    try {
      iv.fold(cipher.init(Cipher.ENCRYPT_MODE, key))(
        i => cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(i))
      )
      val encryptedSource = cipher.doFinal(source)
      Base64.getEncoder.encode(encryptedSource).right
    } catch {
      case e: Throwable => e.left
    }

  /**
    * Generate an AES key and an init vector using `DEFAULT_AES_KEY_LENGTH`.
    *
    * @return A tuple holding the generated key and the init vector.
    */
  def generateAESKeyAndIV(): (SecretKey, Array[Byte]) =
    generateAESKeyAndIV(DEFAULT_AES_KEY_LENGTH)

  /**
    * Generate an AES key and an init vector.
    *
    * @param keySize The desired key size.
    * @return A tuple holding the generated key and the init vector.
    */
  def generateAESKeyAndIV(keySize: Int): (SecretKey, Array[Byte]) = {
    val keyGenerator = KeyGenerator.getInstance("AES")
    keyGenerator.init(keySize)
    val secretKey = keyGenerator.generateKey()

    val secureRandom = getRandomNumberGenerator
    val initVector   = new Array[Byte](keySize / 8) // The IV must have the same number of bits as the key.
    secureRandom.nextBytes(initVector)

    (secretKey, initVector)
  }

  /**
    * Regenerate an AES key from the given bytes.
    *
    * @param keyBytes The key's bytes (produced via `getEncoded`)
    * @return The generated secret key.
    */
  def generateAESKeyFromParameters(keyBytes: Array[Byte]): SecretKey =
    new SecretKeySpec(keyBytes, "AES")

  /**
    * Generate a RSA key pair using `DEFAULT_RSA_KEY_LENGTH`.
    *
    * @return A RSA key pair.
    */
  def generateRSAKeyPair(): KeyPair = generateRSAKeyPair(DEFAULT_RSA_KEY_LENGTH)

  /**
    * Generate a RSA key pair using the specified key size.
    *
    * @param keySize The desired key size.
    * @return A RSA key pair.
    */
  def generateRSAKeyPair(keySize: Int): KeyPair = {
    val keyGenerator = KeyPairGenerator.getInstance("RSA")
    val secureRandom = getRandomNumberGenerator
    keyGenerator.initialize(keySize, secureRandom)

    keyGenerator.generateKeyPair()
  }

  /**
    * Generate a RSA key using the specified parameters.
    *
    * @param modulus   The modulus for the key.
    * @param exponent  The exponend for the key.
    * @param keyType   The type of the key that should be returned. Must be either `classOf[RSAPrivateKey]` or `classOf[RSAPublicKey]`.
    * @return The generated key of the desired type.
    */
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def generateRSAKeyFromParameters(modulus: Array[Byte],
                                   exponent: Array[Byte],
                                   keyType: Class[_]): Key = {
    // We need to create stable classifiers for the pattern matching below.
    val PRIVATE = classOf[RSAPrivateKey]
    val PUBLIC  = classOf[RSAPublicKey]

    val keyFactory = KeyFactory.getInstance("RSA")
    val mod        = new BigInteger(modulus)
    val exp        = new BigInteger(exponent)

    keyType match {
      case PRIVATE =>
        val keySpec = new RSAPrivateKeySpec(mod, exp)
        keyFactory.generatePrivate(keySpec)
      case PUBLIC =>
        val keySpec = new RSAPublicKeySpec(mod, exp)
        keyFactory.generatePublic(keySpec)
      case _ => throw new IllegalArgumentException(s"Unknown RSA key type: $keyType!")
    }
  }

  /**
    * Get an AES cipher that can be used for encryption or decryption of data.
    *
    * @return An AES cipher.
    */
  @throws[NoSuchAlgorithmException]
  @throws[NoSuchPaddingException]
  def getAESCipher: Cipher = Cipher.getInstance(DEFAULT_AES_VARIANT)

  /**
    * Get a RSA cipher that can be used for encryption or decryption of data.
    *
    * @return A RSA cipher.
    */
  @throws[NoSuchAlgorithmException]
  @throws[NoSuchPaddingException]
  def getRSACipher: Cipher = Cipher.getInstance(DEFAULT_RSA_VARIANT)

  /**
    * Generate a signature for the source data using `SIGNATURE_ALGORITHM`.
    * The given private key should be feasable for the signature algorithm!
    *
    * @param source The data to be signed.
    * @param key    A private key.
    * @return Either the base 64 encoded signature or an exception.
    */
  def sign(source: Array[Byte], key: PrivateKey): Throwable \/ Array[Byte] =
    try {
      val signatureProvider = Signature.getInstance(SIGNATURE_ALGORITHM)
      signatureProvider.initSign(key, getRandomNumberGenerator)
      signatureProvider.update(source)
      val signature = signatureProvider.sign()
      Base64.getEncoder.encode(signature).right
    } catch {
      case e: Throwable => e.left
    }

  /**
    * Validate the given signature.
    * This method may produce exceptions.
    *
    * @param message   The message that should match the signature.
    * @param signature The signature.
    * @param key       The key that is used to verify the signature.
    * @return Either `true` if the signature is valid or `false` otherwise.
    * @todo Fix the scaladoc configuration somehow to avoid the `Could not find any member to link for "..."` exception!
    */
  @throws[InvalidKeyException]("The provided key is invalid!")
  @throws[NoSuchAlgorithmException](
    "No Provider supports a Signature implementation for the specified algorithm!"
  )
  @throws[SignatureException](
    "The signature object is not initialized properly, the passed-in signature is improperly encoded or of the wrong type, the signature algorithm is unable to process the input data provided, etc."
  )
  def validate(message: Array[Byte], signature: Array[Byte], key: PublicKey): Boolean = {
    val signatureProvider = Signature.getInstance(SIGNATURE_ALGORITHM)
    signatureProvider.initVerify(key)
    signatureProvider.update(message)
    signatureProvider.verify(signature)
  }
}

object CryptoHelpers {

  // A secure random number generator according to NIST recommendations.
  private final val randomNumberGenerator: SecureRandom = {
    val secureRandom = new SecureRandom() // Prefer this because SecureRandom.getInstanceStrong blocks a lot on systems with low entropy.
    secureRandom.setSeed(secureRandom.generateSeed(55)) // NIST SP800-90A recommends a seed length of 440 bits (i.e. 55 bytes)
    secureRandom
  }

}
