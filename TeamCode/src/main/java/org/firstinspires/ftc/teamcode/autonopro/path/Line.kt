package org.firstinspires.ftc.teamcode.autonopro.path

import org.firstinspires.ftc.teamcode.autonopro.ctrl.PIDCtrl
import org.firstinspires.ftc.teamcode.autonopro.util.*

class Line(val vector: Vector, val r: Angle? = null) : PathSegment() {
    val function: (Double) -> Double = { (vector.vertical / vector.horizontal) * it }
    val progression: (Double) -> Double = { (vector.horizontal / vector.vertical) * it }

    class _Trajectory(val line: Line) : Trajectory() {
        override lateinit var origin: Pose

        val pidCtrl = PIDCtrl(.003, .00003, 0.0)

        override val estimateProgress: (Transformation) -> Double = {
            listOf(
                line.progression(it.x),
                line.progression(it.y)
            ).average()
        }

        override fun calculate(pose: Pose): Angle {
            var p = pose
            if (p.approxEquals(origin)) {
                p = Pose(EPSILON, EPSILON, pose.r)
            }

            val fromOrigin = p - origin
            val t = estimateProgress(fromOrigin)
            val onPath = Transformation(line.function(t), line.function(t))

            val theta = line.vector.theta

            pidCtrl.target = theta.radians
            val deltaPose = onPath - fromOrigin

            return if (fromOrigin.approxEquals(onPath)) theta else Angle(
                (theta.radians + deltaPose.theta.radians) / 2
            )
        }

        override val endPos = origin + Transformation(line.function(1.0), line.function(1.0))
    }

    override val createTrajectory = { _Trajectory(this) }
}