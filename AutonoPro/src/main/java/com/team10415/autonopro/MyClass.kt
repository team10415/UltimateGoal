package com.team10415.autonopro

import com.team10415.autonopro.path.BezierCurve
import com.team10415.autonopro.path.Transformation
import kotlin.system.measureTimeMillis


class K {
    var i = 0

    fun k(): () -> Int {
        var j = i;
        return {
            j++
            j
        }
    }
}

fun main() {
    var c = BezierCurve(Transformation(0.0, 12.0), Transformation(12.0, 0.0), Transformation(12.0, 12.0))
    println(measureTimeMillis {
        println(c.progression(3.0, c.xFun))
        println(c.progression(2.0, c.yFun))
        println(listOf(c.progression(3.0, c.xFun), c.progression(2.0, c.yFun)).average())
    })

    var g = K()
    var j = g.k()
    println(j())
    g.i++
    println(j())
}