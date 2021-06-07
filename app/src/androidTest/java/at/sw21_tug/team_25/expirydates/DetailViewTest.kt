package at.sw21_tug.team_25.expirydates


import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import at.sw21_tug.team_25.expirydates.Util.Companion.assertKeyboardOpen
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDao
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.Is
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@FixMethodOrder
class DetailViewTest {

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
            InstrumentationRegistry.getInstrumentation().targetContext
        ).expItemDao()
        expItemDao.deleteAllItems()
        GlobalScope.async {
            expItemDao.insertItem(ExpItem("Salami", "2021-01-01 01:01:01"))
            expItemDao.insertItem(ExpItem("Tomato", "2021-01-02 02:02:02"))
            expItemDao.insertItem(ExpItem("Bread", "2021-01-03 03:03:03"))
        }
    }

    @Test
    fun detailViewTest() {
        openSalamiItem()

        onView(withId(R.id.detail_view_popup)).inRoot(RootMatchers.isPlatformPopup())
            .check((matches(isDisplayed())))
        onView(withId(R.id.product_name)).inRoot(RootMatchers.isPlatformPopup())
            .check(matches(withText("Salami")))
        onView(withId(R.id.exp_date)).inRoot(RootMatchers.isPlatformPopup())
            .check(matches(withText("2021-01-01 01:01:01")))
        onView(withId(R.id.closePopUp)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.detail_view_popup)).check(doesNotExist())

        assertKeyboardOpen(false)
    }

    @Test
    fun inputTest(){
        openSalamiItem()

        Thread.sleep(1000)

        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        onView(withId(R.id.product_name_edit)).perform(ViewActions.replaceText(""))
        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        assertKeyboardOpen(true)
        onView(withId(R.id.product_name_edit)).check((matches(isDisplayed())))

        val longText =  "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

        onView(withId(R.id.product_name_edit)).perform(ViewActions.replaceText(longText))
        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        assertKeyboardOpen(true)
        onView(withId(R.id.product_name_edit)).check((matches(isDisplayed())))


    }

    @Test
    fun deleteItemTest() {
        openSalamiItem()

        onView(withId(R.id.detail_view_popup)).inRoot(RootMatchers.isPlatformPopup())
            .check((matches(isDisplayed())))
        onView(withId(R.id.product_name)).inRoot(RootMatchers.isPlatformPopup())
            .check(matches(withText("Salami")))
        onView(withId(R.id.exp_date)).inRoot(RootMatchers.isPlatformPopup())
            .check(matches(withText("2021-01-01 01:01:01")))
        onView(withId(R.id.deleteItem)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.detail_view_popup)).check(doesNotExist())

        assertKeyboardOpen(false)

        onView(withId(R.id.items_rv)).check(ListFragmentTest.RecyclerViewItemCountAssertion(2))


    }

    @Test
    fun editItemTest() {
        openSalamiItem()

        Thread.sleep(1000)

        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        onView(withId(R.id.exp_date)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        val materialButton4 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(Is.`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton4.perform(ViewActions.scrollTo(), click())

        onView(withId(R.id.product_name_edit)).perform(ViewActions.replaceText("Hauswurst"))

        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        onView(withId(R.id.closePopUp)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        assertKeyboardOpen(false)

        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val currentDateFormatted = currentDate.format(formatter)

        val textView = onView(
            allOf(
                withId(R.id.item_tv), withText("Hauswurst  $currentDateFormatted"),
                withParent(withParent(withId(R.id.items_rv))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Hauswurst  $currentDateFormatted")))
    }

    @Test
    fun editItemTestNull() {
        openSalamiItem()

        Thread.sleep(1000)

        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        onView(withId(R.id.product_name_edit)).perform(ViewActions.replaceText(""))

        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        // check if toast appears and keyboard stays open
        onView(withText(R.string.invalidInput)).inRoot(
            withDecorView(
                not(
                    mActivityTestRule.activity.window.decorView
                )
            )
        ).check(matches(isDisplayed()))
        assertKeyboardOpen(true)

        onView(withId(R.id.product_name_edit)).perform(ViewActions.replaceText("Hauswurst"))

        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        onView(withId(R.id.closePopUp)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        assertKeyboardOpen(false)
    }


    @Test
    fun shareViewTest() {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        openSalamiItem()

        onView(withId(R.id.share)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        Thread.sleep(500)
        val shareTest = uiDevice.findObject(
            By.textContains(
                "Messages"
            )
        )
        Assert.assertTrue(shareTest != null)

        uiDevice.pressBack()
    }

    @Test
    fun cancelCloseTest(){
        openSalamiItem()
        //edit
        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.product_name_edit)).check(matches(isDisplayed()))
        //cancel
        onView(withId(R.id.closePopUp)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.product_name_edit)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        //edit
        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.product_name_edit)).check(matches(isDisplayed()))
        onView(withId(R.id.product_name_edit)).perform(ViewActions.replaceText("Hauswurst"))

        //cancel
        onView(withId(R.id.closePopUp)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.product_name_edit)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.product_name)).check(matches(isDisplayed()))
        onView(withId(R.id.product_name)).check(matches(withText("Salami")))

        //close
        onView(withId(R.id.closePopUp)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.detail_view_popup)).check(doesNotExist())
        assertKeyboardOpen(false)

        openSalamiItem()
        onView(withId(R.id.edit)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.product_name_edit)).check(matches(isDisplayed()))

    }


    private fun openSalamiItem(){
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
                withId(R.id.item_tv), withText("Salami  2021-01-01 01:01:01"),
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
