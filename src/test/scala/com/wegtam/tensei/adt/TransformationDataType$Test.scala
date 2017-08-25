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

import com.wegtam.tensei.DefaultSpec

class TransformationDataType$Test extends DefaultSpec {
  describe("isValidDataType") {
    describe("the following data types must be valid") {
      it("String") {
        TransformationDataType.isValidDataType(classOf[String]) must be(true)
      }

      it("Integer") {
        TransformationDataType.isValidDataType(classOf[Integer]) must be(true)
      }

      it("Long") {
        TransformationDataType.isValidDataType(classOf[java.lang.Long]) must be(true)
      }

      it("Short") {
        TransformationDataType.isValidDataType(classOf[java.lang.Short]) must be(true)
      }

      it("Byte") {
        TransformationDataType.isValidDataType(classOf[java.lang.Byte]) must be(true)
      }

      it("Character") {
        TransformationDataType.isValidDataType(classOf[Character]) must be(true)
      }

      it("Float") {
        TransformationDataType.isValidDataType(classOf[java.lang.Float]) must be(true)
      }

      it("Double") {
        TransformationDataType.isValidDataType(classOf[java.lang.Double]) must be(true)
      }

      it("Boolean") {
        TransformationDataType.isValidDataType(classOf[java.lang.Boolean]) must be(true)
      }

      it("Array[Byte]") {
        TransformationDataType.isValidDataType(classOf[Array[Byte]]) must be(true)
      }
    }
  }
}
