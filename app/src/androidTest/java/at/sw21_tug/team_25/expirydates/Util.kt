package at.sw21_tug.team_25.expirydates;

import android.content.Context;
import android.view.inputmethod.InputMethodManager

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;

class Util {
    companion object {

        fun assertKeyboardOpen(is_open: Boolean) {
            val imm: InputMethodManager =
                InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
            Assert.assertEquals(imm.isAcceptingText, is_open)
        }
    }
}
