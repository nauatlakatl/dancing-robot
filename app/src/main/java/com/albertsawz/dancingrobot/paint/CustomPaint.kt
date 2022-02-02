package com.albertsawz.dancingrobot.paint

import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt

class CustomPaint(@ColorInt customColor: Int) : Paint() {

    init {
        isAntiAlias = true
        strokeWidth = 2f
        style = Style.STROKE
        color = customColor
    }
}