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

import java.io.{ByteArrayOutputStream, ObjectOutputStream}
import java.net.URI

import com.wegtam.tensei.adt._
import com.wegtam.tensei.remote.adt.StartTransformation
import org.openjdk.jmh.annotations._

@State(Scope.Benchmark)
@Fork(3)
@Warmup(iterations = 4)
@Measurement(iterations = 10)
@BenchmarkMode(Array(Mode.Throughput))
class MessageSerializerBenchmark {

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

  private final val protobufStartTransformationMessage = StartTransformation(
    id = scala.util.Random.alphanumeric.take(32).mkString,
    cookbook = Option(com.wegtam.tensei.remote.adt.Cookbook(
      id = "COOKBOOK",
      sources = List(
        com.wegtam.tensei.remote.adt.DFASDL(
          id = "DFASDL-1",
          content = dfasdl
        )
      ),
      target = Option(com.wegtam.tensei.remote.adt.DFASDL(
        id = "DFASDL-2",
        content = dfasdl
      )),
      recipes = List(
        com.wegtam.tensei.remote.adt.Recipe(
          id = "ID1",
          mode = com.wegtam.tensei.remote.adt.RecipeMode.RecipeMapOneToOne,
          mappings = List(
            com.wegtam.tensei.remote.adt.MappingTransformation(
              sources = List(
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "id"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "name"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "description"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "birthday"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "salary")
              ),
              targets = List(
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "id"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "name"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "description"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "birthday"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "salary")
              ),
              transformations = List()
            )
          )
        ),
        com.wegtam.tensei.remote.adt.Recipe(
          id = "ID2",
          mode = com.wegtam.tensei.remote.adt.RecipeMode.RecipeMapOneToOne,
          mappings = List(
            com.wegtam.tensei.remote.adt.MappingTransformation(
              sources = List(
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "id2"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "name2"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "description2"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "birthday2"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "salary2")
              ),
              targets = List(
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "id2"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "name2"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "description2"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "birthday2"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "salary2")
              ),
              transformations = List()
            )
          )
        ),
        com.wegtam.tensei.remote.adt.Recipe(
          id = "ID3",
          mode = com.wegtam.tensei.remote.adt.RecipeMode.RecipeMapOneToOne,
          mappings = List(
            com.wegtam.tensei.remote.adt.MappingTransformation(
              sources = List(
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "id3"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "name3"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "description3"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "birthday3"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-1", "salary3")
              ),
              targets = List(
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "id3"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "name3"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "description3"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "birthday3"),
                com.wegtam.tensei.remote.adt.ElementReference("DFASDL-2", "salary3")
              ),
              transformations = List()
            )
          )
        )
      )
    )),
    sources = List(
      com.wegtam.tensei.remote.adt.ConnectionInformation(
        uri = "http://example.com/",
        dfasdlRef = Option(com.wegtam.tensei.remote.adt.DFASDLReference(
          cookbookId = "COOKBOOK",
          dfasdlId = "DFASDL-1"
        ))
      )
    ),
    target = Option(com.wegtam.tensei.remote.adt.ConnectionInformation(
      uri = "http://target.example.com/",
      dfasdlRef = Option(com.wegtam.tensei.remote.adt.DFASDLReference(
        cookbookId = "COOKBOOK",
        dfasdlId = "DFASDL-2"
      ))
    ))
  )

  private final val startTransformationMessage = AgentStartTransformationMessage(
    sources = List(
      ConnectionInformation(
        uri = new URI("http://example.com/"),
        dfasdlRef = Option(DFASDLReference(
          cookbookId = "COOKBOOK",
          dfasdlId = "DFASDL-1"
        )),
        username = None,
        password = None,
        checksum = None,
        languageTag = None
      )
    ),
    target = ConnectionInformation(
      uri = new URI("http://target.example.com/"),
      dfasdlRef = Option(DFASDLReference(
        cookbookId = "COOKBOOK",
        dfasdlId = "DFASDL-2"
      ))
    ),
    cookbook = Cookbook(
      id = "COOKBOOK",
      sources = List(DFASDL(id = "DFASDL-1", content = dfasdl)),
      target = Option(DFASDL(id = "DFASDL-2", content = dfasdl)),
      recipes = List(
              Recipe.createOneToOneRecipe("ID1", List(
                MappingTransformation(
                  List(
                    ElementReference("DFASDL-1", "id"),
                    ElementReference("DFASDL-1", "name"),
                    ElementReference("DFASDL-1", "description"),
                    ElementReference("DFASDL-1", "birthday"),
                    ElementReference("DFASDL-1", "salary")
                  ),
                  List(
                    ElementReference("DFASDL-2", "id"),
                    ElementReference("DFASDL-2", "name"),
                    ElementReference("DFASDL-2", "description"),
                    ElementReference("DFASDL-2", "birthday"),
                    ElementReference("DFASDL-2", "salary")
                  ),
                  List()
                )
              )),
              Recipe.createOneToOneRecipe("ID2", List(
                MappingTransformation(
                  List(
                    ElementReference("DFASDL-1", "id2"),
                    ElementReference("DFASDL-1", "name2"),
                    ElementReference("DFASDL-1", "description2"),
                    ElementReference("DFASDL-1", "birthday2"),
                    ElementReference("DFASDL-1", "salary2")
                  ),
                  List(
                    ElementReference("DFASDL-2", "id2"),
                    ElementReference("DFASDL-2", "name2"),
                    ElementReference("DFASDL-2", "description2"),
                    ElementReference("DFASDL-2", "birthday2"),
                    ElementReference("DFASDL-2", "salary2")
                  ),
                  List()
                )
              )),
              Recipe.createOneToOneRecipe("ID3", List(
                MappingTransformation(
                  List(
                    ElementReference("DFASDL-1", "id3"),
                    ElementReference("DFASDL-1", "name3"),
                    ElementReference("DFASDL-1", "description3"),
                    ElementReference("DFASDL-1", "birthday3"),
                    ElementReference("DFASDL-1", "salary3")
                  ),
                  List(
                    ElementReference("DFASDL-2", "id3"),
                    ElementReference("DFASDL-2", "name3"),
                    ElementReference("DFASDL-2", "description3"),
                    ElementReference("DFASDL-2", "birthday3"),
                    ElementReference("DFASDL-2", "salary3")
                  ),
                  List()
                )
              ))
            )
    ),
    uniqueIdentifier = Option(scala.util.Random.alphanumeric.take(32).mkString)
  )
  private final val dfasdlObject = DFASDL(
    id = "MY-DFASDL-ID",
    content = dfasdl
  )
  private final val protoBufDfasdlObject = com.wegtam.tensei.remote.adt.DFASDL(
    id = "MY-DFASDL-ID",
    content = dfasdl
  )

  @Benchmark
  def writeStartTransformationMessage: Array[Byte] = {
    val bs = new ByteArrayOutputStream()
    val os = new ObjectOutputStream(bs)
    os.writeObject(startTransformationMessage)
    os.close()
    bs.toByteArray
  }

  @Benchmark
  def writeStartTransformationMessageProtoBuf: Array[Byte] = {
    protobufStartTransformationMessage.toByteArray
  }

  @Benchmark
  def writeDfasdlObject: Array[Byte] = {
    val bs = new ByteArrayOutputStream()
    val os = new ObjectOutputStream(bs)
    os.writeObject(dfasdlObject)
    os.close()
    bs.toByteArray
  }

  @Benchmark
  def writeDfasdlObjectProtoBuf: Array[Byte] = {
    protoBufDfasdlObject.toByteArray
  }

}
