package com.example.targetassit.service.overlay

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.targetassit.R
import com.example.targetassit.domain.model.SensitivitySettings
import kotlin.math.roundToInt

class OverlayService : Service() {
    
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private lateinit var overlayIconView: View
    
    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0f
    private var initialTouchY: Float = 0f
    
    private var isOverlayExpanded = false
    
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        setupOverlayIcon()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    private fun setupOverlayIcon() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 100
        
        overlayIconView = LayoutInflater.from(this).inflate(R.layout.overlay_icon, null)
        
        // Set up touch listener for drag movement
        overlayIconView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = initialX + (event.rawX - initialTouchX).roundToInt()
                    params.y = initialY + (event.rawY - initialTouchY).roundToInt()
                    windowManager.updateViewLayout(overlayIconView, params)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val moved = Math.abs(event.rawX - initialTouchX) > 5 || 
                                Math.abs(event.rawY - initialTouchY) > 5
                    if (!moved) {
                        toggleOverlayPanel()
                    }
                    true
                }
                else -> false
            }
        }
        
        windowManager.addView(overlayIconView, params)
    }
    
    private fun toggleOverlayPanel() {
        if (isOverlayExpanded) {
            windowManager.removeView(overlayView)
        } else {
            showOverlayPanel()
        }
        isOverlayExpanded = !isOverlayExpanded
    }
    
    private fun showOverlayPanel() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 50
        params.y = 300
        
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_panel, null)
        
        // Set up close button
        overlayView.findViewById<View>(R.id.btn_close).setOnClickListener {
            toggleOverlayPanel()
        }
        
        // Setup sensitivity controls
        setupSensitivityControls()
        
        windowManager.addView(overlayView, params)
    }
    
    private fun setupSensitivityControls() {
        // This would be implemented to adjust sensitivity settings in real-time
        // For now, this is just a placeholder
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (::overlayIconView.isInitialized) {
            windowManager.removeView(overlayIconView)
        }
        if (::overlayView.isInitialized && isOverlayExpanded) {
            windowManager.removeView(overlayView)
        }
    }
} 