package org.firstinspires.ftc.teamcode.autonopro.util

import kotlin.math.*

data class Pose(val x: Double, val y: Double, val r: Angle? = null) {
    val hypot: Double
        get() = hypot(x, y)

    val theta = theta(x, y)

    companion object {
        val theta = { x: Double, y: Double ->
            var theta = if (x != .0) atan(y / x) else PI / 2 * sign(y)

            theta = when (true) {
                x < 0 && y < 0 -> theta - PI
                x < 0 -> -PI / 2 + abs(theta)
                y < 0 -> PI / 2 + abs(theta)
                else -> theta
            }

            Angle(theta)
        }
    }

    operator fun plus(transformation: Transformation) = Pose(
        transformation.x + x,
        transformation.y + y,
        if (transformation.r == null) r else {
            if (r == null) transformation.r
            else transformation.r + r
        }
    )

    operator fun plus(pose: Pose) = Transformation(
        pose.x + x,
        pose.y + y,
        if (pose.r == null) r else {
            if (r == null) pose.r
            else pose.r + r
        }
    )

    operator fun minus(transformation: Transformation) = Pose(
        transformation.x - x,
        transformation.y - y,
        if (transformation.r == null) r else {
            if (r == null) transformation.r
            else transformation.r - r
        }
    )

    operator fun minus(pose: Pose) = Transformation(
        pose.x - x,
        pose.y - y,
        if (pose.r == null) r else {
            if (r == null) pose.r
            else pose.r - r
        }
    )

    fun approxEquals(pose: Pose) = hypot.approxEquals(pose.hypot) && theta.approxEquals(pose.theta)
}