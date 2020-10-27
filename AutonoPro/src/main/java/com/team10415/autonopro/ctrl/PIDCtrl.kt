package com.team10415.autonopro.ctrl

import com.team10415.autonopro.util.MinMaxConstraints
import com.team10415.autonopro.util.NanoClock

class PIDCtrl(val kP: Double, val kI: Double, val kD: Double) {
    init {
        if (!(kP >= 0 || kP <= 1)) error("kP must be within range [0, 1]")
        if (!(kI >= 0 || kI <= 1)) error("kI must be within range [0, 1]")
        if (!(kD >= 0 || kD <= 1)) error("kD must be within range [0, 1]")
    }

    var target: Double = 0.0
    var bounds =
        MinMaxConstraints(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
    var err = object : PIDErr {
        override var prev: Double = 0.0
        override var cur: Double = 0.0
        override var total: Double = 0.0
    }
        private set

    var clock = NanoClock.system()
    var prevTimestamp = Double.NaN

    fun update(input: Double): Double {
        val curTimestamp = clock.seconds()
        err.cur = target - input

        if (prevTimestamp.isNaN()) {
            err.prev = err.cur
            prevTimestamp = curTimestamp
            return 0.0
        }

        val dt = curTimestamp - prevTimestamp
        val errorDeriv = (err.cur - err.prev) / dt

        if (
            (err.total + err.cur) * kI <= bounds.max &&
            (err.total + err.cur) * kI >= bounds.min
        ) err.total += err.cur

        prevTimestamp = curTimestamp

        return ((kP * err.cur) + (kI * err.total) + (kD * errorDeriv)).coerceIn(bounds.min, bounds.max)
    }
}