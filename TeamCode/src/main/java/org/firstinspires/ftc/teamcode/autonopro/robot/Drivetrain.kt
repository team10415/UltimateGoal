package org.firstinspires.ftc.teamcode.autonopro.robot

import org.firstinspires.ftc.teamcode.autonopro.util.Vector

interface Drivetrain {
    fun drive(direction: Vector, rotationSpeed: Double)
}