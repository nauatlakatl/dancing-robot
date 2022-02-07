package com.albertsawz.dancingrobot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import com.albertsawz.dancingrobot.cube.Cube

class Background(context: Context) : View(context) {

    private val cube = Cube(Color.RED)

    init {
        cube.translate(2.0, 2.0, 0.0)
        cube.scale(40.0, 40.0, 40.0)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        cube.draw(canvas!!)
    }
}