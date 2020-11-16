package org.firstinspires.ftc.teamcode.autonopro

import org.firstinspires.ftc.teamcode.autonopro.util.Pose


class K {
    var i = 0

    fun k(): () -> Int {
        var j = i;
        return {
            j++
            j
        }
    }
}

fun main() {
    println(Pose.theta(.0, 1.0))
}