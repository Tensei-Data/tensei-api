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
  * This sealed trait describes all possible states of the `TenseiAgent` which leads
  */
sealed trait TenseiAgentState {
  override def toString: String = this match {
    case TenseiAgentState.Aborting              ⇒ "Aborting"
    case TenseiAgentState.CleaningUp            ⇒ "CleaningUp"
    case TenseiAgentState.Idle                  ⇒ "Idle"
    case TenseiAgentState.InitializingResources ⇒ "InitializingResources"
    case TenseiAgentState.Working               ⇒ "Working"
  }
}

object TenseiAgentState {
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  implicit def TenseiAgentStateCodecJson: CodecJson[TenseiAgentState] =
    CodecJson(
      (s: TenseiAgentState) ⇒ jString(s.toString),
      c ⇒
        for {
          s ← c.as[String]
        } yield
          s match {
            case "Aborting"              ⇒ Aborting
            case "CleaningUp"            ⇒ CleaningUp
            case "Idle"                  ⇒ Idle
            case "InitializingResources" ⇒ InitializingResources
            case "Working"               ⇒ Working
            case e: String               ⇒ throw new IllegalArgumentException(s"Unknown TenseiAgentState: '$e'!")
        }
    )

  case object Aborting extends TenseiAgentState

  case object CleaningUp extends TenseiAgentState

  case object Idle extends TenseiAgentState

  case object InitializingResources extends TenseiAgentState

  case object Working extends TenseiAgentState

}
