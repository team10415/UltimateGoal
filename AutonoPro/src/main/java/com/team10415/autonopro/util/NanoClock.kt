package com.team10415.autonopro.util;

// graciously stolen from NanoClock :)
abstract class NanoClock {

    companion object {
        /**
         * Returns a [NanoClock] backed by [System.nanoTime].
         */
        @JvmStatic
        fun system() = object : NanoClock() {
            override fun seconds() = System.nanoTime() / 1e9
        }
    }

    /**
     * Returns the number of seconds since an arbitrary (yet consistent) origin.
     */
    abstract fun seconds(): Double
}