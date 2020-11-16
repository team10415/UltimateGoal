package org.firstinspires.ftc.teamcode.autonopro.util

import kotlin.math.abs

val EPSILON = 1e-4

fun Double.approxEquals(double: Double) = abs(this - double) < EPSILON

fun Int.factorial() = (1..this).fold(1.toLong(), { acc: Long, i: Int -> acc * i.toLong() })