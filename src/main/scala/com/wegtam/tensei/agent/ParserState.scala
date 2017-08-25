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

package com.wegtam.tensei.agent

import argonaut._, Argonaut._

/**
  * This sealed trait describes all possible states of a parser on an agent system.
  */
sealed trait ParserState {
  override def toString: String = this match {
    case ParserState.Idle                   ⇒ "Idle"
    case ParserState.ValidatingSyntax       ⇒ "ValidatingSyntax"
    case ParserState.ValidatingAccess       ⇒ "ValidatingAccess"
    case ParserState.ValidatingChecksums    ⇒ "ValidatingChecksums"
    case ParserState.PreparingSourceData    ⇒ "PreparingSourceData"
    case ParserState.InitializingSubParsers ⇒ "InitializingSubParsers"
    case ParserState.Parsing                ⇒ "Parsing"
  }
}

object ParserState {
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  implicit def ParserStateCodecJson: CodecJson[ParserState] =
    CodecJson(
      (s: ParserState) ⇒ jString(s.toString),
      c ⇒
        for {
          s ← c.as[String]
        } yield
          s match {
            case "Idle"                   ⇒ Idle
            case "ValidatingSyntax"       ⇒ ValidatingSyntax
            case "ValidatingAccess"       ⇒ ValidatingAccess
            case "ValidatingChecksums"    ⇒ ValidatingChecksums
            case "PreparingSourceData"    ⇒ PreparingSourceData
            case "InitializingSubParsers" ⇒ InitializingSubParsers
            case "Parsing"                ⇒ Parsing
            case e: String                ⇒ throw new IllegalArgumentException(s"Unknown ParserState: '$e'!")
        }
    )

  case object Idle extends ParserState

  case object ValidatingSyntax extends ParserState

  case object ValidatingAccess extends ParserState

  case object ValidatingChecksums extends ParserState

  case object PreparingSourceData extends ParserState

  case object InitializingSubParsers extends ParserState

  case object Parsing extends ParserState

}
