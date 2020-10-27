package com.team10415.autonopro.math

import kotlin.math.abs

val EPSILON = 1e-4

fun Double.approxEquals(double: Double) = abs(this - double) < EPSILON

fun Int.factorial() = (1..this).fold(1.toLong(), { acc: Long, i: Int -> acc * i.toLong() })