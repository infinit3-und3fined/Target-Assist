package com.example.targetassit.ui.overlay

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.targetassit.R

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var initialX = 0f
    private var initialY = 0f
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    
    private val gridPaint = Paint().apply {
        color = Color.argb(80, 255, 255, 255)
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }
    
    private val circlePaint = Paint().apply {
        color = Color.argb(100, 0, 150, 255)
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }
    
    private val centerPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }
    
    private var gridDensity = 30 // Default grid density (DPI representation)
    private var isGridVisible = true
    private var isCenterMarkerVisible = true
    
    init {
        setBackgroundColor(Color.TRANSPARENT)
        setWillNotDraw(false)
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Draw grid if visible
        if (isGridVisible) {
            drawGrid(canvas)
        }
        
        // Draw center marker if visible
        if (isCenterMarkerVisible) {
            val centerX = width / 2f
            val centerY = height / 2f
            
            // Draw crosshair
            canvas.drawLine(centerX - 20, centerY, centerX + 20, centerY, centerPaint)
            canvas.drawLine(centerX, centerY - 20, centerX, centerY + 20, centerPaint)
            
            // Draw circle
            canvas.drawCircle(centerX, centerY, 30f, circlePaint)
        }
    }
    
    private fun drawGrid(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()
        
        // Calculate grid spacing based on density
        val spacing = gridDensity.toFloat()
        
        // Draw vertical lines
        var x = 0f
        while (x < width) {
            canvas.drawLine(x, 0f, x, height, gridPaint)
            x += spacing
        }
        
        // Draw horizontal lines
        var y = 0f
        while (y < height) {
            canvas.drawLine(0f, y, width, y, gridPaint)
            y += spacing
        }
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = this.x
                initialY = this.y
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                this.x = initialX + (event.rawX - initialTouchX)
                this.y = initialY + (event.rawY - initialTouchY)
                return true
            }
        }
        return super.onTouchEvent(event)
    }
    
    fun setGridDensity(density: Int) {
        gridDensity = density
        invalidate()
    }
    
    fun toggleGrid(visible: Boolean) {
        isGridVisible = visible
        invalidate()
    }
    
    fun toggleCenterMarker(visible: Boolean) {
        isCenterMarkerVisible = visible
        invalidate()
    }
} 