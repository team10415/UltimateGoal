package com.team10415.autonopro.math

import kotlin.math.cos
import kotlin.math.sin

data class Angle(var radians: Double) {
    val degrees: Double
        get() = Math.toDegrees(radians)

    companion object {
        val degrees = { degrees: Double ->
            Angle(Math.toRadians(degrees))
        }
    }

    val horizontal = cos(radians)
    val vertical = sin(radians)

    operator fun plus(angle: Angle) = copy().apply { radians += angle.radians }
    operator fun minus(angle: Angle) = copy().apply { radians -= angle.radians }

    fun approxEquals(angle: Angle) = radians.approxEquals(angle.radians)
}