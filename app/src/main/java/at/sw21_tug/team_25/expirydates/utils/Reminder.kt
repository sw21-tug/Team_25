package at.sw21_tug.team_25.expirydates.utils

import android.content.Context
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

        for (n in names.indices) {
            val name = names[n]
            val id = ids[n]
            NotificationManager.displayExpiryNotification(id, name, this.applicationContext)
        }

        return Result.success()
    }
}
