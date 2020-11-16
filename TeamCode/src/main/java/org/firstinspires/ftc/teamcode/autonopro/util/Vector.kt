package org.firstinspires.ftc.teamcode.autonopro.util

import kotlin.math.cos
import kotlin.math.sin

/*
data class Vector(val magnitude: Double, val theta: Angle) {
    val horizontal = magnitude * cos(theta.radians)
    val vertical = magnitude * sin(theta.radians)

    companion object {
        fun fromXY(x: Double, y: Double) = Vector(hypot(x, y), Pose.theta(x, y))
        fun from
    }

    operator fun plus(vector: Vector) = fromXY(horizontal + vector.horizontal, vertical + vector.vertical)
}*/

data class Vector(var horizontal: Double, var vertical: Double) {
    constructor(magnitude: Double, theta: Angle) : this(
        magnitude * cos(theta.radians), magnitude * sin(theta.radians)
    )

    val theta: Angle
        get() = Pose.theta(horizontal, vertical)

    operator fun plus(vector: Vector) = Vector(horizontal + vector.horizontal, vertical + vector.vertical)
}