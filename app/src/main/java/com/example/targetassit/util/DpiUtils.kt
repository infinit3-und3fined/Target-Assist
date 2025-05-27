package com.example.targetassit.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object DpiUtils {
    
    /**
     * Calculates the physical distance in millimeters for a given number of pixels
     * based on the device's DPI.
     */
    fun pixelsToMm(context: Context, pixels: Int): Float {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        
        val dpi = displayMetrics.densityDpi
        return (pixels * 25.4f) / dpi
    }
    
    /**
     * Calculates the number of pixels for a given distance in millimeters
     * based on the device's DPI.
     */
    fun mmToPixels(context: Context, mm: Float): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        
        val dpi = displayMetrics.densityDpi
        return ((mm * dpi) / 25.4f).toInt()
    }
    
    /**
     * Calculates the grid size in pixels for a given DPI value.
     * This represents the smallest movement increment possible at that DPI.
     */
    fun calculateGridSize(dpi: Int): Float {
        // For a given DPI, the grid size is 1 inch / DPI
        // Convert to pixels based on device density
        return 1f / dpi
    }
} 