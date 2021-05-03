package at.sw21_tug.team_25.expirydates.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class Reminder(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {

        val ids = this.inputData.getIntArray("item_ids")
        if (ids == null) {
            Log.e("Reminder", "unable to display notification, no ids")
            return Result.success()
        }

        val names = this.inputData.getStringArray("item_names")
        if (names == null) {
            Log.e("Reminder", "unable to display notification, no names")
            return Result.success()
        }

        //val ctx = this.applicationContext
        //val db = ExpItemDatabase.getDatabase(ctx).expItemDao()

        for (n in names.indices) {
            val name = names[n]
            val id = ids[n]

            val notificationTitle = String.format("%s is expiring", name)
            val notificationBody = String.format("Product %s is expiring tomorrow", name)

            val data: Bundle = Bundle.EMPTY.deepCopy()
            data.putInt("item_id", id)

            NotificationManager.displayNotification(notificationTitle, notificationBody, this.applicationContext, data)
        }



        // implement handling of touching a notification




        return Result.success()
    }
}