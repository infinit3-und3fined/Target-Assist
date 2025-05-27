package com.example.targetassit.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DpiGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val gridPaint = Paint().apply {
        color = Color.WHITE
        alpha = 100
        strokeWidth = 1f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val centerPaint = Paint().apply {
        color = Color.RED
        alpha = 180
        strokeWidth = 2f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    var dpi: Int = 360
        set(value) {
            field = value
            invalidate()
        }

    var gridSize: Int = 200
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2
        val centerY = height / 2

        // Calculate grid spacing based on DPI
        // For visualization purposes, we'll scale the DPI to make the grid visible
        val scaleFactor = 0.05f  // Adjust this to change grid density
        val spacing = (dpi * scaleFactor).coerceAtLeast(5f)

        // Draw vertical lines
        var x = centerX
        while (x < width) {
            canvas.drawLine(x, 0f, x, height, gridPaint)
            x += spacing
        }
        x = centerX - spacing
        while (x >= 0) {
            canvas.drawLine(x, 0f, x, height, gridPaint)
            x -= spacing
        }

        // Draw horizontal lines
        var y = centerY
        while (y < height) {
            canvas.drawLine(0f, y, width, y, gridPaint)
            y += spacing
        }
        y = centerY - spacing
        while (y >= 0) {
            canvas.drawLine(0f, y, width, y, gridPaint)
            y -= spacing
        }

        // Draw center crosshair
        canvas.drawLine(centerX - 20, centerY, centerX + 20, centerY, centerPaint)
        canvas.drawLine(centerX, centerY - 20, centerX, centerY + 20, centerPaint)
        canvas.drawCircle(centerX, centerY, 10f, centerPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = gridSize
        setMeasuredDimension(size, size)
    }
} 