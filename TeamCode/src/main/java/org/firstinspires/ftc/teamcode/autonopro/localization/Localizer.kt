package org.firstinspires.ftc.teamcode.autonopro.localization

import org.firstinspires.ftc.teamcode.autonopro.util.Pose
import org.firstinspires.ftc.teamcode.autonopro.util.Vector

interface Localizer {
    val velocity: Vector
    var currentPose: Pose

    fun update()
}