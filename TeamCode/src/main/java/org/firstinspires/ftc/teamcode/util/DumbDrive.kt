package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.autonopro.robot.Drivetrain
import org.firstinspires.ftc.teamcode.autonopro.util.Vector
import org.firstinspires.ftc.teamcode.util.math.round
import kotlin.math.abs
import kotlin.math.pow

class DumbDrive(hardwareMap: HardwareMap) : Drivetrain {
    val fl: DcMotor = hardwareMap.dcMotor.get("fl")
    val fr: DcMotor = hardwareMap.dcMotor.get("fr")
    val bl: DcMotor = hardwareMap.dcMotor.get("bl")
    val br: DcMotor = hardwareMap.dcMotor.get("br")

    val intakes: Pair<List<DcMotor>, DcMotor>
    val outtake: DcMotor

    val grab: Servo
    val twist: Servo
    val pushers: Pair<Servo, CRServo>

    init {

        fl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        fr.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        bl.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        br.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        fl.mode = DcMotor.RunMode.RUN_USING_ENCODER
        fr.mode = DcMotor.RunMode.RUN_USING_ENCODER
        bl.mode = DcMotor.RunMode.RUN_USING_ENCODER
        br.mode = DcMotor.RunMode.RUN_USING_ENCODER

        fr.direction = DcMotorSimple.Direction.REVERSE
        br.direction = DcMotorSimple.Direction.REVERSE

        intakes = Pair(
            listOf(hardwareMap.dcMotor.get("i1"), hardwareMap.dcMotor.get("i2")), hardwareMap.dcMotor.get("i3")
        )
        outtake = hardwareMap.dcMotor.get("shoot")

        grab = hardwareMap.servo.get("Gran")
        twist = hardwareMap.servo.get("twist")
        pushers = Pair(hardwareMap.servo.get("p1"), hardwareMap.get(CRServo::class.java, "p2"))
    }

    enum class Constants {
        INTAKE1_ON, INTAKE2_ON, OUTTAKE_ON
    }

    operator fun get(id: Constants): Any? = when (id) {
        Constants.INTAKE1_ON -> -1.0
        Constants.INTAKE2_ON -> 1.0
        Constants.OUTTAKE_ON -> 1.0
    }

    private var _speed: Speed = Speed(0.0, 0.0, 0.0)

    var speed: Speed
        get() = _speed
        set(speed) {
            _speed.horizontal = speed.horizontal ?: _speed.horizontal
            _speed.vertical = speed.vertical ?: _speed.vertical
            _speed.rotation = speed.rotation ?: _speed.rotation
        }

    var powers: List<Double> = listOf(0.0, 0.0, 0.0, 0.0)

    fun zoom(movementType: MovementType) {
        val vals = if (movementType == MovementType.MECANUM) List(4) {
            when (it) {
                0 -> speed.vertical!! + speed.horizontal!! + speed.rotation!!
                1 -> speed.vertical!! - speed.horizontal!! - speed.rotation!!
                2 -> speed.vertical!! - speed.horizontal!! + speed.rotation!!
                3 -> speed.vertical!! + speed.horizontal!! - speed.rotation!!
                else -> 0.0
            }
        } else List(4) {
            when (it) {
                0 -> speed.vertical!! + 2.0.pow(speed.horizontal!!) - 1
                1 -> speed.vertical!! - 2.0.pow(speed.horizontal!!) - 1
                2 -> speed.vertical!! + 2.0.pow(speed.horizontal!!) - 1
                3 -> speed.vertical!! - 2.0.pow(speed.horizontal!!) - 1
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

        powers = adjustedVals

        fl.power = powers[0]
        fr.power = powers[1]
        bl.power = powers[2]
        br.power = powers[3]
    }

    override fun drive(direction: Vector, rotationSpeed: Double) {
        TODO("Not yet implemented")
    }
}


fun main() {
}