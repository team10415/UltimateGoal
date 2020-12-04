package com.team10415.ftc_recorder

import com.qualcomm.robotcore.hardware.Gamepad
import com.team10415.ftc_recorder.Record.Input.*

data class Record(var gamepad: Gamepad) {
    init {
        val copy = Gamepad()
        copy.copy(gamepad)
        gamepad = copy
    }

    enum class Input {
        JOYSTICKS, DPAD, BUTTONS, BUMPERS, JOYSTICK_BTNS, TRIGGERS
    }

    fun export(inputs: List<Input>): String = inputs.fold("") { acc, it ->
        acc + gamepad.run {
            when (it) {
                JOYSTICKS -> "$left_stick_x;$left_stick_y;$right_stick_x;$right_stick_y"
                DPAD -> "$dpad_up;$dpad_right;$dpad_down;$dpad_left"
                BUTTONS -> "$a;$b;$x;$y"
                BUMPERS -> "$left_bumper;$right_bumper"
                JOYSTICK_BTNS -> "$left_stick_button;$right_stick_button"
                TRIGGERS -> "$left_trigger;$right_trigger"
            }
        } + "|"
    }

    companion object {
        fun parse(content: String, inputs: List<Input>): Gamepad {
            var gamepad = Gamepad()
            content.split("|").forEachIndexed content@{ i, it ->
                if (i >= inputs.size) return@content
                when (inputs[i]) {
                    JOYSTICKS -> {
                        val items = it.split(";")
                        gamepad.left_stick_x = items[0].toFloat()
                        gamepad.left_stick_y = items[1].toFloat()
                        gamepad.right_stick_x = items[2].toFloat()
                        gamepad.right_stick_y = items[3].toFloat()
                    }
                    DPAD -> {
                        val items = it.split(";")
                        gamepad.dpad_up = items[0].toBoolean()
                        gamepad.dpad_right = items[1].toBoolean()
                        gamepad.dpad_down = items[2].toBoolean()
                        gamepad.dpad_down = items[3].toBoolean()
                    }
                    BUTTONS -> {
                        val items = it.split(";")
                        gamepad.a = items[0].toBoolean()
                        gamepad.b = items[1].toBoolean()
                        gamepad.x = items[2].toBoolean()
                        gamepad.y = items[3].toBoolean()
                    }
                    BUMPERS -> {
                        val items = it.split(";")
                        gamepad.left_bumper = items[0].toBoolean()
                        gamepad.right_bumper = items[1].toBoolean()
                    }
                    JOYSTICK_BTNS -> {
                        val items = it.split(";")
                        gamepad.left_stick_button = items[0].toBoolean()
                        gamepad.right_stick_button = items[1].toBoolean()
                    }
                    TRIGGERS -> {
                        val items = it.split(";")
                        gamepad.left_trigger = items[0].toFloat()
                        gamepad.right_trigger = items[1].toFloat()
                    }
                }
            }
            return gamepad
        }
    }
}