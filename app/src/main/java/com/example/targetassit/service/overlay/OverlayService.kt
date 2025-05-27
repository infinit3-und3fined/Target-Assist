package com.example.targetassit.service.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.app.NotificationCompat
import com.example.targetassit.R
import com.example.targetassit.data.preferences.UserPreferencesRepository
import com.example.targetassit.ui.overlay.OverlayView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OverlayService : Service() {
    
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: OverlayView
    private var isOverlayShowing = false
    
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "TargetAssistOverlay"
        
        fun startService(context: Context) {
            val intent = Intent(context, OverlayService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
        
        fun stopService(context: Context) {
            val intent = Intent(context, OverlayService::class.java)
            context.stopService(intent)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        userPreferencesRepository = UserPreferencesRepository.getInstance(applicationContext)
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        
        if (!isOverlayShowing) {
            showOverlay()
            observePreferences()
        }
        
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (isOverlayShowing) {
            windowManager.removeView(overlayView)
            isOverlayShowing = false
        }
        serviceScope.cancel()
        userPreferencesRepository.updateOverlayEnabled(false)
    }
    
    private fun showOverlay() {
        // Create the overlay view
        overlayView = OverlayView(this)
        
        // Set window parameters
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        
        // Add the view to window manager
        windowManager.addView(overlayView, params)
        isOverlayShowing = true
    }
    
    private fun observePreferences() {
        serviceScope.launch {
            userPreferencesRepository.userPreferencesFlow.collectLatest { preferences ->
                overlayView.setGridDensity(preferences.gridDensity)
                overlayView.toggleGrid(preferences.isGridVisible)
                overlayView.toggleCenterMarker(preferences.isCenterMarkerVisible)
                
                // If overlay is disabled in preferences, stop the service
                if (!preferences.isOverlayEnabled) {
                    stopSelf()
                }
            }
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Target Assist Overlay",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows notification when overlay is active"
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Target Assist Active")
            .setContentText("Overlay is currently active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
} 