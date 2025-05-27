package com.example.targetassit.service.overlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.example.targetassit.R

class OverlayService : Service() {
    
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var isOverlayShown = false
    
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SHOW -> showOverlay()
            ACTION_HIDE -> hideOverlay()
            ACTION_TOGGLE -> toggleOverlay()
        }
        
        return START_STICKY
    }
    
    private fun showOverlay() {
        if (isOverlayShown) return
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }
        
        val container = FrameLayout(this)
        val composeView = ComposeView(this).apply {
            setContent {
                OverlayContent()
            }
        }
        
        container.addView(composeView)
        overlayView = container
        windowManager?.addView(overlayView, params)
        isOverlayShown = true
    }
    
    private fun hideOverlay() {
        if (!isOverlayShown) return
        
        overlayView?.let {
            windowManager?.removeView(it)
            overlayView = null
            isOverlayShown = false
        }
    }
    
    private fun toggleOverlay() {
        if (isOverlayShown) {
            hideOverlay()
        } else {
            showOverlay()
        }
    }
    
    @Composable
    private fun OverlayContent() {
        // Simple overlay content - a red dot in the center
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(Color.Red.copy(alpha = 0.5f), CircleShape)
        )
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        hideOverlay()
    }
    
    companion object {
        private const val ACTION_SHOW = "com.example.targetassit.action.SHOW_OVERLAY"
        private const val ACTION_HIDE = "com.example.targetassit.action.HIDE_OVERLAY"
        private const val ACTION_TOGGLE = "com.example.targetassit.action.TOGGLE_OVERLAY"
        
        fun showOverlay(context: Context) {
            val intent = Intent(context, OverlayService::class.java).apply {
                action = ACTION_SHOW
            }
            context.startService(intent)
        }
        
        fun hideOverlay(context: Context) {
            val intent = Intent(context, OverlayService::class.java).apply {
                action = ACTION_HIDE
            }
            context.startService(intent)
        }
        
        fun toggleOverlay(context: Context) {
            val intent = Intent(context, OverlayService::class.java).apply {
                action = ACTION_TOGGLE
            }
            context.startService(intent)
        }
    }
} 