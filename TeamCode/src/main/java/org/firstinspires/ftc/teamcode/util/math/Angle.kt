package org.firstinspires.ftc.teamcode.util.math

import kotlin.math.cos
import kotlin.math.sin

class Angle(val radians: Double) {
    val degrees = Math.toDegrees(radians);

    companion object {
        val degrees = { degrees: Double ->
            Angle(Math.toRadians(degrees))
        }
    }

    val horizontal = cos(radians);
    val vertical = sin(radians);
}