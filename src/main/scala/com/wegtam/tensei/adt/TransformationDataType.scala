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

object TransformationDataType {

  private val legalClasses = List(
    classOf[String],
    classOf[java.lang.Integer],
    classOf[java.lang.Long],
    classOf[java.lang.Short],
    classOf[java.lang.Byte],
    classOf[java.lang.Character],
    classOf[java.lang.Float],
    classOf[java.lang.Double],
    classOf[java.lang.Boolean],
    classOf[Array[Byte]]
  )

  def isValidDataType(klass: Class[_]): Boolean = legalClasses.contains(klass)

}
