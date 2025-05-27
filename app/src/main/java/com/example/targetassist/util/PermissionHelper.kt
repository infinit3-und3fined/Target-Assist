package com.example.targetassit.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

object PermissionHelper {
    
    /**
     * Checks if the app has permission to draw over other apps.
     * 
     * @param context The context to use for checking permission
     * @return True if permission is granted, false otherwise
     */
    fun canDrawOverlays(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }
    
    /**
     * Creates an intent to request permission to draw over other apps.
     * 
     * @param context The context to use for creating the intent
     * @return An intent that can be used to request the permission
     */
    fun createOverlayPermissionIntent(context: Context): Intent {
        return Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
} 