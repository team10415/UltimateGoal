package com.team10415.ftc_recorder

import android.util.Log
import com.qualcomm.robotcore.util.ReadWriteFile
import org.firstinspires.ftc.robotcore.internal.system.AppUtil

abstract class Recorder : TeleOpRunner() {
    abstract val name: String
    abstract val inputs: List<Record.Input>

    var content = ""

    override fun loop() {
        super.loop()
        content += Record(gamepad1).export(inputs) + "\n" + Record(gamepad2).export(inputs) + "\n\n"
        //Log.i("iteration", Record(gamepad1).export(inputs) + "\n" + Record(gamepad2).export(inputs) + "\n\n")
        if (gamepad1.a) Log.i("content", content)
    }

    override fun stop() {
        super.stop()

        val filename = "$name-path.txt"
        val file = AppUtil.getInstance().getSettingsFile(filename)
        ReadWriteFile.writeFile(
            file, content
        )

        Log.i("content", content)
    }
}