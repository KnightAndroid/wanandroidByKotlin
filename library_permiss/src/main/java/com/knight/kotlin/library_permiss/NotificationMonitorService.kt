package com.knight.kotlin.library_permiss


import android.app.Notification
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import com.knight.kotlin.library_util.toast.ToastUtils
import java.lang.String


/**
 * Author:Knight
 * Time:2023/8/30 18:48
 * Description:NotificationMonitorService
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
class NotificationMonitorService : NotificationListenerService() {

    /**
     * 当系统收到新的通知后出发回调
     */
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        val extras = sbn.notification.extras ?: return

        //获取通知消息标题
        val title = extras.getString(Notification.EXTRA_TITLE)
        // 获取通知消息内容
        val msgText: Any? = extras.getCharSequence(Notification.EXTRA_TEXT)
        ToastUtils.show(String.format(
            getString(R.string.permission_notification_listener_toast),
            title,
            msgText
        ))
    }

    /**
     * 当系统通知被删掉后出发回调
     */
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}