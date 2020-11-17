package org.firstinspires.ftc.teamcode.opmodes.teleop

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.autonopro.localization.Odometry
import org.firstinspires.ftc.teamcode.autonopro.localization.OdometryConsts
import org.firstinspires.ftc.teamcode.util.*

@TeleOp
class Teleop : OpMode() {
    lateinit var drivetrain: DumbDrive
    lateinit var odometry: Odometry

    var gamepads: List<Gamepad>? = null

    interface States {
        var intake1: Boolean
        var intake2: Boolean
        var outtake: Boolean
    }

    val states = object : States {
        override var intake1 = false
        override var intake2 = false
        override var outtake = false
    }

    override fun init() {
        /* CREATE DRIVETRAIN */
        drivetrain = DumbDrive(hardwareMap)
        odometry = Odometry(
            drivetrain.bl, drivetrain.br, drivetrain.fr,
            hardwareMap.get(BNO055IMU::class.java, "imu"),
            OdometryConsts(
                ROBOT_WIDTH = 18.0,
                ROBOT_LENGTH = 18.0,
                TICKS_PER_INCH = 8
            )
        )
    }

    override fun loop() {
        if (gamepads == null) assignClickListeners()
        gamepads!!.forEach { it.activateClicks() }

        drivetrain.apply {
            speed = Speed(gamepads!![0].sticks.left.x, gamepads!![0].sticks.left.y, gamepads!![0].sticks.right.x)
            zoom(MovementType.MECANUM)
            telemetry.addLine(powers.toString())

        }

        odometry.apply {
            update()
            telemetry.addLine(currentPose.toString())
        }

    }

    fun assignClickListeners() {
        gamepads = listOf(Gamepad(gamepad1), Gamepad(gamepad2))

        gamepads!![1].onClick(BenignButtons.A) {
            states.intake1 = !states.intake1
            drivetrain.intakes.first.forEach {
                it.power = if (states.intake1) (drivetrain[DumbDrive.Constants.INTAKE1_ON] as Double) else 0.0
            }
        }
        gamepads!![1].onClick(BenignButtons.X) {
            states.intake2 = !states.intake2
            drivetrain.intakes.second.also { // use .also for consistent code style
                it.power = if (states.intake2) (drivetrain[DumbDrive.Constants.INTAKE2_ON] as Double) else 0.0
            }
        }
        gamepads!![1].onClick(BenignButtons.Y) {
            states.outtake = !states.outtake
            drivetrain.outtake.also { // use .also for consistent code style
                it.power = if (states.intake2) (drivetrain[DumbDrive.Constants.OUTTAKE_ON] as Double) else 0.0
            }
        }
    }
}