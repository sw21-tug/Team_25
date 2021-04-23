package at.sw21_tug.team_25.expirydates.utils

import android.content.Context
import android.icu.util.Calendar
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

object ReminderScheduler {

    fun scheduleNextReminder(ctx: Context) {

        val currentDate = Calendar.getInstance()
        val expiryDate = Calendar.getInstance()
        expiryDate.add(Calendar.SECOND, 10);

        val data = Data.Builder().putInt("item_id", 42).build();

        val timeDiff = expiryDate.timeInMillis - currentDate.timeInMillis
        val dailyWorkRequest = OneTimeWorkRequestBuilder<Reminder>()
            .setInputData(data)
            .setInitialDelay(timeDiff, java.util.concurrent.TimeUnit.MILLISECONDS).build()
        WorkManager.getInstance(ctx).enqueue(dailyWorkRequest)

    }

}