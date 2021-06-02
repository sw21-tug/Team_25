package at.sw21_tug.team_25.expirydates


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.ui.list.ExpItemRecyclerViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.test.TestCoroutineDispatcher
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

@LargeTest
@RunWith(AndroidJUnit4::class)
class ListSortingTest {

    companion object {
        private lateinit var expItemDao: ExpItemDao
        private lateinit var db: ExpItemDatabase
        private val testDispatcher = TestCoroutineDispatcher()

        @BeforeClass
        @JvmStatic
        fun setup() {
            Dispatchers.setMain(testDispatcher)
            val context = InstrumentationRegistry.getInstrumentation().targetContext

            db = ExpItemDatabase.getDatabase(context)

            expItemDao = db.expItemDao()
        }
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun initializeDb() {
        val expItemDao = ExpItemDatabase.getDatabase(
            InstrumentationRegistry.getInstrumentation().targetContext
        ).expItemDao()
        expItemDao.deleteAllItems()
        GlobalScope.async {
            expItemDao.insertItem(ExpItem("B", "2021-09-01 01:01:01"))
            expItemDao.insertItem(ExpItem("A", "2021-09-02 01:01:03"))
            expItemDao.insertItem(ExpItem("D", "2021-09-04 01:01:01"))
            expItemDao.insertItem(ExpItem("E", "2021-09-02 01:01:01"))
            expItemDao.insertItem(ExpItem("C", "2021-09-05 01:01:01"))
        }
    }

    @Test
    fun listSortingTest() {
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
        bottomNavigationItemView.perform(ViewActions.click())

        // Make sure RecyclerView is displayed
        onView(withId(R.id.items_rv))
            .check(matches(isDisplayed()))


        // Expect Items to be sorted expiry date ascending
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(0, "B"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(1, "E"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(2, "A"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(3, "D"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(4, "C"))

        // Let's test Sorting: Name ascending
        val actionMenuItemView = onView(
            allOf(
                withId(R.id.sort_by_name_menu),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(ViewActions.click())

        val materialTextView = onView(
            allOf(
                withId(R.id.title), withText("Name Ascending"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(ViewActions.click())

        // Expect Items to be sorted by name ascending
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(0, "A"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(1, "B"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(2, "C"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(3, "D"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(4, "E"))

        // Let's test Sorting: Name descending
        val actionMenuItemView2 = onView(
            allOf(
                withId(R.id.sort_by_name_menu),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        actionMenuItemView2.perform(ViewActions.click())

        val materialTextView2 = onView(
            allOf(
                withId(R.id.title), withText("Name Descending"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView2.perform(ViewActions.click())

        // Expect Items to be sorted by name ascending
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(0, "E"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(1, "D"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(2, "C"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(3, "B"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(4, "A"))

        // Let's test Sorting: Expires first
        val actionMenuItemView3 = onView(
            allOf(
                withId(R.id.sortByDateMenu),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        actionMenuItemView3.perform(ViewActions.click())

        val materialTextView3 = onView(
            allOf(
                withId(R.id.title), withText("Expires First"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView3.perform(ViewActions.click())

        // Expect Items to be sorted expiry date ascending
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(0, "B"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(1, "E"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(2, "A"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(3, "D"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(4, "C"))

        // Let's test Sorting: Expires last
        val actionMenuItemView4 = onView(
            allOf(
                withId(R.id.sortByDateMenu),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        actionMenuItemView4.perform(ViewActions.click())

        val materialTextView4 = onView(
            allOf(
                withId(R.id.title), withText("Expires Last"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView4.perform(ViewActions.click())

        // Expect Items to be sorted expiry date ascending
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(0, "C"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(1, "D"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(2, "A"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(3, "E"))
        onView(withId(R.id.items_rv)).check(RecyclerViewItemAtPositionAssertion(4, "B"))
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

    class RecyclerViewItemAtPositionAssertion(
        private val expectedIndex: Int,
        private val expectedName: String
    ) : ViewAssertion {
        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }
            val recyclerView = view as RecyclerView
            val expItemAdapter = recyclerView.adapter as ExpItemRecyclerViewAdapter
            assertThat(expItemAdapter.getExpItems()[expectedIndex].name, `is`(expectedName))
        }
    }
}
