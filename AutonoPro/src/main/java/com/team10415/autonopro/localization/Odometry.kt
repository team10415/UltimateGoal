package com.team10415.autonopro.localization

import com.qualcomm.robotcore.hardware.DcMotor

data class Odometry(val horizontal: Pair<DcMotor, DcMotor>, val vertical: DcMotor) {
    val values: Triple<Int, Int, Int>
        get() = Triple(
            horizontal.first.currentPosition, horizontal.second.currentPosition,
            vertical.currentPosition
        )
}