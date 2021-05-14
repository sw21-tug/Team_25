package at.sw21_tug.team_25.expirydates.utils

import okhttp3.JavaNetCookieJar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.net.CookieManager
import java.net.CookiePolicy


const val DEFAULT_API_PATH = "https://foodsharing.de/api"

data class FoodSharePoint(val id: Int, val name: String, val lat: Double, val lon: Double)

class FoodSharingAPIClient(private val baseUrl: String = DEFAULT_API_PATH) {

    private var client: OkHttpClient

    init {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        client = OkHttpClient.Builder().cookieJar(JavaNetCookieJar(cookieManager)).build()


        val requestData = JSONObject()
        requestData.put("email", "ebe43008@zwoho.com")
        requestData.put("password", "expirydates")
        requestData.put("remember_me", false)

        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

        val body = requestData.toString().toRequestBody(jsonMediaType)

        val req = Request.Builder()
                .post(body)
                .url(String.format("%s/user/login", baseUrl))
                .build()

        val resp = client.newCall(req).execute()
        if (!resp.isSuccessful) {
            throw Exception("unable to login to api")
        }
    }


    fun getNearbyFoodSharePoints(lat: Double, lon: Double, distance: String = "20"): List<FoodSharePoint> {
        val result = mutableListOf<FoodSharePoint>()

        val req = Request.Builder()
                .url(
                        String.format("%s/foodSharePoints/nearby?lat=%s&lon=%s&distance=%s", baseUrl, lat, lon, distance)
                ).build()

        val resp = client.newCall(req).execute()

        if (!resp.isSuccessful) {
            throw Exception("unable to getNearbyFoodSharePoints")
        }

        if (resp.body == null) {
            throw Exception("Server responded without body")
        }

        val responseBody = JSONArray(resp.body!!.string())

        for (i in 0 until responseBody.length()) {
            val item = responseBody.getJSONObject(i)

            val id = item.getInt("id")
            val name = item.getString("name")
            val itemLat = item.getDouble("lat")
            val itemLon = item.getDouble("lon")
            val point = FoodSharePoint(id, name, itemLat, itemLon)
            result.add(point)
        }
        
        return result
    }


}