package com.team10415.autonopro.drive

import com.team10415.autonopro.localization.Position
import com.team10415.autonopro.math.Angle
import com.team10415.autonopro.path.Path
import com.team10415.autonopro.util.NanoClock

abstract class Drive {
    private val clock = NanoClock.system()

    var paths: MutableList<Path> = mutableListOf()
        private set
    var pathIndex = 0
        private set

    fun nextPath() = pathIndex++
    fun jumpToPath(name: Any) = run { pathIndex = paths.indexOfLast { it.name == name } }

    fun execPath(onFinished: (Drive) -> Unit): () -> Angle {
        val segments = paths[pathIndex].segments.toList()

        var segmentIndex = 0
        var segment = segments[segmentIndex]
        var trajectory = segment.createTrajectory()
        trajectory.origin = position

        var startTimestamp = Double.NaN

        val executor = executor@{
            val p = position
            if (p.copy(r = trajectory.endPos.r).approxEquals(trajectory.endPos)) {
                if ((trajectory.endPos.r ?: p.r!!).approxEquals(p.r!!)) {
                    segmentIndex++
                    segment = segments[segmentIndex]
                    trajectory = segment.createTrajectory()
                    trajectory.origin = p
                    return@executor Angle(0.0)
                } else {

                }
            }

            val dTimestamp = clock.seconds() - startTimestamp
            val dRotation = p.r!!.radians - (trajectory.endPos.r ?: p.r).radians
            val rSpeed = dRotation / dTimestamp

            val progress = trajectory.estimateProgress(p - trajectory.origin)
            val progressPerSecond = progress / dTimestamp

            trajectory.calculate(p)
        }

        return executor
    }

    abstract val position: Position
}