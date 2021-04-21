package at.sw21_tug.team_25.expirydates.utils

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import at.sw21_tug.team_25.expirydates.R

object NotificationManager {

    private lateinit var context: Context
    private var channel_id = "12345"

    fun displayNotification(title: String, body: String) {


        val builder = Notification.Builder(context, channel_id)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_home_black_24dp)

        val notification = builder.build();
        val notification_id = 10

        with(NotificationManagerCompat.from(context)) {
            notify(notification_id, notification)
        }
    }

    fun constructChannel() {
        with(NotificationManagerCompat.from(context)) {

            if(getNotificationChannel(channel_id) == null) {
                val name = "channelname"
                val descriptionText = "channeldescription"
                val importance = NotificationManagerCompat.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channel_id, name, importance).apply {
                    description = descriptionText
                }

                createNotificationChannel(channel);
            }
        }
    }

    fun setContext(con: Context) {
        context=con
        constructChannel()
    }

}