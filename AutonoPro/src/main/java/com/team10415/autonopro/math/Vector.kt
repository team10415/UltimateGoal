package com.team10415.autonopro.math

import com.team10415.autonopro.localization.Position
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

data class Vector(val magnitude: Double, val theta: Angle) {
    val horizontal = cos(theta.radians) * magnitude
    val vertical = sin(theta.radians) * magnitude

    companion object {
        fun fromXY(x: Double, y: Double) = Vector(hypot(x, y), Position.theta(x, y))
    }
}