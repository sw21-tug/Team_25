package at.sw21_tug.team_25.expirydates.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class Reminder(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {

        val id = this.inputData.getInt("item_id", 0);
        NotificationManager.displayNotification(String.format("Hello %d", id), "12345", this.applicationContext)


        return Result.success()
    }
}