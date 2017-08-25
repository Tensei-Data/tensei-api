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

/**
  * We test our cluster role names because we need to be alerted if we ever change them!
  */
class ClusterConstants$Test extends DefaultSpec {
  describe("the name for the system") {
    val name = "tensei-system"
    it(s"must be '$name'") {
      ClusterConstants.systemName must be(name)
    }
  }

  describe("the top level actor name on an agent system") {
    val name = "TenseiAgent"
    it(s"must be '$name'") {
      ClusterConstants.topLevelActorNameOnAgent must be(name)
    }
  }

  describe("the top level actor name on the server system") {
    val name = "TenseiServer"
    it(s"must be '$name'") {
      ClusterConstants.topLevelActorNameOnServer must be(name)
    }
  }

  describe("the name for an agent node") {
    val name = "agent"
    it(s"must be '$name'") {
      ClusterConstants.Roles.agent must be(name)
    }
  }

  describe("the name for a frontend node") {
    val name = "frontend"
    it(s"must be '$name'") {
      ClusterConstants.Roles.frontend must be(name)
    }
  }

  describe("the name for a server node") {
    val name = "server"
    it(s"must be '$name'") {
      ClusterConstants.Roles.server must be(name)
    }
  }

  describe("the name for a watchdog node") {
    val name = "watchdog"
    it(s"must be '$name'") {
      ClusterConstants.Roles.watchdog must be(name)
    }
  }
}
