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

import java.io.{ StringReader, StringWriter, Writer }
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.{ OutputKeys, TransformerFactory }

import argonaut._, Argonaut._
import org.w3c.dom.Element
import org.w3c.dom.traversal.{ DocumentTraversal, NodeFilter }
import org.xml.sax.InputSource

import scala.collection.mutable.ListBuffer

/**
  * A wrapper class for DFASDL descriptions.
  *
  * @param id The identifier (e.g. ID) for the DFASDL.
  * @param content A string holding the DFASDL.
  * @param version The version of the DFASDL, defaults to `1.0-SNAPSHOT`
  */
final case class DFASDL(
    id: String,
    content: String,
    version: String = "1.0-SNAPSHOT"
) {

  override def equals(obj: scala.Any): Boolean =
    obj match {
      case other: DFASDL ⇒
        id == other.id && content == other.content && version == other.version
      case _ ⇒ false
    }

  override def hashCode(): Int =
    431 + 7 * id.hashCode + 7 * content.hashCode + 7 * version.hashCode

}

object DFASDL {

  // The prefix used for auto-generated IDs.
  val AUTO_ID_PREFIX      = "auto-id"
  val AUTO_ID_REGEX       = s"$AUTO_ID_PREFIX-(\\d+)".r
  val DFASDL_ROOT_ELEMENT = "dfasdl" // FIXME We should use the dfasdl helper library here (if it is ready)!

  implicit def DFASDLCodecJson: CodecJson[DFASDL] =
    CodecJson(
      (dfasdl: DFASDL) ⇒
        ("id" := dfasdl.id) ->: ("content" := dfasdl.content) ->: ("version" := dfasdl.version) ->: jEmptyObject,
      cursor ⇒
        for {
          id      ← (cursor --\ "id").as[String]
          content ← (cursor --\ "content").as[String]
          version ← (cursor --\ "version").as[String]
        } yield DFASDL(id, content, version)
    )

  /**
    * Takes a dfasdl and fills missing ID fields with auto-generated values.
    *
    * @param dfasdl    A DFASDL.
    * @param indentXml A flag that indicates if the returned xml should be formatted human readable.
    * @return A DFASDL with no empty ID fields.
    */
  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def autogenerateMissingIds(dfasdl: DFASDL, indentXml: Boolean = false): DFASDL =
    if (dfasdl.content.isEmpty)
      dfasdl
    else {
      // Parse xml string into dom and do our work.
      val documentBuilderFactory = DocumentBuilderFactory.newInstance()
      val documentBuilder        = documentBuilderFactory.newDocumentBuilder()
      val xmlTree                = documentBuilder.parse(new InputSource(new StringReader(dfasdl.content)))
      xmlTree.getDocumentElement.normalize()
      val elementList: ListBuffer[Element] = new ListBuffer[Element]()
      val traversal                        = xmlTree.asInstanceOf[DocumentTraversal]
      val iterator = traversal.createNodeIterator(xmlTree.getDocumentElement,
                                                  NodeFilter.SHOW_ELEMENT,
                                                  null,
                                                  true)
      var needsAutoIds = false
      var currentNode  = iterator.nextNode()
      var idCounter    = 0L
      while (currentNode != null) {
        val e = currentNode.asInstanceOf[Element]
        elementList += e // Buffer element
        // Try to examine the largest existing auto-id counter.
        if (e.hasAttribute("id"))
          e.getAttribute("id") match {
            case AUTO_ID_REGEX(counter) ⇒
              // An auto-generated ID.
              val cnt = counter.toLong
              if (cnt > idCounter) idCounter = cnt
            case _ ⇒
            // A regular ID.
          }
        // If we aren't the root element we must have an id.
        if (e.getTagName != DFASDL_ROOT_ELEMENT)
          needsAutoIds =
            if (!e.hasAttribute("id") || e.getAttribute("id").isEmpty)
              true
            else
              false
        currentNode = iterator.nextNode()
      }
      // Only loop again if neccessary.
      if (needsAutoIds) {
        elementList.foreach(
          e ⇒
            if (e.getTagName != DFASDL_ROOT_ELEMENT && (!e
                  .hasAttribute("id") || e.getAttribute("id").isEmpty)) {
              idCounter = idCounter + 1
              e.setAttribute("id", s"$AUTO_ID_PREFIX-$idCounter")
          }
        )
        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
        if (indentXml) transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        val out: Writer = new StringWriter()
        transformer.transform(new DOMSource(xmlTree), new StreamResult(out))
        dfasdl.copy(content = out.toString)
      } else
        dfasdl
    }

}
