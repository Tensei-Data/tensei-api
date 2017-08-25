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
  * This sealed trait describes all possible states of a processor on an agent system.
  */
sealed trait ProcessorState {
  override def toString: String = this match {
    case ProcessorState.Idle                    ⇒ "Idle"
    case ProcessorState.Sorting                 ⇒ "Sorting"
    case ProcessorState.Processing              ⇒ "Processing"
    case ProcessorState.WaitingForWriterClosing ⇒ "WaitingForWriterClosing"
  }
}

object ProcessorState {
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  implicit def ProcessorStateCodecJson: CodecJson[ProcessorState] =
    CodecJson(
      (s: ProcessorState) ⇒ jString(s.toString),
      c ⇒
        for {
          s ← c.as[String]
        } yield
          s match {
            case "Idle"                    ⇒ Idle
            case "Sorting"                 ⇒ Sorting
            case "Processing"              ⇒ Processing
            case "WaitingForWriterClosing" ⇒ WaitingForWriterClosing
            case e: String                 ⇒ throw new IllegalArgumentException(s"Unknown ProcessorState: '$e'!")
        }
    )

  case object Idle extends ProcessorState

  case object Sorting extends ProcessorState

  case object Processing extends ProcessorState

  case object WaitingForWriterClosing extends ProcessorState

}
