package at.sw21_tug.team_25.expirydates

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class InterfaceTests {

    private lateinit var stringToBetyped: String

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso"
    }

    @Test
    fun changeText_sameActivity() {
        // Type text and then press the button.
//        onView(withId(R.id.editTextUserInput))
//            .perform(typeText(stringToBetyped), closeSoftKeyboard())
//        onView(withId(R.id.changeTextBt)).perform(click())
//
//        // Check that the text was changed.
//        onView(withId(R.id.textToBeChanged))
//            .check(matches(withText(stringToBetyped)))
    }
}

/*
https://github.com/sw21-tug/Team_25/issues/21
https://developer.android.com/training/testing/ui-testing/espresso-testing
https://developer.android.com/training/basics/supporting-devices/languages
https://stackoverflow.com/questions/2900023/change-app-language-programmatically-in-android/9173571#9173571 !!!!!!!
 */