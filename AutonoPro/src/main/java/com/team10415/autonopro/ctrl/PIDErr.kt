package com.team10415.autonopro.ctrl

interface PIDErr {
    var prev: Double
    var cur: Double
    var total: Double
}