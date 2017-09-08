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
  * Some statistics regarding the jvm runtime.
  *
  * @param freeMemory  The amount of free memory in bytes.
  * @param maxMemory   The maximum possible amount of memory available to the jvm in bytes.
  * @param totalMemory The total available memory for the jvm in bytes.
  */
final case class RuntimeStats(
    freeMemory: Long,
    maxMemory: Long,
    totalMemory: Long,
    processors: Int = 1,
    systemLoad: Option[Double] = None
)

object RuntimeStats {

  implicit def RuntimeStatsCodecJson: CodecJson[RuntimeStats] =
    CodecJson(
      (s: RuntimeStats) =>
        ("load" := s.systemLoad) ->: ("processors" := jNumber(s.processors)) ->: ("total" := jNumber(
          s.totalMemory
        )) ->: ("max" := jNumber(s.maxMemory)) ->: ("free" := jNumber(s.freeMemory)) ->: jEmptyObject,
      c =>
        for {
          free  <- (c --\ "free").as[Long]
          max   <- (c --\ "max").as[Long]
          total <- (c --\ "total").as[Long]
          p     <- (c --\ "processors").as[Int]
          load  <- (c --\ "load").as[Option[Double]]
        } yield
          RuntimeStats(freeMemory = free,
                       maxMemory = max,
                       totalMemory = total,
                       processors = p,
                       systemLoad = load)
    )

  /**
    * Creates a runtime stats object holding the stats of the current runtime.
    *
    * @return The current runtime stats.
    */
  def getCurrentRuntimeStats: RuntimeStats = {
    val runtime = Runtime.getRuntime
    val osBean  = java.lang.management.ManagementFactory.getOperatingSystemMXBean
    new RuntimeStats(
      freeMemory = runtime.freeMemory(),
      maxMemory = runtime.maxMemory(),
      totalMemory = runtime.totalMemory(),
      processors = osBean.getAvailableProcessors,
      systemLoad = Option(osBean.getSystemLoadAverage)
    )
  }

}
