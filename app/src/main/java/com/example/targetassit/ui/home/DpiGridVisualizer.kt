package com.example.targetassit.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun DpiGridVisualizer(
    dpi: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
    ) {
        // Calculate grid spacing based on DPI
        // For visualization purposes, we'll scale it to be visible
        // In a real implementation, this would be more accurate
        val scaleFactor = 0.2f  // Adjust this to make grid more/less dense
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
                    strokeWidth = 1f
                )
            }
            
            // Draw horizontal lines
            for (y in 0..height.toInt() step spacing) {
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, y.toFloat()),
                    end = Offset(width, y.toFloat()),
                    strokeWidth = 1f
                )
            }
            
            // Draw a crosshair in the center
            val centerX = width / 2
            val centerY = height / 2
            
            // Horizontal line of crosshair
            drawLine(
                color = Color.Red,
                start = Offset(centerX - 50, centerY),
                end = Offset(centerX + 50, centerY),
                strokeWidth = 2f
            )
            
            // Vertical line of crosshair
            drawLine(
                color = Color.Red,
                start = Offset(centerX, centerY - 50),
                end = Offset(centerX, centerY + 50),
                strokeWidth = 2f
            )
        }
        
        Text(
            text = "DPI: $dpi",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        )
        
        Text(
            text = "Grid spacing: ${(dpi * scaleFactor).roundToInt()} px",
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
} 