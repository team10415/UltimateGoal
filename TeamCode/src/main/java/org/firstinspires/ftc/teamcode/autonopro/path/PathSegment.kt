package org.firstinspires.ftc.teamcode.autonopro.path

abstract class PathSegment {
    abstract val createTrajectory: () -> Trajectory
}