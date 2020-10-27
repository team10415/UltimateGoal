package com.team10415.autonopro.localization

import com.team10415.autonopro.math.Angle
import com.team10415.autonopro.path.Transformation
import kotlin.math.cos
import kotlin.math.sin

class OdoLoco(var consts: OdometryConsts, val odometry: Odometry) {
    var position = Position(0.0, 0.0, Angle(0.0))
        private set

    private var prevValues = Triple(0, 0, 0)

    fun update(angle: Angle? = null) {
        val left = (odometry.values.first - prevValues.first) * consts.ONE_ROTATION_TICKS
        val right = (odometry.values.second - prevValues.second) * consts.ONE_ROTATION_TICKS
        val aux = (odometry.values.third - prevValues.third) * consts.ONE_ROTATION_TICKS
        prevValues = odometry.values.copy()

        val horizontal = ((left + right) * 2.0 * Math.PI * consts.WHEEL_RADIUS) / 2.0
        val vertical = aux * 2.0 * Math.PI * consts.WHEEL_RADIUS

        val r = angle ?: (position.r ?: Angle(0.0)) + theta(left, right)
        val x = horizontal * sin(r.radians) + vertical * cos(r.radians)
        val y = horizontal * cos(r.radians) + vertical * sin(r.radians)

        position += Transformation(x, y, r)
    }

    private val theta = { l: Double, r: Double -> Angle((l - r) / consts.WHEEL_DISTANCE_APART) }
}
