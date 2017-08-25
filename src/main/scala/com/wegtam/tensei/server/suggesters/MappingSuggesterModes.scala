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

package com.wegtam.tensei.server.suggesters

import argonaut._, Argonaut._

/**
  * The possible modes for a mapping suggester.
  */
sealed trait MappingSuggesterModes {
  override def toString: String = this match {
    case MappingSuggesterModes.Simple                          ⇒ "Simple"
    case MappingSuggesterModes.SimpleWithTransformers          ⇒ "SimpleWithTransformers"
    case MappingSuggesterModes.SimpleSemantics                 ⇒ "SimpleSemantics"
    case MappingSuggesterModes.SimpleSemanticsWithTransformers ⇒ "SimpleSemanticsWithTransformers"
    case MappingSuggesterModes.AdvancedSemantics               ⇒ "AdvancedSemantics"
  }
}

/**
  * Container object to keep the namespace clean.
  */
object MappingSuggesterModes {
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  implicit def MappingSuggesterModesCodecJson: CodecJson[MappingSuggesterModes] =
    CodecJson(
      (m: MappingSuggesterModes) ⇒ jString(m.toString),
      c ⇒
        for {
          m ← c.as[String]
        } yield
          m match {
            case "Simple"                          ⇒ Simple
            case "SimpleWithTransformers"          ⇒ SimpleWithTransformers
            case "SimpleSemantics"                 ⇒ SimpleSemantics
            case "SimpleSemanticsWithTransformers" ⇒ SimpleSemanticsWithTransformers
            case "AdvancedSemantics"               ⇒ AdvancedSemantics
            case e: String ⇒
              throw new IllegalArgumentException(s"Unknown MappingSuggesterMode: '$e'!")
        }
    )

  case object Simple extends MappingSuggesterModes

  case object SimpleWithTransformers extends MappingSuggesterModes

  case object SimpleSemantics extends MappingSuggesterModes

  case object SimpleSemanticsWithTransformers extends MappingSuggesterModes

  case object AdvancedSemantics extends MappingSuggesterModes

}
