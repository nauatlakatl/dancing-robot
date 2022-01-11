package com.albertsawz.dancingrobot

/**
 * Class that represents a coordinate in a 3D plane.
 */
class Coordinate(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var w: Double = 1.0
) {

    /**
     * Normalises coordinates.
     */
    fun normalise() {
        if (w != 0.0) {
            x /= w
            y /= w
            z /= w
        }
        w = 1.0
    }
}