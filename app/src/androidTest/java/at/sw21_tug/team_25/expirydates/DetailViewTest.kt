package at.sw21_tug.team_25.expirydates


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
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
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@FixMethodOrder
class DetailViewTest {

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
//        - - - - - INFO: Causes errors in following test, that also use ExpItemDatabase - - - -
//        @AfterClass
//        @JvmStatic
//        @Throws(IOException::class)
//        fun closeDb() {
//            db.close()
//        }
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun initializeDb() {
        val expItemDao = ExpItemDatabase.getDatabase(
            InstrumentationRegistry.getInstrumentation().targetContext).expItemDao()
        expItemDao.deleteAllItems()
        GlobalScope.async {
            expItemDao.insertItem(ExpItem("Salami", "2021-01-01"))
            expItemDao.insertItem(ExpItem("Tomato", "2021-01-02"))
            expItemDao.insertItem(ExpItem("Bread", "2021-01-03"))
        }
    }

    @Test
    fun detailViewTest() {
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

        val materialTextViewSalami = onView(
            allOf(
                withId(R.id.item_tv), withText("Salami  2021-01-01"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.items_rv),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextViewSalami.perform(click())

        onView(withId(R.id.detail_view_popup)).inRoot(RootMatchers.isPlatformPopup()).check((matches(isDisplayed())))
        onView(withId(R.id.product_name)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("Salami")))
        onView(withId(R.id.exp_date)).inRoot(RootMatchers.isPlatformPopup()).check(matches(withText("2021-01-01")))
        onView(withId(R.id.closePopUp)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.detail_view_popup)).check(doesNotExist())
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
}