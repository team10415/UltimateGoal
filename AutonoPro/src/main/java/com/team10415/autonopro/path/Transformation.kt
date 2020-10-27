package com.team10415.autonopro.path

import com.team10415.autonopro.math.Angle
import com.team10415.autonopro.math.Vector
import com.team10415.autonopro.math.approxEquals
import kotlin.math.*

data class Transformation(val x: Double, val y: Double, val r: Angle? = null) {
    fun toLine() = Line(Vector(hypot(x, y), Angle(tan(y / x))), r)

    val hypot: Double
        get() = hypot(x, y)

    val theta: Angle
        get() {
            var theta = try {
                atan(y / x)
            } catch (e: Exception) {
                PI / 2 * sign(x) * sign(y)
            }

            theta = when (true) {
                x < 0 && y < 0 -> theta - PI
                x < 0 -> -PI / 2 + abs(theta)
                y < 0 -> PI / 2 + abs(theta)
                else -> theta
            }

            return Angle(theta)
        }

    operator fun plus(transformation: Transformation) = Transformation(
        transformation.x + x,
        transformation.y + y,
        if (transformation.r == null) r else {
            if (r == null) transformation.r
            else transformation.r + r
        }
    )

    operator fun minus(transformation: Transformation) = Transformation(
        transformation.x - x,
        transformation.y - y,
        if (transformation.r == null) r else {
            if (r == null) transformation.r
            else transformation.r - r
        }
    )

    fun approxEquals(transformation: Transformation) =
        hypot.approxEquals(transformation.hypot) && theta.approxEquals(transformation.theta)
}