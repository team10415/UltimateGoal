package com.team10415.autonopro.path

abstract class PathSegment {
    abstract val createTrajectory: () -> Trajectory
}