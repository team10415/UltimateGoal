package com.team10415.ftc_recorder

import android.util.Log
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.ReadWriteFile
import org.firstinspires.ftc.robotcore.internal.system.AppUtil

abstract class AutonRunner : LinearOpMode() {
    abstract val name: String
    abstract val inputs: List<Record.Input>

    abstract val controller: Controller

    var gamepadRecords = mutableListOf<Pair<Gamepad, Gamepad>>()

    fun initController() {
        controller.also {
            it.gamepad1 = gamepad1
            it.gamepad2 = gamepad2
            it.hardwareMap = hardwareMap
            it.telemetry = telemetry
            it.init()
        }
    }

    override fun runOpMode() {
        telemetry.addLine("Parsing path file...")
        telemetry.update()
        val filename = "$name-path.txt"
        val file = AppUtil.getInstance().getSettingsFile(filename)
        val content = ReadWriteFile.readFile(file)
        val recordsList = content.split("\n\n").dropLast(1)
        recordsList.forEachIndexed { i, it ->
            Log.i("record", it)
            val gamepads = it.split("\n")
            gamepadRecords.plusAssign(Pair(Record.parse(gamepads[0], inputs), Record.parse(gamepads[1], inputs)))
        }
        telemetry.addLine("Ready...")
        telemetry.update()

        initController()

        while (!isStarted) controller.init_loop()

        val recordsIterator = gamepadRecords.iterator()

        controller.start()
        var i = 0;
        while (!isStopRequested) {
            if (!recordsIterator.hasNext()) break
            val records = recordsIterator.next()
            i++
            Log.i("record", records.first.left_stick_y.toString())
            controller.gamepad1.copy(records.first)
            controller.gamepad2.copy(records.second)
            controller.loop()
            telemetry.update()
        }

        controller.stop()
    }
}

fun main() {
    val k = listOf(1, 2, 3, 4, 5).iterator()
    println(k.next());
    println(k.next());
}