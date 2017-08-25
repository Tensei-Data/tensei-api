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

import org.openjdk.jmh.annotations._

import scalaz.{-\/, \/, \/-}

@State(Scope.Benchmark)
@Fork(3)
@Warmup(iterations = 4)
@Measurement(iterations = 10)
@BenchmarkMode(Array(Mode.Throughput))
class CryptoHelpersBenchmark extends CryptoHelpers {

  private val (aesKey, aesIV) = generateAESKeyAndIV()
  private val keyPair         = generateRSAKeyPair()
  private val sourceData      = scala.util.Random.alphanumeric.take(4096).mkString.getBytes("UTF-8")
  private val rsaSourceData   = scala.util.Random.alphanumeric.take(200).mkString.getBytes("UTF-8")
  private val signature       = sign(sourceData, keyPair.getPrivate) match {
    case -\/(failure) => throw failure
    case \/-(success) => java.util.Base64.getDecoder.decode(success)
  }

  private val aesData         = encrypt(sourceData, getAESCipher, aesKey, Option(aesIV)) match {
    case -\/(failure) => throw failure
    case \/-(success) => java.util.Base64.getDecoder.decode(success)
  }
  private val rsaData         = encrypt(rsaSourceData, getRSACipher, keyPair.getPublic, None) match {
    case -\/(failure) => throw failure
    case \/-(success) => java.util.Base64.getDecoder.decode(success)
  }

  @Benchmark
  def encrypAESBenchmark: \/[Throwable, Array[Byte]] = {
    encrypt(sourceData, getAESCipher, aesKey, Option(aesIV))
  }

  @Benchmark
  def encrypRSABenchmark: \/[Throwable, Array[Byte]] = {
    encrypt(rsaSourceData, getRSACipher, keyPair.getPublic, None)
  }

  @Benchmark
  def decryptAESBenchmark: \/[Throwable, Array[Byte]] = {
    decrypt(aesData, getAESCipher, aesKey, Option(aesIV))
  }

  @Benchmark
  def decryptRSABenchmark: \/[Throwable, Array[Byte]] = {
    decrypt(rsaData, getRSACipher, keyPair.getPrivate, None)
  }

  @Benchmark
  def signBenchmark: \/[Throwable, Array[Byte]] = {
    sign(sourceData, keyPair.getPrivate)
  }

  @Benchmark
  def validateBenchmark: Boolean = {
    validate(sourceData, signature, keyPair.getPublic)
  }

}
