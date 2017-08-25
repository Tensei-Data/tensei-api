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

package com.wegtam.tensei

import java.io.{ ByteArrayOutputStream, ObjectOutputStream }

import com.wegtam.tensei.adt.DFASDL
import org.github.jamm.MemoryMeter

object MessageSerializerMemoryUsage {

  private final val dfasdl =
    """
      |<?xml version="1.0" encoding="UTF-8"?>
      |<dfasdl xmlns="http://www.dfasdl.org/DFASDL"
      |        default-encoding="utf-8" semantic="niem">
      |  <seq id="accounts">
      |    <elem id="row">
      |      <num id="id" db-column-name="id"/>
      |      <str id="name" max-length="254" db-column-name="name"/>
      |      <str id="description" db-column-name="description"/>
      |      <date id="birthday" db-column-name="birthday"/>
      |      <formatnum id="salary" format="([-]?\d+[,?\d*?]*?\.\d+)" db-column-name="salary" decimal-separator="."/>
      |    </elem>
      |  </seq>
      |  <seq id="accounts2">
      |    <elem id="row2">
      |      <num id="id2" db-column-name="id"/>
      |      <str id="name2" max-length="254" db-column-name="name"/>
      |      <str id="description2" db-column-name="description"/>
      |      <date id="birthday2" db-column-name="birthday"/>
      |      <formatnum id="salary2" format="([-]?\d+[,?\d*?]*?\.\d+)" db-column-name="salary" decimal-separator="."/>
      |    </elem>
      |  </seq>
      |  <seq id="accounts3">
      |    <elem id="row3">
      |      <num id="id3" db-column-name="id"/>
      |      <str id="name3" max-length="254" db-column-name="name"/>
      |      <str id="description3" db-column-name="description"/>
      |      <date id="birthday3" db-column-name="birthday"/>
      |      <formatnum id="salary3" format="([-]?\d+[,?\d*?]*?\.\d+)" db-column-name="salary" decimal-separator="."/>
      |    </elem>
      |  </seq>
      |</dfasdl>
    """.stripMargin
  private final val dfasdlObject = DFASDL(
    id = "MY-DFASDL-ID",
    content = dfasdl
  )
  private final val protoBufDfasdlObject = com.wegtam.tensei.remote.adt.DFASDL(
    id = "MY-DFASDL-ID",
    content = dfasdl
  )

  def main(args: Array[String]): Unit = {
    val meter = new MemoryMeter()
    val classicBytes: Array[Byte] = {
      val bs = new ByteArrayOutputStream()
      val os = new ObjectOutputStream(bs)
      os.writeObject(dfasdlObject)
      os.close()
      bs.toByteArray
    }
    val protoBufBytes = protoBufDfasdlObject.toByteArray

    val srcSize     = meter.measureDeep(dfasdl)
    val classicSize = meter.measureDeep(classicBytes)
    val protoSize   = meter.measureDeep(protoBufBytes)

    println("DFASDL Sizes:")
    println(s"\tXML-String\t\t: $srcSize bytes")
    println(s"\tClassic Serializer\t: $classicSize bytes")
    println(s"\tProtobuf Serializer\t: $protoSize bytes")
  }

}
