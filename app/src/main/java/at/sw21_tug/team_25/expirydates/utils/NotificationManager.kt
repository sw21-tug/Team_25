package at.sw21_tug.team_25.expirydates.utils

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import android.app.NotificationManager as AndroidNotificationManager

object NotificationManager {

    private lateinit var context: Context
    private var channel_id = "reminders"

    fun displayNotification(
        title: String,
        body: String,
        ctx: Context? = null,
        extras: Bundle = Bundle.EMPTY
    ) {

        val usedCtx: Context = ctx ?: context

        constructChannel(usedCtx)
        val intent = Intent(usedCtx, MainActivity::class.java)
        intent.putExtras(extras)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(usedCtx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = Notification.Builder(usedCtx, channel_id)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_home_black_24dp)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notification = builder.build()
        val notification_id = 10

        with(NotificationManagerCompat.from(usedCtx)) {
            notify(notification_id, notification)
        }
    }

    fun constructChannel(ctx: Context) {
        with(NotificationManagerCompat.from(ctx)) {

            if(getNotificationChannel(channel_id) == null) {
                val name = "ExpiryDates Notifications"
                val descriptionText = "Notifications about expiring products."
                val importance = AndroidNotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channel_id, name, importance).apply {
                    description = descriptionText
                }

                createNotificationChannel(channel)
            }
        }
    }

    fun setup(con: Activity) {
        context = con

        val id = con.intent.getIntExtra("item_id", -1)
        if (id != -1) {
            Log.d("NotificationManager", "opened by notification with item_id $id")
            // TODO: open detail view for item with $id
        }
    }

}