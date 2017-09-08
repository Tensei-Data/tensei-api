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

package com.wegtam.tensei.server

import argonaut._, Argonaut._

/**
  * This sealed trait describes all possible states of the chef de cuisine.
  */
sealed trait ChefDeCuisineState {
  override def toString: String = this match {
    case ChefDeCuisineState.Booting      => "Booting"
    case ChefDeCuisineState.Initializing => "Initializing"
    case ChefDeCuisineState.Running      => "Running"
  }
}

object ChefDeCuisineState {
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  implicit def ChefDeCuisineStateCodecJson: CodecJson[ChefDeCuisineState] =
    CodecJson(
      (s: ChefDeCuisineState) => jString(s.toString),
      c =>
        for {
          s <- c.as[String]
        } yield
          s match {
            case "Booting"      => Booting
            case "Initializing" => Initializing
            case "Running"      => Running
            case e: String =>
              throw new IllegalArgumentException(s"Unknown ChefDeCuisineState: '$e'!")
        }
    )

  case object Booting extends ChefDeCuisineState

  case object Initializing extends ChefDeCuisineState

  case object Running extends ChefDeCuisineState

}
