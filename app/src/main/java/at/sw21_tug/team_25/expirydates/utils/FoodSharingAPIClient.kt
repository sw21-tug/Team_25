package at.sw21_tug.team_25.expirydates.utils

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import java.net.CookieManager
import java.net.CookiePolicy


const val DEFAULT_API_PATH = "https://foodsharing.de/api"

data class FoodSharePoint(val id: Int, val name: String, val lat: Double, val lon: Double)

class FoodSharingAPIClient(val baseUrl: String = DEFAULT_API_PATH) {

    var client: OkHttpClient

    init {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        client = OkHttpClient.Builder().cookieJar(JavaNetCookieJar(cookieManager)).build()
    }


    fun getNearbyFoodSharePoints(lat: Double, lon: Double, distance: String = "20"): List<FoodSharePoint> {

        val result = mutableListOf<FoodSharePoint>()

        return result
    }


}