package org.firstinspires.ftc.teamcode.util.math

import kotlin.math.roundToInt

fun Double.withinRange(min: Double, max: Double): Double {
    return max.coerceAtMost(this.coerceAtLeast(min))
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (this * multiplier).roundToInt() / multiplier
}
