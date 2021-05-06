package at.sw21_tug.team_25.expirydates

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.data.ExpItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@LargeTest
@RunWith(MockitoJUnitRunner::class)
class ListFragmentTest {

    companion object {
        lateinit var expItemDao: ExpItemDao
        lateinit var db: ExpItemDatabase
        lateinit var repository: ExpItemRepository
        private val testDispatcher = TestCoroutineDispatcher()
        private val testScope = TestCoroutineScope(testDispatcher)

        @BeforeClass
        @JvmStatic
        fun setup() {
            Dispatchers.setMain(testDispatcher)
            val context = InstrumentationRegistry.getInstrumentation().targetContext

            db = ExpItemDatabase.getDatabase(context)

            expItemDao = db.expItemDao()
            repository = ExpItemRepository(expItemDao)
        }
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun initializeDb() {
        val expItemDao = ExpItemDatabase.getDatabase(
                InstrumentationRegistry.getInstrumentation().targetContext).expItemDao()
        expItemDao.deleteAllItems()
        for (i in 10..30) {
            GlobalScope.async {
            // yyyy-MM-dd hh:mm:ss
            var date = "20" + i.toString()
            date = date + "-05-01 20:43:45"
            var expItem = ExpItem("Salami " + i.toString(), date)
            expItemDao.insertItem(expItem)
            }
        }
    }

    @Test
    fun listFragmentTest() {
        val bottomNavigationItemView = onView(
                allOf(
                        withId(R.id.navigation_list), withContentDescription("List"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0
                                ),
                                2
                        ),
                        isDisplayed()
                )
        )
        bottomNavigationItemView.perform(click())

        onView(withId(R.id.items_rv))
                .check(matches(isDisplayed()))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemCountAssertion(21))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {
        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            assertThat(adapter!!.itemCount, `is`(expectedCount))
        }
    }
}
