package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.hardware.Gamepad as OldGamepad

class Gamepad(gamepad: OldGamepad) {

    val sticks = object : GamepadSticks {
        override val left = object : GamepadStick {
            override val x: Double
                get() = gamepad.left_stick_x.toDouble()
            override val y: Double
                get() = gamepad.left_stick_y.toDouble()
        }

        override val right = object : GamepadStick {
            override val x: Double
                get() = gamepad.right_stick_x.toDouble()
            override val y: Double
                get() = gamepad.right_stick_y.toDouble()
        }
    }

    val triggers = object : GamepadTriggers {
        override val left: Double
            get() = gamepad.left_trigger.toDouble()

        override val right: Double
            get() = gamepad.right_trigger.toDouble()
    }

    val buttons = object : GamepadButtons {
        override var a: Boolean
            get() = gamepad.a
            set(_) {}
        override var b: Boolean
            get() = gamepad.b
            set(_) {}
        override var x: Boolean
            get() = gamepad.x
            set(_) {}
        override var y: Boolean
            get() = gamepad.y
            set(_) {}
        override var dpad_up: Boolean
            get() = gamepad.dpad_up
            set(_) {}
        override var dpad_down: Boolean
            get() = gamepad.dpad_down
            set(_) {}
    }

    private val flipCtrls = object : GamepadButtons {
        override var a = false
        override var b = false
        override var x = false
        override var y = false
        override var dpad_up = false
        override var dpad_down = false
    }
    val flips = object : GamepadButtons {
        override var a = false
            get() = (gamepad.a && gamepad.a != flipCtrls.a).also {
                flipCtrls.a = gamepad.a
            }
        override var b = false
            get() = (gamepad.b && gamepad.b != flipCtrls.b).also {
                flipCtrls.b = gamepad.b
            }
        override var x = false
            get() = (gamepad.x && gamepad.x != flipCtrls.x).also {
                flipCtrls.x = gamepad.x
            }
        override var y = false
            get() = (gamepad.y && gamepad.y != flipCtrls.y).also {
                flipCtrls.y = gamepad.y
            }
        override var dpad_up = false
            get() = (gamepad.dpad_up && gamepad.dpad_up != flipCtrls.dpad_up).also {
                flipCtrls.dpad_up = gamepad.dpad_up
            }
        override var dpad_down = false
            get() = (gamepad.dpad_down && gamepad.dpad_down != flipCtrls.dpad_down).also {
                flipCtrls.dpad_down = gamepad.dpad_down
            }
    }

    private var listeners = object : GamepadButtonListeners {
        override val a = mutableListOf<() -> Unit>()
        override val b = mutableListOf<() -> Unit>()
        override val x = mutableListOf<() -> Unit>()
        override val y = mutableListOf<() -> Unit>()
        override val dpad_up = mutableListOf<() -> Unit>()
        override val dpad_down = mutableListOf<() -> Unit>()
    }

    fun onClick(button: BenignButtons, listener: () -> Unit) {
        when (button) {
            BenignButtons.A -> listeners.a
            BenignButtons.B -> listeners.b
            BenignButtons.X -> listeners.x
            BenignButtons.Y -> listeners.y
            BenignButtons.DPAD_UP -> listeners.dpad_up
            BenignButtons.DPAD_DOWN -> listeners.dpad_down
        } += listener
    }

    fun activateClicks() {
        if (flips.a) listeners.a.forEach { it() }
        if (flips.b) listeners.b.forEach { it() }
        if (flips.x) listeners.x.forEach { it() }
        if (flips.y) listeners.y.forEach { it() }
        if (flips.dpad_up) listeners.dpad_up.forEach { it() }
        if (flips.dpad_down) listeners.dpad_down.forEach { it() }
    }
}

interface GamepadSticks {
    val left: GamepadStick
    val right: GamepadStick
}

interface GamepadTriggers {
    val left: Double
    val right: Double
}

interface GamepadStick {
    val x: Double
    val y: Double
}

interface GamepadButtons {
    var a: Boolean
    var b: Boolean
    var x: Boolean
    var y: Boolean
    var dpad_down: Boolean
    var dpad_up: Boolean
}

interface GamepadButtonListeners {
    val a: MutableList<() -> Unit>
    val b: MutableList<() -> Unit>
    val x: MutableList<() -> Unit>
    val y: MutableList<() -> Unit>
    val dpad_down: MutableList<() -> Unit>
    val dpad_up: MutableList<() -> Unit>
}