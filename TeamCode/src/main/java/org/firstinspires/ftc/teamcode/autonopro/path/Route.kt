package org.firstinspires.ftc.teamcode.autonopro.path

import org.firstinspires.ftc.teamcode.autonopro.util.Angle
import org.firstinspires.ftc.teamcode.autonopro.util.NanoClock
import org.firstinspires.ftc.teamcode.autonopro.util.Pose

class Route(val path: Path) {
    private val id = path.id

    private val clock: NanoClock = NanoClock.system()

    private val segments = path.segments.toList()
    private var segmentIndex = 0

    private lateinit var pose: () -> Pose

    fun execPath(onFinished: ((Route) -> Unit)?): () -> Angle {
        var segment = segments[segmentIndex]
        var trajectory = segment.createTrajectory()
        trajectory.origin = pose()

        val startTimestamp = Double.NaN

        val executor = executor@{
            val pose = pose()
            if (pose.copy(r = trajectory.endPos.r).approxEquals(trajectory.endPos)) {
                if ((trajectory.endPos.r ?: pose.r!!).approxEquals(pose.r!!)) {
                    segmentIndex++
                    segment = segments[segmentIndex]
                    trajectory = segment.createTrajectory()
                    trajectory.origin = pose
                    return@executor Angle(0.0)
                } else {
                    // rotate until finished rotation
                }
            }

            val dTimestamp = clock.seconds() - startTimestamp
            val dRotation = pose.r!!.radians - (trajectory.endPos.r ?: pose.r).radians
            val rSpeed = dRotation / dTimestamp

            val progress = trajectory.estimateProgress(pose - trajectory.origin)
            val progressPerSecond = progress / dTimestamp

            trajectory.calculate(pose)
        }

        return executor
    }

    class Builder {
        lateinit var path: Path
        lateinit var pose: () -> Pose

        val build: () -> Route = {
            val route = Route(path)
            route.pose = pose
            route
        }
    }
}