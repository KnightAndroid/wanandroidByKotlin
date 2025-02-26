package com.knight.kotlin.library_base.util

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.view.Window
import androidx.core.view.WindowInsetsControllerCompat


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 11:26
 * @descript:
 */
fun Window.setSystemBarStyle(
    statusShaderP: Boolean,
    lightStatusP: Boolean,
    navigationShaderP: Boolean,
    lightNavigationP: Boolean,
) {
    var lightStatus = lightStatusP
    var statusShader = statusShaderP
    var lightNavigation = lightNavigationP
    var navigationShader = navigationShaderP

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        // Use default dark and light platform colors from EdgeToEdge
        val colorSystemBarDark = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
        val colorSystemBarLight = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Always apply a dark shader as a light or transparent status bar is not supported
            lightStatus = false
            statusShader = true
        }
        this.statusBarColor = if (statusShader) {
            if (lightStatus) colorSystemBarLight else colorSystemBarDark
        } else {
            Color.TRANSPARENT
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // Always apply a dark shader as a light or transparent navigation bar is not supported
            lightNavigation = false
            navigationShader = true
        }
        this.navigationBarColor = if (navigationShader) {
            if (lightNavigation) colorSystemBarLight else colorSystemBarDark
        } else {
            Color.TRANSPARENT
        }
    } else {
        this.isStatusBarContrastEnforced = statusShader
        this.isNavigationBarContrastEnforced = navigationShaderP
    }

    // Contrary to the documentation FALSE applies a light foreground color and TRUE a dark foreground color
    WindowInsetsControllerCompat(this, this.decorView).run {
        isAppearanceLightStatusBars = lightStatus
        isAppearanceLightNavigationBars = lightNavigation
    }
}


val Context.isMotionReduced: Boolean
    get() {
        return try {
            Settings.Global.getFloat(this.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE) == 0f
        } catch (e: SettingNotFoundException) {
            false
        }
    }