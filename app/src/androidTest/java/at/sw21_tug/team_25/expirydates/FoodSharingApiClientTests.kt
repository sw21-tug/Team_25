package at.sw21_tug.team_25.expirydates

import androidx.test.ext.junit.runners.AndroidJUnit4
import at.sw21_tug.team_25.expirydates.utils.FoodSharingAPIClient
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FoodSharingApiClientTests {
    @Test
    fun canRequestFoodSharePoints() {
        // Context of the app under test.
        val apiClient = FoodSharingAPIClient()
        val result = apiClient.getNearbyFoodSharePoints(47.070713, 15.439504)
        Assert.assertNotNull(result)
        Assert.assertTrue(result.isNotEmpty())
    }

}