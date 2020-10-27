package com.team10415.autonopro.path

import com.team10415.autonopro.ctrl.PIDCtrl
import com.team10415.autonopro.localization.Position
import com.team10415.autonopro.math.Angle
import com.team10415.autonopro.math.EPSILON

class ToPosition(val endPos: Position) : PathSegment() {
    class _Trajectory(movement: ToPosition) : Trajectory() {
        override lateinit var origin: Position

        val pidCtrl = PIDCtrl(.003, .00003, 0.0)

        val function: (Double) -> Double = { (endPos.y - origin.y) / (endPos.x - origin.x) * it }
        val progression: (Double) -> Double = { (endPos.x - origin.x) / (endPos.y - origin.y) * it }

        override val estimateProgress: (Transformation) -> Double = {
            listOf(
                progression(it.x),
                progression(it.y)
            ).average()
        }

        override fun calculate(position: Position): Angle {
            var p = position
            if (p.approxEquals(origin)) {
                p = Position(EPSILON, EPSILON, position.r)
            }

            val fromOrigin = p - origin
            val t = estimateProgress(fromOrigin)
            val onPath = Transformation(function(t), function(t))

            val theta = (endPos - origin).theta

            pidCtrl.target = theta.radians
            val deltaPosition = onPath - fromOrigin

            return if (fromOrigin.approxEquals(onPath)) theta else Angle(
                (theta.radians + deltaPosition.theta.radians) / 2
            )
        }

        override val endPos = movement.endPos
    }

    override val createTrajectory: () -> Trajectory
        get() = TODO("Not yet implemented")

}