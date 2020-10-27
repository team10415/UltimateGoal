package com.team10415.autonopro.path

import com.team10415.autonopro.localization.Position
import com.team10415.autonopro.math.Angle

abstract class Trajectory {
    abstract var origin: Position
    abstract val endPos: Position

    abstract val estimateProgress: (transformation: Transformation) -> Double
    abstract fun calculate(position: Position): Angle
}