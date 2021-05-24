package at.sw21_tug.team_25.expirydates.utils

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.work.*
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    var work_tag = "expiry_reminders_queue"
    private var unique_work_tag = "remind_next_expiry_date_job"

    suspend fun ensureNextReminderScheduled(ctx: Context) {
        val db = ExpItemDatabase.getDatabase(ctx).expItemDao()

        val expiredItems = db.getNextExpiringItems()

        if (expiredItems.isEmpty()) {
            WorkManager.getInstance(ctx).cancelUniqueWork(unique_work_tag)
            return
        }

        val workRequest = createWorkerRequest(LocalDateTime.now(), expiredItems)

        WorkManager.getInstance(ctx).enqueueUniqueWork(unique_work_tag, ExistingWorkPolicy.REPLACE, workRequest)
    }

    @VisibleForTesting
    fun createWorkerRequest(currentDateTime: LocalDateTime, items: List<ExpItem>): OneTimeWorkRequest {
        val ids = items.map { it.id }
        val names = items.map { it.name }


        val data = Data.Builder()
                .putIntArray("item_ids", ids.toIntArray())
                .putStringArray("item_names", names.toTypedArray())
                .build()

        val timeDelay = calculateNotificationDelayTimeInMillis(currentDateTime, items[0].date)

        return OneTimeWorkRequestBuilder<Reminder>()
                .setInputData(data)
                .addTag(work_tag)
                .setInitialDelay(timeDelay, TimeUnit.MILLISECONDS).build()
    }

    @VisibleForTesting
    fun calculateNotificationDelayTimeInMillis(currentDateTime: LocalDateTime, dueDate: String): Long {
        // "yyyy-MM-dd" -> dueDate
        var reminderDateTime = LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay()
        reminderDateTime = reminderDateTime.minusDays(1)
        reminderDateTime = reminderDateTime.withHour(9)

        return reminderDateTime.toInstant(ZoneOffset.UTC).toEpochMilli() - currentDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    }

}