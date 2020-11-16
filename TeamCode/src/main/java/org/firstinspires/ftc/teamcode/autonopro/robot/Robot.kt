package org.firstinspires.ftc.teamcode.autonopro.robot

import org.firstinspires.ftc.teamcode.autonopro.localization.Localizer
import org.firstinspires.ftc.teamcode.autonopro.path.Route

class Robot(drivetrain: Drivetrain, val localizer: Localizer) : Drivetrain by drivetrain {
    private val routes = mutableListOf<Route>()

    fun addRoute(route: Route.Builder) {
        route.pose = {
            localizer.update()
            localizer.currentPose
        }
        routes += route.build()
    }
}