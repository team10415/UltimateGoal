package org.firstinspires.ftc.teamcode.autonopro.localization

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.autonopro.util.Angle
import org.firstinspires.ftc.teamcode.autonopro.util.Pose
import org.firstinspires.ftc.teamcode.autonopro.util.Transformation
import org.firstinspires.ftc.teamcode.autonopro.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class Odometry(
    val left: DcMotor,
    val right: DcMotor,
    val aux: DcMotor,
    val imu: BNO055IMU,
    val consts: OdometryConsts
) : Localizer {
    override val velocity: Vector
        get() = TODO("Not yet implemented")
    override lateinit var currentPose: Pose

    private var values: OdometerValues

    init {
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS
        imu.initialize(parameters)
        values = OdometerValues(
            left.currentPosition.toDouble() * consts.TICKS_PER_INCH,
            right.currentPosition.toDouble() * consts.TICKS_PER_INCH,
            aux.currentPosition.toDouble() * consts.TICKS_PER_INCH,
            Angle(imu.angularOrientation.firstAngle.toDouble())
        )
        currentPose = Pose(.0, .0, Angle(imu.angularOrientation.firstAngle.toDouble()))
    }

    override fun update() {
        val nValues = OdometerValues(
            left.currentPosition.toDouble() * consts.TICKS_PER_INCH,
            right.currentPosition.toDouble() * consts.TICKS_PER_INCH,
            aux.currentPosition.toDouble() * consts.TICKS_PER_INCH,
            Angle(imu.angularOrientation.firstAngle.toDouble())
        )
        val dValues = nValues - values
        values = nValues

        val dHorizontal: Double
        val dVertical: Double
        if (dValues.heading.approxEquals(Angle(.0))) {
            dHorizontal = dValues.aux
            dVertical = (dValues.left + dValues.right) / 2
        } else {
            val rt = (consts.ROBOT_WIDTH / 2) * consts.TICKS_PER_INCH * (dValues.left + dValues.right) /
                    (dValues.right - dValues.left)
            val rs = (dValues.aux / dValues.heading.radians)
            dHorizontal = rt * (cos(dValues.heading.radians) - 1) + rs * sin(dValues.heading.radians)
            dVertical = rt * sin(dValues.heading.radians) + rs * (1 - cos(dValues.heading.radians))
        }

        val transformation = Transformation(
            dHorizontal * cos(values.heading.radians) + dVertical * sin(values.heading.radians),
            dHorizontal * sin(values.heading.radians) + dVertical * cos(values.heading.radians),
            dValues.heading
        )
        currentPose += transformation
    }
}

data class OdometryConsts(
    val ROBOT_WIDTH: Double,
    val ROBOT_LENGTH: Double,
    val TICKS_PER_INCH: Int
)

private class OdometerValues(
    var left: Double,
    var right: Double,
    var aux: Double,
    var heading: Angle
) {
    operator fun minus(values: OdometerValues) = OdometerValues(
        left - values.left,
        right - values.right,
        aux - values.aux,
        heading - values.heading
    )
}