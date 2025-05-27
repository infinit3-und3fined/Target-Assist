package com.example.targetassit.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.targetassit.ui.theme.PrimaryBlue
import com.example.targetassit.ui.theme.SecondaryTeal
import com.example.targetassit.ui.theme.SurfaceLight
import kotlin.math.roundToInt

@Composable
fun DpiGridVisualizer(
    dpi: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceLight)
            .padding(16.dp)
    ) {
        // Calculate grid spacing based on DPI
        val scaleFactor = 0.2f
        val spacing = (dpi * scaleFactor).roundToInt().coerceAtLeast(10)
        
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            
            // Draw vertical lines
            for (x in 0..width.toInt() step spacing) {
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(x.toFloat(), 0f),
                    end = Offset(x.toFloat(), height),
                    strokeWidth = 0.5f
                )
            }
            
            // Draw horizontal lines
            for (y in 0..height.toInt() step spacing) {
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, y.toFloat()),
                    end = Offset(width, y.toFloat()),
                    strokeWidth = 0.5f
                )
            }
            
            // Draw a crosshair in the center
            val centerX = width / 2
            val centerY = height / 2
            
            // Horizontal line of crosshair
            drawLine(
                color = PrimaryBlue,
                start = Offset(centerX - 40, centerY),
                end = Offset(centerX + 40, centerY),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
            
            // Vertical line of crosshair
            drawLine(
                color = PrimaryBlue,
                start = Offset(centerX, centerY - 40),
                end = Offset(centerX, centerY + 40),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
            
            // Draw a dot at the center
            drawCircle(
                color = SecondaryTeal,
                radius = 5f,
                center = Offset(centerX, centerY)
            )
        }
    }
} 