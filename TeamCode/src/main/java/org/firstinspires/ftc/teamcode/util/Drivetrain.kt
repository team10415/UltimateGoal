package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.util.math.round
import kotlin.math.abs
import kotlin.math.pow

class Drivetrain(val fl: DcMotor?, val fr: DcMotor?, val bl: DcMotor?, val br: DcMotor?) {
    val front = Pair(fl, fr)
    val back = Pair(bl, br)
    val left = Pair(fl, bl)
    val right = Pair(fr, br)
    val diagonals = Pair(Pair(fl, br), Pair(fr, bl))

    private var _speed: Speed = object : Speed {
        override var horizontal: Double? = 0.0
        override var vertical: Double? = 0.0
        override var rotation: Double? = 0.0
    }

    var speed: Speed
        get() = _speed
        set(speed) {
            _speed.horizontal = speed.horizontal ?: _speed.horizontal
            _speed.vertical = speed.vertical ?: _speed.vertical
            _speed.rotation = speed.rotation ?: _speed.rotation
        }

    fun zoom(movementType: MovementType) {
        val vals = if (movementType == MovementType.MECANUM) List(4) {
            when (it) {
                0 -> speed.vertical!! + speed.horizontal!! + speed.rotation!!
                1 -> speed.vertical!! - speed.horizontal!! - speed.rotation!!
                2 -> speed.vertical!! - speed.horizontal!! + speed.rotation!!
                4 -> speed.vertical!! + speed.horizontal!! - speed.rotation!!
                else -> 0.0
            }
        } else List(4) {
            when (it) {
                0 -> speed.vertical!! + 2.0.pow(speed.horizontal!!) - 1
                1 -> speed.vertical!! - 2.0.pow(speed.horizontal!!) - 1
                2 -> speed.vertical!! + 2.0.pow(speed.horizontal!!) - 1
                4 -> speed.vertical!! - 2.0.pow(speed.horizontal!!) - 1
                else -> 0.0
            }
        }

        val max = vals.reduce { acc, d ->
            maxOf(acc, abs(d))
        }

        val adjustedVals = vals.map { d ->
            when (max > 1) {
                true -> d * (1 / max)
                false -> d
            }.round(2)
        }

        println("${adjustedVals[0]}\t${adjustedVals[1]}\n${adjustedVals[2]}\t${adjustedVals[3]}\n")
    }

    fun test(configuration: Drivetrain.() -> Unit) = configuration()

}


fun main() {
}