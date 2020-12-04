package org.firstinspires.ftc.teamcode.opmodes.teleop

import android.util.Log
import com.qualcomm.hardware.bosch.BNO055IMU
import com.team10415.ftc_recorder.Controller
import org.firstinspires.ftc.teamcode.autonopro.localization.Odometry
import org.firstinspires.ftc.teamcode.autonopro.localization.OdometryConsts
import org.firstinspires.ftc.teamcode.autonopro.util.NanoClock
import org.firstinspires.ftc.teamcode.util.*

class TeleOpController : Controller() {
    lateinit var drivetrain: DumbDrive
    lateinit var odometry: Odometry

    var gamepads: List<Gamepad>? = null

    interface States {
        var intake1: Boolean
        var intake2: Boolean
        var outtake: Boolean
        var twist: Boolean
        var grab: Boolean
        var p1: Boolean
    }

    val states = object : States {
        override var intake1 = false
        override var intake2 = false
        override var outtake = false
        override var twist = false
        override var grab = false
        override var p1 = false
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

        drivetrain.twist.position = .5
        drivetrain.grab.position = -1.0
    }

    override fun loop() {
        if (gamepads == null) assignClickListeners()
        gamepads!!.forEach { it.activateClicks() }

        Log.i("gamepad", gamepad1.left_stick_y.toString())
        Log.i("gamepad2", gamepads!![0].sticks.left.y.toString())

        drivetrain.apply {
            speed = Speed(gamepads!![0].sticks.left.x / 2, -gamepads!![0].sticks.left.y, gamepads!![0].sticks.right.x)
            zoom(MovementType.MECANUM)
            telemetry.addLine(powers.toString())
            pushers.second.power = -gamepads!![1].triggers.right
            drivetrain.outtake.power = if (gamepads!![1].buttons.y) 1.0 else .0
        }

        odometry.apply {
            update()
        }

    }

    fun assignClickListeners() {
        gamepads = listOf(Gamepad(gamepad1), Gamepad(gamepad2))

        gamepads!![1].onClick(BenignButtons.A) {
            states.intake1 = !states.intake1
            drivetrain.intakes.first.forEach {
                it.power = if (states.intake1) (drivetrain[DumbDrive.Constants.INTAKE1_ON] as Double) else 0.0
            }
            states.intake2 = !states.intake2
            drivetrain.intakes.second.also { // use .also for consistent code style
                it.power = if (states.intake2) (drivetrain[DumbDrive.Constants.INTAKE2_ON] as Double) else 0.0
            }
        }
        gamepads!![1].onClick(BenignButtons.B) {
            drivetrain.speed = Speed(.0, .0, .0)
            drivetrain.zoom(MovementType.MECANUM)
            var time = NanoClock.system().seconds()
            drivetrain.outtake.power = .95
            drivetrain.pushers.first.position = 1.0
            while (NanoClock.system().seconds() - time < .5);
            time = NanoClock.system().seconds()

            drivetrain.pushers.second.power = -1.0
            while (NanoClock.system().seconds() - time < .25);
            time = NanoClock.system().seconds()

            drivetrain.pushers.first.position = -1.0
            while (NanoClock.system().seconds() - time < .5);
            time = NanoClock.system().seconds()

            drivetrain.outtake.power = .0
            while (NanoClock.system().seconds() - time < .85);
            time = NanoClock.system().seconds()

            drivetrain.pushers.second.power = .0
            while (NanoClock.system().seconds() - time < .5);
        }
        gamepads!![1].onClick(BenignButtons.Y) {
            drivetrain.speed = Speed(.0, .0, .0)
            drivetrain.zoom(MovementType.MECANUM)
            var time = NanoClock.system().seconds()
            drivetrain.outtake.power = 0.92
            drivetrain.pushers.first.position = 1.0
            while (NanoClock.system().seconds() - time < .5);
            time = NanoClock.system().seconds()

            drivetrain.pushers.second.power = -1.0
            while (NanoClock.system().seconds() - time < .25);
            time = NanoClock.system().seconds()

            drivetrain.pushers.first.position = -1.0
            while (NanoClock.system().seconds() - time < .5);
            time = NanoClock.system().seconds()

            drivetrain.outtake.power = .0
            while (NanoClock.system().seconds() - time < .85);
            time = NanoClock.system().seconds()

            drivetrain.pushers.second.power = .0
            while (NanoClock.system().seconds() - time < .5);
        }
        gamepads!![1].onClick(BenignButtons.X) {

        }
        gamepads!![1].onClick(BenignButtons.DPAD_UP) {
            states.grab = !states.grab
            drivetrain.grab.position = if (!states.grab) 1.0 else -1.0
        }
        gamepads!![1].onClick(BenignButtons.DPAD_DOWN) {
            states.twist = !states.twist
            drivetrain.twist.position = if (!states.twist) .5 else -.5
        }
    }
}