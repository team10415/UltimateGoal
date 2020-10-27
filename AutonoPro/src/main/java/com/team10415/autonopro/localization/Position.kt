package com.team10415.autonopro.localization

import com.team10415.autonopro.math.Angle
import com.team10415.autonopro.math.approxEquals
import com.team10415.autonopro.path.Transformation
import kotlin.math.*

data class Position(val x: Double, val y: Double, val r: Angle? = null) {
    val hypot: Double
        get() = hypot(x, y)

    val theta = Position.theta(x, y)

    companion object {
        val theta = { x: Double, y: Double ->
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

            Angle(theta)
        }
    }

    operator fun plus(transformation: Transformation) = Position(
        transformation.x + x,
        transformation.y + y,
        if (transformation.r == null) r else {
            if (r == null) transformation.r
            else transformation.r + r
        }
    )

    operator fun plus(position: Position) = Transformation(
        position.x + x,
        position.y + y,
        if (position.r == null) r else {
            if (r == null) position.r
            else position.r + r
        }
    )

    operator fun minus(transformation: Transformation) = Position(
        transformation.x - x,
        transformation.y - y,
        if (transformation.r == null) r else {
            if (r == null) transformation.r
            else transformation.r - r
        }
    )

    operator fun minus(position: Position) = Transformation(
        position.x - x,
        position.y - y,
        if (position.r == null) r else {
            if (r == null) position.r
            else position.r - r
        }
    )

    fun approxEquals(position: Position) = hypot.approxEquals(position.hypot) && theta.approxEquals(position.theta)
}