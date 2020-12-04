package com.team10415.ftc_recorder

import com.qualcomm.robotcore.eventloop.opmode.OpMode

abstract class TeleOpRunner : OpMode() {
    abstract val controller: Controller


    override fun init() {
        //----------------------------------------------------------------------------------------------
        // Safety Management
        //
        // These constants manage the duration we allow for callbacks to user code to run for before
        // such code is considered to be stuck (in an infinite loop, or wherever) and consequently
        // the robot controller application is restarted. They SHOULD NOT be modified except as absolutely
        // necessary as poorly chosen values might inadvertently compromise safety.
        //----------------------------------------------------------------------------------------------
        msStuckDetectInit = 50000
        msStuckDetectInitLoop = 50000
        msStuckDetectStart = 50000
        msStuckDetectLoop = 50000
        msStuckDetectStop = 9000

        controller.also {
            it.gamepad1 = gamepad1
            it.gamepad2 = gamepad2
            it.hardwareMap = hardwareMap
            it.telemetry = telemetry
            it.init()
        }
    }

    override fun init_loop() = controller.init_loop()
    override fun start() = controller.start()
    override fun loop() = controller.loop()
    override fun stop() = controller.stop()
}