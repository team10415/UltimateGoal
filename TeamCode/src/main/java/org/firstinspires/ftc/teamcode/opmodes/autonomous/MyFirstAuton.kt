package org.firstinspires.ftc.teamcode.opmodes.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor

@Autonomous
class MyFirstAuton : LinearOpMode() {
    override fun runOpMode() {
        val m1 = hardwareMap.get(DcMotor::class.java, "m1")
        val m2 = hardwareMap.get(DcMotor::class.java, "m2")
        

    }
}