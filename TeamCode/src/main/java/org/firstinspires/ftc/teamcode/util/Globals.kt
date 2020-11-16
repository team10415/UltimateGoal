package org.firstinspires.ftc.teamcode.util

import org.firstinspires.ftc.robotcore.external.Telemetry

interface Globals {
    var telemetry: Telemetry?
}

object globals : Globals {
    override var telemetry: Telemetry? = null
}