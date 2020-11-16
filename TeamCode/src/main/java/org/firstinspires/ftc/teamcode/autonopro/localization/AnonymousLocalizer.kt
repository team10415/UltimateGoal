package org.firstinspires.ftc.teamcode.autonopro.localization

import com.qualcomm.hardware.bosch.BNO055IMU
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.autonopro.util.*
import org.firstinspires.ftc.teamcode.util.math.round
import kotlin.math.cos
import kotlin.math.sin

class AnonymousLocalizer(val imu: BNO055IMU, val adjustment: Transformation) {
    init {
        val parameters = BNO055IMU.Parameters()
        parameters.mode = BNO055IMU.SensorMode.IMU
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json" // see the calibration sample opmode
        imu.initialize(parameters)
    }

    private var prevTime = NanoClock.system().seconds()
    fun resetClock() = Unit.also { prevTime = NanoClock.system().seconds() }

    private var rawVelocity: Vector? = null // from REV hub
    var velocity: Vector? = null
        private set
    var currentPose = Pose(.0, .0)

    var x: Double = .0
    var y: Double = .0

    fun update(telemetry: Telemetry) {
        val heading = Angle(imu.angularOrientation.firstAngle.toDouble())
        val rawAccel = imu.linearAcceleration // from REV hub

        val time = NanoClock.system().seconds()
        val deltaTime = time - prevTime
        prevTime = time

        /* RELATIVE TO FIELD */
        val xAccel = (rawAccel.xAccel * cos(heading.radians) + rawAccel.yAccel * sin(heading.radians)).round(1)
        val yAccel = (rawAccel.xAccel * sin(heading.radians) + rawAccel.yAccel * cos(heading.radians)).round(1)
        val newVelocity = Vector((xAccel * deltaTime), (yAccel * deltaTime))
        rawVelocity = if (rawVelocity != null) rawVelocity!! + newVelocity else newVelocity

        x += (xAccel * deltaTime)
        y += (yAccel * deltaTime)

        telemetry.addData("a", "${xAccel}, ${yAccel}")
        telemetry.addData("time", deltaTime)
        telemetry.addData("rawVelocity", "${(xAccel * deltaTime).round(3)}, ${(yAccel * deltaTime).round(3)}")
        telemetry.addData("rawVelocity", "${(rawVelocity!!.horizontal).round(3)}, ${(rawVelocity!!.vertical).round(3)}")
        telemetry.addData("theta", Pose.theta(xAccel * deltaTime, yAccel * deltaTime).toString())

        // s = vi*t + 1/2*a*t^2
        /*var xPos = velocity.horizontal * deltaTime + (xAccel * deltaTime.pow(2)) / 2
        var yPos = velocity.vertical * deltaTime + (yAccel * deltaTime.pow(2)) / 2

        /* ADJUST FOR POSITION OF REV HUB */
        xPos -= adjustment.x * cos(heading.radians) + adjustment.y * sin(heading.radians)
        yPos -= adjustment.x * sin(heading.radians) + adjustment.y * cos(heading.radians)

        val newPose = Pose(xPos, yPos, heading)
        velocity = Vector((newPose.x - currentPose.x) / deltaTime, (newPose.y - currentPose.y) / deltaTime)
        currentPose = newPose*/
    }
}