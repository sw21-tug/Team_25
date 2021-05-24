package at.sw21_tug.team_25.expirydates

import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.utils.ReminderScheduler
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RunWith(MockitoJUnitRunner::class)
class ReminderSchedulerTests {


    @Test
    fun generateScheduledWorkTest() {
        val currentDate = LocalDateTime.parse("2021-04-24T10:15:30", DateTimeFormatter.ISO_LOCAL_DATE_TIME)


        val item1 = ExpItem("Item1", "2021-04-30")
        val item2 = ExpItem("Item2", "2021-04-30")

        val items = listOf(item1, item2)

        val req = ReminderScheduler.createWorkerRequest(currentDate, items)

        Assert.assertTrue(req.tags.contains(ReminderScheduler.work_tag))

        val ids = req.workSpec.input.getIntArray("item_ids")
        Assert.assertArrayEquals(ids, items.map { it.id }.toIntArray())

        //val expectedReminderDate = LocalDateTime.parse("2021-04-29T09:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val expectedDelay: Long = 427470 * 1000
        val timeDelay = req.workSpec.initialDelay

        Assert.assertEquals(expectedDelay, timeDelay)

    }

}