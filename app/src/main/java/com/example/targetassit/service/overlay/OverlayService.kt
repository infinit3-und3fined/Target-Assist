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
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.NotificationCompat
import com.example.targetassit.R
import com.example.targetassit.data.preferences.SettingsPreferences
import com.example.targetassit.ui.common.DpiGridView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class OverlayService : Service() {

    @Inject
    lateinit var settingsPreferences: SettingsPreferences

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null
    private var params: WindowManager.LayoutParams? = null

    private val channelId = "overlay_service_channel"
    private val notificationId = 1001

    // Variables for drag functionality
    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0f
    private var initialTouchY: Float = 0f

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        createNotificationChannel()
        startForeground(notificationId, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startOverlay()
            ACTION_STOP -> stopSelf()
        }
        return START_STICKY
    }

    private fun startOverlay() {
        serviceScope.launch {
            try {
                if (overlayView == null) {
                    createOverlayView()
                }
            } catch (e: Exception) {
                Timber.e(e, "Error starting overlay")
            }
        }
    }

    private fun createOverlayView() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        overlayView = inflater.inflate(R.layout.overlay_layout, null)

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 100
        }

        windowManager.addView(overlayView, params)
        setupOverlayControls()
    }

    private fun setupOverlayControls() {
        overlayView?.let { view ->
            // Setup drag handle for moving the overlay
            val dragHandle = view.findViewById<ImageView>(R.id.drag_handle)
            dragHandle.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Save initial position
                        initialX = params?.x ?: 0
                        initialY = params?.y ?: 0
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        // Calculate new position
                        params?.x = initialX + (event.rawX - initialTouchX).toInt()
                        params?.y = initialY + (event.rawY - initialTouchY).toInt()
                        // Update overlay position
                        params?.let { windowManager.updateViewLayout(view, it) }
                        true
                    }
                    else -> false
                }
            }

            // Setup close button
            val closeButton = view.findViewById<ImageButton>(R.id.btn_close)
            closeButton.setOnClickListener {
                stopSelf()
            }

            // Setup settings button
            val settingsButton = view.findViewById<ImageButton>(R.id.btn_settings)
            settingsButton.setOnClickListener {
                // Toggle settings container visibility
                val settingsContainer = view.findViewById<LinearLayout>(R.id.settings_container)
                val dpiGridContainer = view.findViewById<LinearLayout>(R.id.dpi_grid_container)
                
                if (settingsContainer.visibility == View.VISIBLE) {
                    settingsContainer.visibility = View.GONE
                } else {
                    settingsContainer.visibility = View.VISIBLE
                    dpiGridContainer.visibility = View.GONE
                }
            }

            // Setup DPI button
            val dpiButton = view.findViewById<ImageButton>(R.id.btn_dpi)
            dpiButton.setOnClickListener {
                // Toggle DPI grid container visibility
                val dpiGridContainer = view.findViewById<LinearLayout>(R.id.dpi_grid_container)
                val settingsContainer = view.findViewById<LinearLayout>(R.id.settings_container)
                
                if (dpiGridContainer.visibility == View.VISIBLE) {
                    dpiGridContainer.visibility = View.GONE
                } else {
                    dpiGridContainer.visibility = View.VISIBLE
                    settingsContainer.visibility = View.GONE
                    
                    // Add DPI grid view if it doesn't exist
                    if (dpiGridContainer.childCount == 0) {
                        serviceScope.launch {
                            val dpi = settingsPreferences.dpi.first()
                            val dpiGridView = DpiGridView(this@OverlayService).apply {
                                this.dpi = dpi
                                this.gridSize = 200
                            }
                            dpiGridContainer.addView(dpiGridView)
                        }
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Overlay Service Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Channel for Target Assist overlay service"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Target Assist")
            .setContentText("Overlay is active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        removeOverlayView()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun removeOverlayView() {
        overlayView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                Timber.e(e, "Error removing overlay view")
            }
            overlayView = null
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val ACTION_START = "com.example.targetassit.START_OVERLAY"
        const val ACTION_STOP = "com.example.targetassit.STOP_OVERLAY"

        fun startService(context: Context) {
            val intent = Intent(context, OverlayService::class.java).apply {
                action = ACTION_START
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, OverlayService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
} 