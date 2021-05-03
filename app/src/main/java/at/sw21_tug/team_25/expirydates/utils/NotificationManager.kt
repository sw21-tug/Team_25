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
import kotlin.random.Random
import android.app.NotificationManager as AndroidNotificationManager

object NotificationManager {

    private lateinit var context: Context
    private var channel_id = "reminders"
    private const val EXTRA_ITEM_ID_KEY = "item_id"

    fun displayExpiryNotification(id: Int, name: String, ctx: Context? = null) {
        val usedCtx: Context = ctx ?: context


        val notificationTitle =
            usedCtx.resources.getString(R.string.expiry_notification_title, name)
        val notificationBody = usedCtx.resources.getString(R.string.expiry_notification_body, name)

        val data: Bundle = Bundle.EMPTY.deepCopy()
        data.putInt(EXTRA_ITEM_ID_KEY, id)

        displayNotification(notificationTitle, notificationBody, usedCtx, data)
    }

    fun displayNotification(
        title: String,
        body: String,
        ctx: Context? = null,
        extras: Bundle = Bundle.EMPTY
    ) {

        val usedCtx: Context = ctx ?: context

        val notificationId = Random.nextInt()

        constructChannel(usedCtx)
        val intent = Intent(usedCtx, MainActivity::class.java)
        intent.putExtras(extras)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                usedCtx,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val builder = Notification.Builder(usedCtx, channel_id)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_home_black_24dp)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notification = builder.build()

        with(NotificationManagerCompat.from(usedCtx)) {
            notify(notificationId, notification)
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

    fun setDefaultContext(con: Activity) {
        context = con
    }

    fun whenAppWasStartedByExpiryNotification(intent: Intent, cb: (itemId: Int) -> Unit) {

        val defaultValue: Int = -1
        val id = intent.getIntExtra(EXTRA_ITEM_ID_KEY, defaultValue)
        if (id != defaultValue) {
            Log.d("NotificationManager", "opened by notification with item_id $id")
            cb(id)
        }
    }

}