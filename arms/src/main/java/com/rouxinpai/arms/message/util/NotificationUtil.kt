@file:Suppress("DEPRECATION")

package com.rouxinpai.arms.message.util

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rouxinpai.arms.R
import java.util.UUID

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/3/12 14:54
 * desc   :
 */
object NotificationUtil {

    /**
     *
     */
    fun buildNotification(
        context: Context,
        isForeground: Boolean,
        channelId: String,
        channelName: String,
        contentTitle: String,
        contentText: String? = null,
        intent: PendingIntent? = null,
    ): Notification {
        // 8.0 以上需要特殊处理，创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = if (isForeground) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = context.getSystemService(IntentService.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        // 创建通知
        val notification = NotificationCompat.Builder(context, channelId)
            .apply {
                setSmallIcon(R.drawable.ic_notifications)
                setContentTitle(contentTitle)
                contentText?.let { setContentText(contentText) }
                intent?.let { setContentIntent(intent) }
                setSilent(isForeground)
                if (!isForeground) {
                    priority = NotificationCompat.PRIORITY_DEFAULT
                    setAutoCancel(true)
                }
            }.build()
        return notification
    }

    /**
     *
     */
    fun buildPendingIntent(context: Context, intents: Array<Intent>): PendingIntent {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_ONE_SHOT
        return PendingIntent.getActivities(context, 0, intents, flags)
    }

    /**
     * 发送通知
     */
    fun sendNotification(context: Context, notification: Notification) {
        val id = UUID.randomUUID().hashCode()
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}