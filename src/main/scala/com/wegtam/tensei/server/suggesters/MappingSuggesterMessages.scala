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

import akka.actor.ActorRef
import com.wegtam.tensei.adt.{ Cookbook, StatusMessage }

/**
  * The messages that can passed to and received from a mapping suggester.
  */
sealed trait MappingSuggesterMessages

object MappingSuggesterMessages {

  /**
    * If an error occurs, this message is used to return the error details.
    *
    * @param error An error message.
    */
  final case class MappingSuggesterErrorMessage(error: StatusMessage)
      extends MappingSuggesterMessages

  /**
    * Suggest and return a mapping for the given cookbook.
    *
    * @param cookbook  The cookbook that holds the information needed for the mapping creation.
    * @param mode      The mode of the mapping suggester (`MappingSuggesterModes`) which defaults to `Simple`.
    * @param answerTo  An option to an actor ref that should receive the answer instead of the sender.
    */
  final case class SuggestMapping(cookbook: Cookbook,
                                  mode: MappingSuggesterModes = MappingSuggesterModes.Simple,
                                  answerTo: Option[ActorRef] = None)
      extends MappingSuggesterMessages

  /**
    * This message holds the cookbook containing the suggested mapping.
    *
    * @param cookbook  The cookbook holding the mapping.
    * @param mode      The mode that was used to create the mapping.
    */
  final case class SuggestedMapping(cookbook: Cookbook, mode: MappingSuggesterModes)
      extends MappingSuggesterMessages

}
