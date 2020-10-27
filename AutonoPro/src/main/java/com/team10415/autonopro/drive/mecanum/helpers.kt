package com.team10415.autonopro.drive.mecanum

import com.team10415.autonopro.math.Angle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val angledVelocities = { angle: Angle ->
    listOf(
        sin(angle.radians + PI / 4),
        cos(angle.radians + PI / 4),
        cos(angle.radians + PI / 4),
        sin(angle.radians + PI / 4)
    )
}