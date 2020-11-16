package org.firstinspires.ftc.teamcode.autonopro.path

import org.firstinspires.ftc.teamcode.autonopro.ctrl.PIDCtrl
import org.firstinspires.ftc.teamcode.autonopro.util.Angle
import org.firstinspires.ftc.teamcode.autonopro.util.EPSILON
import org.firstinspires.ftc.teamcode.autonopro.util.Pose
import org.firstinspires.ftc.teamcode.autonopro.util.Transformation

class ToPose(val endPos: Pose) : PathSegment() {
    class _Trajectory(movement: ToPose) : Trajectory() {
        override lateinit var origin: Pose

        val pidCtrl = PIDCtrl(.003, .00003, 0.0)

        val function: (Double) -> Double = { (endPos.y - origin.y) / (endPos.x - origin.x) * it }
        val progression: (Double) -> Double = { (endPos.x - origin.x) / (endPos.y - origin.y) * it }

        override val estimateProgress: (Transformation) -> Double = {
            listOf(
                progression(it.x),
                progression(it.y)
            ).average()
        }

        override fun calculate(pose: Pose): Angle {
            var p = pose
            if (p.approxEquals(origin)) {
                p = Pose(EPSILON, EPSILON, pose.r)
            }

            val fromOrigin = p - origin
            val t = estimateProgress(fromOrigin)
            val onPath = Transformation(function(t), function(t))

            val theta = (endPos - origin).theta

            pidCtrl.target = theta.radians
            val deltaPose = onPath - fromOrigin

            return if (fromOrigin.approxEquals(onPath)) theta else Angle(
                (theta.radians + deltaPose.theta.radians) / 2
            )
        }

        override val endPos = movement.endPos
    }

    override val createTrajectory: () -> Trajectory
        get() = TODO("Not yet implemented")

}