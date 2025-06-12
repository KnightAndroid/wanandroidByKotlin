package com.knight.kotlin.library_base.ktx

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutManager
import android.hardware.SensorManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import java.io.File


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/26 9:41
 * @descript:
 */
/**
 * Taken from Mihon
 * Apache License, Version 2.0
 *
 * https://github.com/mihonapp/mihon/blob/162b6397050e1577c113a88e7b7cfe9f98e6a45c/app/src/main/java/eu/kanade/tachiyomi/util/system/ContextExtensions.kt
 */

/**
 * Checks if the give permission is granted.
 *
 * @param permission the permission to check.
 * @return true if it has permissions.
 */
fun Context.hasPermission(
    permission: String,
) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

/**
 * Checks if the notification permission is granted.
 *
 * @return true if the permission is granted. Always returns true on Android 12 and lower.
 */
val Context.hasNotificationPermission
    get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            hasPermission(Manifest.permission.POST_NOTIFICATIONS)

val Context.inputMethodManager: InputMethodManager
    get() = getSystemService()!!

val Context.locationManager: LocationManager
    get() = getSystemService()!!

val Context.powerManager: PowerManager
    get() = getSystemService()!!

val Context.sensorManager: SensorManager?
    get() = if (SettingsManager.getInstance(this).isGravitySensorEnabled) {
        getSystemService()
    } else {
        null
    }

val Context.windowManager: WindowManager?
    get() = getSystemService()

val Context.shortcutManager: ShortcutManager?
    get() = getSystemService()

fun Context.createFileInCacheDir(name: String): File {
    val file = File(externalCacheDir, name)
    if (file.exists()) {
        file.delete()
    }
    file.createNewFile()
    return file
}

fun Context.openApplicationDetailsSettings() {
    startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
            Uri.fromParts("package", packageName, null)
        )
    )
}
