package org.firstinspires.ftc.teamcode.autonopro.path

import org.firstinspires.ftc.teamcode.autonopro.util.Pose
import org.firstinspires.ftc.teamcode.autonopro.util.Transformation
import org.firstinspires.ftc.teamcode.autonopro.util.Vector

data class Path(var id: Any) {
    var segments: MutableList<PathSegment> = mutableListOf()
        private set

    fun add(line: Line) = segments.add(line)
    fun add(transformation: Transformation) = segments.add(transformation.toLine())
    fun add(vector: Vector) = segments.add(Line(vector))
    fun add(curve: BezierCurve) = segments.add(curve)
    fun add(pose: Pose) = segments.add(ToPose(pose))

    operator fun plus(line: Line) = copy().apply { this.add(line) }
    operator fun plus(transformation: Transformation) = copy().apply { this.add(transformation) }
    operator fun plus(vector: Vector) = copy().apply { this.add(vector) }
    operator fun plus(curve: BezierCurve) = copy().apply { this.add(curve) }
    operator fun plus(pose: Pose) = copy().apply { this.add(pose) }

    operator fun plusAssign(line: Line) = Unit.also { add(line) }
    operator fun plusAssign(transformation: Transformation) = Unit.also { add(transformation) }
    operator fun plusAssign(vector: Vector) = Unit.also { add(vector) }
    operator fun plusAssign(curve: BezierCurve) = Unit.also { add(curve) }
    operator fun plusAssign(pose: Pose) = Unit.also { add(pose) }
}
