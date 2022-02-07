package com.albertsawz.dancingrobot.cube

import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.ColorInt
import com.albertsawz.dancingrobot.Coordinate
import com.albertsawz.dancingrobot.paint.CustomPaint
import com.albertsawz.dancingrobot.utils.getIdentityMatrix
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Cube(@ColorInt private val color: Int = Color.BLACK) {

    private val vertices = ArrayList<Coordinate>(8)
    private var paint: CustomPaint

    init {
        // Coordinates of the cube at its initial state
        vertices.add(Coordinate(-1.0, -1.0, -1.0))
        vertices.add(Coordinate(-1.0, -1.0, 1.0))
        vertices.add(Coordinate(-1.0, 1.0, -1.0))
        vertices.add(Coordinate(-1.0, 1.0, 1.0))
        vertices.add(Coordinate(1.0, -1.0, -1.0))
        vertices.add(Coordinate(1.0, -1.0, 1.0))
        vertices.add(Coordinate(1.0, 1.0, -1.0))
        vertices.add(Coordinate(1.0, 1.0, 1.0))

        paint = CustomPaint(color)
    }

    /**
     * Defines the transformation matrix used to translate a coordinate and applies it to a set of
     * coordinates.
     * @param tx Translation along the x-axis
     * @param ty Translation along the y-axis
     * @param tz Translation along the z-axis
     * @return Coordinates whose values were applied the translation matrix
     */
    fun translate(
        tx: Double,
        ty: Double,
        tz: Double
    ) {
        val transformationMatrix = getIdentityMatrix()

        transformationMatrix[3] = tx
        transformationMatrix[7] = ty
        transformationMatrix[11] = tz

        transform(transformationMatrix)
    }

    /**
     * Defines the transformation matrix used to scale a coordinate and applies it to a set of
     * coordinates.
     * @param sx Scaling for the x-axis
     * @param sy Scaling for the y-axis
     * @param sz Scaling for the z-axis
     * @return Coordinates whose values were applied the scaling matrix
     */
    fun scale(
        sx: Double,
        sy: Double,
        sz: Double
    ) {
        val transformationMatrix = getIdentityMatrix()

        transformationMatrix[0] = sx
        transformationMatrix[5] = sy
        transformationMatrix[10] = sy

        transform(transformationMatrix)
    }

    /**
     * Defines the transformation matrix used to rotate a coordinate by means of quaternions and
     * applies it to a set of coordinates.
     * @param angle The angle which coordinates will rotate
     * @param rotationAxis Rotation axis around which coordinates will rotate
     * @return Coordinates whose values were applied the rotation matrix
     */
    fun rotate(
        angle: Double,
        rotationAxis: IntArray
    ) {
        val transformationMatrix = getIdentityMatrix()
        val radian = Math.toRadians(angle)

        val w: Double = cos(radian / 2)
        val x: Double = sin(radian / 2) * rotationAxis[0]
        val y: Double = sin(radian / 2) * rotationAxis[1]
        val z: Double = sin(radian / 2) * rotationAxis[2]

        transformationMatrix[0] = w.pow(2) + x.pow(2) - y.pow(2) - z.pow(2)
        transformationMatrix[1] = 2 * x * y - 2 * w * z
        transformationMatrix[2] = 2 * x * z + 2 * w * y
        transformationMatrix[3] = 0.0
        transformationMatrix[4] = 2 * x * y + 2 * w * z
        transformationMatrix[5] = w.pow(2) + y.pow(2) - x.pow(2) - z.pow(2)
        transformationMatrix[6] = 2 * y * z - 2 * w * x
        transformationMatrix[7] = 0.0
        transformationMatrix[8] = 2 * x * z - 2 * w * y
        transformationMatrix[9] = 2 * y * z + 2 * w * x
        transformationMatrix[10] = w.pow(2) + z.pow(2) - x.pow(2) - y.pow(2)
        transformationMatrix[11] = 0.0
        transformationMatrix[12] = 0.0
        transformationMatrix[13] = 0.0
        transformationMatrix[14] = 0.0
        transformationMatrix[15] = 0.0
        transform(transformationMatrix)
    }

    /**
     * Applies a transformation matrix for a set of coordinates, individually.
     * @param transformationMatrix ArrayList that defines the transformation matrix to be applied
     * to coordinates
     * @return Coordinates which were transformed
     */
    private fun transform(
        transformationMatrix: ArrayList<Double>
    ) {
        vertices.forEachIndexed { index, coordinate ->
            val newCoordinate = transform(coordinate, transformationMatrix)
            newCoordinate.normalise()
            vertices[index] = newCoordinate
        }
    }

    /**
     * Multiplies a coordinate (vector) by a transformation matrix.
     * @param coordinate Coordinate whose values will be modified
     * @param matrix Transformation matrix to transform the coordinate
     * @return Coordinate transformed by the transformation matrix
     */
    private fun transform(coordinate: Coordinate, matrix: ArrayList<Double>): Coordinate {
        val result = Coordinate()
        result.x =
            (matrix[0] * coordinate.x) + (matrix[1] * coordinate.y) + (matrix[2] * coordinate.z) + (matrix[3] * coordinate.w)
        result.y =
            (matrix[4] * coordinate.x) + (matrix[5] * coordinate.y) + (matrix[6] * coordinate.z) + (matrix[7] * coordinate.w)
        result.z =
            (matrix[8] * coordinate.x) + (matrix[9] * coordinate.y) + (matrix[10] * coordinate.z) + (matrix[11] * coordinate.w)
        result.w =
            (matrix[12] * coordinate.x) + (matrix[13] * coordinate.y) + (matrix[14] * coordinate.z) + (matrix[15] * coordinate.w)
        return result
    }

    /**
     * Sets up the way edges of the cube will be drawn.
     */
    fun draw(canvas: Canvas) {
        drawLinePairs(canvas, 0, 1)
        drawLinePairs(canvas, 1, 3)
        drawLinePairs(canvas, 3, 2)
        drawLinePairs(canvas, 2, 0)
        drawLinePairs(canvas, 4, 5)
        drawLinePairs(canvas, 5, 7)
        drawLinePairs(canvas, 7, 6)
        drawLinePairs(canvas, 6, 4)
        drawLinePairs(canvas, 0, 4)
        drawLinePairs(canvas, 1, 5)
        drawLinePairs(canvas, 2, 6)
        drawLinePairs(canvas, 3, 7)
    }

    /**
     * Draws the edge that joins two coordinates.
     * @param canvas Object to draw
     * @param start Index of the first coordinate
     * @param end Index of the second coordinate
     */
    private fun drawLinePairs(
        canvas: Canvas?,
        start: Int,
        end: Int,
    ) {
        canvas?.drawLine(
            vertices[start].x.toFloat(),
            vertices[start].y.toFloat(),
            vertices[end].x.toFloat(),
            vertices[end].y.toFloat(),
            paint
        )
    }
}