package at.sw21_tug.team_25.expirydates.utils

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class Reminder(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {

        val id = this.inputData.getIntArray("item_ids")
        if (id == null) {
            Log.e("Reminder", "unable to display notification, no ids")
            return Result.success()
        }

        // grab items from db by ids
        // display one notification each
        // implement handling of touching a notification

        NotificationManager.displayNotification(String.format("Hello %d", id), "12345", this.applicationContext)


        return Result.success()
    }
}