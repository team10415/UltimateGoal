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

    val triggers = Pair(gamepad.left_trigger.toDouble(), gamepad.right_trigger.toDouble())

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
    }

    private val flipCtrls = object : GamepadButtons {
        override var a = false
        override var b = false
        override var x = false
        override var y = false
    }
    val flips = object : GamepadButtons {
        override var a = false
            get() = gamepad.a != flipCtrls.a.also {
                flipCtrls.a = gamepad.a
            }
        override var b = false
            get() = gamepad.b != flipCtrls.b.also {
                flipCtrls.b = gamepad.b
            }
        override var x = false
            get() = gamepad.x != flipCtrls.x.also {
                flipCtrls.x = gamepad.x
            }
        override var y = false
            get() = gamepad.y != flipCtrls.y.also {
                flipCtrls.y = gamepad.y
            }
    }

    private var listeners = object : GamepadButtonListeners {
        override val a = mutableListOf<() -> Unit>()
        override val b = mutableListOf<() -> Unit>()
        override val x = mutableListOf<() -> Unit>()
        override val y = mutableListOf<() -> Unit>()
    }

    fun onClick(button: BenignButtons, listener: () -> Unit) {
        when (button) {
            BenignButtons.A -> listeners.a
            BenignButtons.B -> listeners.b
            BenignButtons.X -> listeners.x
            BenignButtons.Y -> listeners.y
        } += listener
    }

    fun activateClicks() {
        if (flips.a) listeners.a.forEach { it() }
        if (flips.b) listeners.b.forEach { it() }
        if (flips.x) listeners.x.forEach { it() }
        if (flips.y) listeners.y.forEach { it() }
    }
}

interface GamepadSticks {
    val left: GamepadStick
    val right: GamepadStick
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
}

interface GamepadButtonListeners {
    val a: MutableList<() -> Unit>
    val b: MutableList<() -> Unit>
    val x: MutableList<() -> Unit>
    val y: MutableList<() -> Unit>
}