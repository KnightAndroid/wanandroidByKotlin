package com.knight.kotlin.library_permiss


import android.app.Notification
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.knight.kotlin.library_util.toast


/**
 * @Description
 * @Author knight
 * @Time 2025/6/8 22:28
 * 
 */

class NotificationMonitorService : NotificationListenerService() {
    /**
     * 当系统收到新的通知后出发回调
     */
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // 需要注释掉回调 super.onNotificationPosted 的调用，测试在原生 Android 4.3 版本会触发崩溃
        // 但是测试在原生 Android 5.0 的版本却没有这个问题，证明这个是一个历史遗留问题
        // java.lang.AbstractMethodError: abstract method not implemented
        // super.onNotificationPosted(sbn);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }

        val extras = sbn.notification.extras ?: return

        //获取通知消息标题
        val title = extras.getString(Notification.EXTRA_TITLE)
        // 获取通知消息内容
        val msgText: Any? = extras.getCharSequence(Notification.EXTRA_TEXT)
        toast(
            String.format(
                getString(R.string.permission_notification_listener_toast),
                title,
                msgText
            )
        )
    }

    /**
     * 当系统通知被删掉后出发回调
     */
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        // 需要注释掉回调 super.onNotificationRemoved 的调用，测试在原生 Android 4.3 版本会触发崩溃
        // 但是测试在原生 Android 5.0 的版本却没有这个问题，证明这个是一个历史遗留问题
        // java.lang.AbstractMethodError: abstract method not implemented
        // super.onNotificationRemoved(sbn);
    }
}