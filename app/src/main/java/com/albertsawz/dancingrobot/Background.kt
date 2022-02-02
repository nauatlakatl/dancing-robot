package com.albertsawz.dancingrobot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.albertsawz.dancingrobot.paint.CustomPaint
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Background(context: Context) : View(context) {

    // Coordinates of a cube at its initial state
    private val cubeVertices = ArrayList<Coordinate>(8)

    // Coordinates of a cube after applying transformations on them
    private var drawCubeVertices = ArrayList<Coordinate>()

    private val paint = CustomPaint(Color.MAGENTA)

    init {
        cubeVertices.add(Coordinate(-1.0, -1.0, -1.0))
        cubeVertices.add(Coordinate(-1.0, -1.0, 1.0))
        cubeVertices.add(Coordinate(-1.0, 1.0, -1.0))
        cubeVertices.add(Coordinate(-1.0, 1.0, 1.0))
        cubeVertices.add(Coordinate(1.0, -1.0, -1.0))
        cubeVertices.add(Coordinate(1.0, -1.0, 1.0))
        cubeVertices.add(Coordinate(1.0, 1.0, -1.0))
        cubeVertices.add(Coordinate(1.0, 1.0, 1.0))

        drawCubeVertices = translate(cubeVertices, 12.0, 2.0, 2.0)
        drawCubeVertices = scale(drawCubeVertices, 40.0, 40.0, 40.0)

        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCube(canvas)
    }

    /**
     * Defines the transformation matrix used to translate a coordinate and applies it to a set of
     * coordinates.
     * @param coordinates Coordinates which will be translated individually
     * @param tx Translation along the x-axis
     * @param ty Translation along the y-axis
     * @param tz Translation along the z-axis
     * @return Coordinates whose values were applied the translation matrix
     */
    private fun translate(
        coordinates: ArrayList<Coordinate>,
        tx: Double,
        ty: Double,
        tz: Double
    ): ArrayList<Coordinate> {
        val transformationMatrix = getIdentityMatrix()

        transformationMatrix[3] = tx
        transformationMatrix[7] = ty
        transformationMatrix[11] = tz

        return transform(coordinates, transformationMatrix)
    }

    /**
     * Defines the transformation matrix used to scale a coordinate and applies it to a set of
     * coordinates.
     * @param coordinates Coordinates which will be translated individually
     * @param sx Scaling for the x-axis
     * @param sy Scaling for the y-axis
     * @param sz Scaling for the z-axis
     * @return Coordinates whose values were applied the scaling matrix
     */
    private fun scale(
        coordinates: ArrayList<Coordinate>,
        sx: Double,
        sy: Double,
        sz: Double
    ): ArrayList<Coordinate> {
        val transformationMatrix = getIdentityMatrix()

        transformationMatrix[0] = sx
        transformationMatrix[5] = sy
        transformationMatrix[10] = sy

        return transform(coordinates, transformationMatrix)
    }

    /**
     * Defines the transformation matrix used to rotate a coordinate by means of quaternions and
     * applies it to a set of coordinates.
     * @param coordinates Coordinates which will be translated individually
     * @param angle The angle which coordinates will rotate
     * @param rotationAxis Rotation axis around which coordinates will rotate
     * @return Coordinates whose values were applied the rotation matrix
     */
    private fun rotate(
        coordinates: ArrayList<Coordinate>,
        angle: Double,
        rotationAxis: IntArray
    ): ArrayList<Coordinate> {
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
        return transform(coordinates, transformationMatrix)
    }

    /**
     * Applies a transformation matrix for a set of coordinates, individually.
     * @param coordinates Coordinates which will be transformed
     * @param transformationMatrix ArrayList that defines the transformation matrix to be applied
     * to coordinates
     * @return Coordinates which were transformed
     */
    private fun transform(
        coordinates: ArrayList<Coordinate>,
        transformationMatrix: ArrayList<Double>
    ): ArrayList<Coordinate> {
        val result = ArrayList<Coordinate>()
        for (coordinate in coordinates) {
            val transformedCoordinate = transform(coordinate, transformationMatrix)
            transformedCoordinate.normalise()
            result.add(transformedCoordinate)
        }
        return result
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
     * Defines and returns an identity matrix (square matrix with ones on the main diagonal and
     * zeros elsewhere.
     */
    private fun getIdentityMatrix(): ArrayList<Double> {
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

    /**
     * Sets up the way edges of the cube will be drawn.
     */
    private fun drawCube(canvas: Canvas?) {
        drawLinePairs(canvas, drawCubeVertices, 0, 1, paint)
        drawLinePairs(canvas, drawCubeVertices, 1, 3, paint)
        drawLinePairs(canvas, drawCubeVertices, 3, 2, paint)
        drawLinePairs(canvas, drawCubeVertices, 2, 0, paint)
        drawLinePairs(canvas, drawCubeVertices, 4, 5, paint)
        drawLinePairs(canvas, drawCubeVertices, 5, 7, paint)
        drawLinePairs(canvas, drawCubeVertices, 7, 6, paint)
        drawLinePairs(canvas, drawCubeVertices, 6, 4, paint)
        drawLinePairs(canvas, drawCubeVertices, 0, 4, paint)
        drawLinePairs(canvas, drawCubeVertices, 1, 5, paint)
        drawLinePairs(canvas, drawCubeVertices, 2, 6, paint)
        drawLinePairs(canvas, drawCubeVertices, 3, 7, paint)
    }

    /**
     * Draws the edge that joins two coordinates.
     * @param canvas Object to draw
     * @param vertices Coordinates of the whole cube
     * @param start Index of the first coordinate
     * @param end Index of the second coordinate
     * @param paint Describes the color of the edge
     */
    private fun drawLinePairs(
        canvas: Canvas?,
        vertices: ArrayList<Coordinate>,
        start: Int,
        end: Int,
        paint: Paint
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