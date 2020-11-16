package org.firstinspires.ftc.teamcode.autonopro.path

import org.firstinspires.ftc.teamcode.autonopro.util.Angle
import org.firstinspires.ftc.teamcode.autonopro.util.Pose
import org.firstinspires.ftc.teamcode.autonopro.util.Transformation

abstract class Trajectory {
    abstract var origin: Pose
    abstract val endPos: Pose

    abstract val estimateProgress: (transformation: Transformation) -> Double
    abstract fun calculate(pose: Pose): Angle
}