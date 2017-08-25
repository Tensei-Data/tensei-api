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

import argonaut._, Argonaut._

/**
  * Defines all possible authorization states for an agent.
  */
sealed trait AgentAuthorizationState {
  override def toString: String = this match {
    case AgentAuthorizationState.Connected    ⇒ "Connected"
    case AgentAuthorizationState.Disconnected ⇒ "Disconnected"
    case AgentAuthorizationState.Unauthorized ⇒ "Unauthorized"
  }
}

/**
  * The concrete authorization states package into an object.
  */
object AgentAuthorizationState {
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  implicit def AgentAuthorizationStateCodecJson: CodecJson[AgentAuthorizationState] =
    CodecJson(
      (s: AgentAuthorizationState) ⇒ jString(s.toString),
      c ⇒
        for {
          s ← c.as[String]
        } yield
          s match {
            case "Connected"    ⇒ Connected
            case "Disconnected" ⇒ Disconnected
            case "Unauthorized" ⇒ Unauthorized
            case e: String ⇒
              throw new IllegalArgumentException(s"Unknown AgentAuthorizationState: '$e'!")
        }
    )

  /**
    * The agent is connected to the cluster and has a valid license (e.g. is authorized).
    */
  case object Connected extends AgentAuthorizationState

  /**
    * The agent is not connected to the cluster.
    */
  case object Disconnected extends AgentAuthorizationState

  /**
    * The agent is connected to the cluster but not authorized to work (for example it's license is not valid or has expired).
    */
  case object Unauthorized extends AgentAuthorizationState

}
