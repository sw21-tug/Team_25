package at.sw21_tug.team_25.expirydates.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import kotlin.random.Random
import android.app.NotificationManager as AndroidNotificationManager

object NotificationManager {

    private var channel_id = "reminders"
    private const val EXTRA_ITEM_ID_KEY = "item_id"

    fun displayExpiryNotification(id: Int, name: String, ctx: Context) {

        val notificationTitle =
            ctx.resources.getString(R.string.expiry_notification_title, name)
        val notificationBody = ctx.resources.getString(R.string.expiry_notification_body, name)

        val data: Bundle = Bundle.EMPTY.deepCopy()
        data.putInt(EXTRA_ITEM_ID_KEY, id)

        displayNotification(notificationTitle, notificationBody, ctx, data)
    }

    fun displayNotification(
        title: String,
        body: String,
        ctx: Context,
        extras: Bundle = Bundle.EMPTY
    ) {

        val notificationId = Random.nextInt()

        constructChannel(ctx)
        val intent = Intent(ctx, MainActivity::class.java)
        intent.putExtras(extras)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                ctx,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val builder = Notification.Builder(ctx, channel_id)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_home_black_24dp)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notification = builder.build()

        with(NotificationManagerCompat.from(ctx)) {
            notify(notificationId, notification)
        }
    }

    private fun constructChannel(ctx: Context) {
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

    fun whenAppWasStartedByExpiryNotification(intent: Intent, cb: (itemId: Int) -> Unit) {

        val defaultValue: Int = -1
        val id = intent.getIntExtra(EXTRA_ITEM_ID_KEY, defaultValue)
        if (id != defaultValue) {
            cb(id)
        }
    }

}