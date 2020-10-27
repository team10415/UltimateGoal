package com.team10415.autonopro.path

import com.team10415.autonopro.ctrl.PIDCtrl
import com.team10415.autonopro.localization.Position
import com.team10415.autonopro.math.Angle
import com.team10415.autonopro.math.EPSILON
import com.team10415.autonopro.math.Vector

class Line(val vector: Vector, val r: Angle? = null) : PathSegment() {
    val function: (Double) -> Double = { (vector.vertical / vector.horizontal) * it }
    val progression: (Double) -> Double = { (vector.horizontal / vector.vertical) * it }

    class _Trajectory(val line: Line) : Trajectory() {
        override lateinit var origin: Position

        val pidCtrl = PIDCtrl(.003, .00003, 0.0)

        override val estimateProgress: (Transformation) -> Double = {
            listOf(
                line.progression(it.x),
                line.progression(it.y)
            ).average()
        }

        override fun calculate(position: Position): Angle {
            var p = position
            if (p.approxEquals(origin)) {
                p = Position(EPSILON, EPSILON, position.r)
            }

            val fromOrigin = p - origin
            val t = estimateProgress(fromOrigin)
            val onPath = Transformation(line.function(t), line.function(t))

            val theta = line.vector.theta

            pidCtrl.target = theta.radians
            val deltaPosition = onPath - fromOrigin

            return if (fromOrigin.approxEquals(onPath)) theta else Angle(
                (theta.radians + deltaPosition.theta.radians) / 2
            )
        }

        override val endPos = origin + Transformation(line.function(1.0), line.function(1.0))
    }

    override val createTrajectory = { _Trajectory(this) }
}