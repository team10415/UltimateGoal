package org.firstinspires.ftc.teamcode.autonopro.path

import org.firstinspires.ftc.teamcode.autonopro.ctrl.PIDCtrl
import org.firstinspires.ftc.teamcode.autonopro.util.*
import kotlin.math.abs
import kotlin.math.pow

class BezierCurve(val p1: Transformation, val p2: Transformation, val end: Transformation) : PathSegment() {
    private val f = { n: Int, k: Int -> (n.factorial()) / (k.factorial() * (n - k).factorial()) }
    private val b = { t: Double, K: Int, v: Double -> f(3, K) * (1 - t).pow(3 - K) * t.pow(K) * v }

    val xFun = { t: Double ->
        b(abs(t - 1), 0, end.x) + b(abs(t - 1), 1, p1.x) + b(abs(t - 1), 2, p2.x)
    }
    val yFun = { t: Double ->
        b(abs(t - 1), 0, end.y) + b(abs(t - 1), 1, p1.y) + b(abs(t - 1), 2, p2.y)
    }

    val xDerivFun = { t: Double ->
        -((-3 * end.x * (1 - abs(t - 1)).pow(2)) +
                (3 * p1.x * (3 * abs(t - 1).pow(2) - 4 * abs(t - 1) + 1)) +
                (3 * p2.x * (-3 * abs(t - 1).pow(2) + 2 * abs(t - 1))))
    }
    val yDerivFun = { t: Double ->
        -((-3 * end.y * (1 - abs(t - 1)).pow(2)) +
                (3 * p1.y * (3 * abs(t - 1).pow(2) - 4 * abs(t - 1) + 1)) +
                (3 * p2.y * (-3 * abs(t - 1).pow(2) + 2 * abs(t - 1))))
    }

    val progression = { amnt: Double, function: (Double) -> Double ->
        var t: Double
        var clamps = MinMaxConstraints(0.0, 1.0)
        var approxAmnt: Double

        while (true) {
            t = (clamps.max + clamps.min) / 2
            approxAmnt = function(t)
            /*println(amnt)
            println(approxAmnt)
            println(amnt - approxAmnt)
            println(EPSILON)
            println()*/
            if (amnt.approxEquals(approxAmnt)) break
            if (amnt > approxAmnt) clamps.min = t
            if (amnt < approxAmnt) clamps.max = t
        }
        t
    }

    class _Trajectory(val curve: BezierCurve) : Trajectory() {
        override lateinit var origin: Pose

        val pidCtrl = PIDCtrl(.003, .00003, 0.0)

        override val estimateProgress: (Transformation) -> Double = {
            listOf(
                curve.progression(it.x, curve.xFun),
                curve.progression(it.y, curve.yFun)
            ).average()
        }

        override fun calculate(pose: Pose): Angle {
            var p = pose
            if (p.approxEquals(origin)) {
                p = Pose(EPSILON, EPSILON, pose.r)
            }

            val fromOrigin = p - origin
            val t = estimateProgress(fromOrigin)
            val onPath = Transformation(curve.xFun(t), curve.yFun(t))

            val xSlope = curve.xDerivFun(t)
            val ySlope = curve.yDerivFun(t)
            val theta = Pose(xSlope, ySlope).theta

            pidCtrl.target = theta.radians
            val deltaPose = onPath - fromOrigin

            return if (fromOrigin.approxEquals(onPath)) theta else Angle(
                (theta.radians + deltaPose.theta.radians) / 2 + pidCtrl.update(deltaPose.theta.radians)
            )
        }

        override val endPos = origin + curve.end
    }

    override val createTrajectory = { _Trajectory(this) }
}