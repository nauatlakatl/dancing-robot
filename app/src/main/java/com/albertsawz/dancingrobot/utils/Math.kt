package com.albertsawz.dancingrobot.utils

/**
 * Defines and returns an identity matrix (square matrix with ones on the main diagonal and
 * zeros elsewhere.
 */
fun getIdentityMatrix(): ArrayList<Double> {
    val matrix = ArrayList<Double>()
    matrix.add(1.0)
    matrix.add(0.0)
    matrix.add(0.0)
    matrix.add(0.0)

    matrix.add(0.0)
    matrix.add(1.0)
    matrix.add(0.0)
    matrix.add(0.0)

    matrix.add(0.0)
    matrix.add(0.0)
    matrix.add(1.0)
    matrix.add(0.0)

    matrix.add(0.0)
    matrix.add(0.0)
    matrix.add(0.0)
    matrix.add(1.0)
    return matrix
}