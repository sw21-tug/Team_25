package at.sw21_tug.team_25.expirydates.utils

import android.app.NotificationManager as AndroidNotificationManager
import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import at.sw21_tug.team_25.expirydates.R

object NotificationManager {

    private lateinit var context: Context
    private var channel_id = "reminders"

    fun displayNotification(title: String, body: String, ctx: Context? = null) {

        val usedCtx: Context = ctx ?: context

        constructChannel(usedCtx)

        val builder = Notification.Builder(usedCtx, channel_id)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_home_black_24dp)

        val notification = builder.build();
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
                val importance = NotificationManagerCompat.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channel_id, name, AndroidNotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = descriptionText
                }

                createNotificationChannel(channel);
            }
        }
    }

    fun setDefaultContext(con: Context) {
        context=con
    }

}