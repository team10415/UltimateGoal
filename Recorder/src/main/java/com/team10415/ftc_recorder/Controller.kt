package com.team10415.ftc_recorder

import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

abstract class Controller {
    lateinit var gamepad1: Gamepad
    lateinit var gamepad2: Gamepad
    lateinit var hardwareMap: HardwareMap
    lateinit var telemetry: Telemetry

    open fun init() {}
    open fun init_loop() {}
    open fun start() {}
    open fun loop() {}
    open fun stop() {}
}