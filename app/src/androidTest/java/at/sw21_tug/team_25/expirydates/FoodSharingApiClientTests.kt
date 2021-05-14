package at.sw21_tug.team_25.expirydates

import androidx.test.ext.junit.runners.AndroidJUnit4
import at.sw21_tug.team_25.expirydates.utils.FoodSharingAPIClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class FoodSharingApiClientTests {
    @Test
    fun canLoginAndRequestDataFromLiveApi() {
        // Context of the app under test.
        val apiClient = FoodSharingAPIClient()
        val result = apiClient.getNearbyFoodSharePoints(47.070713, 15.439504)
        Assert.assertNotNull(result)
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun canLoginAndRequestData() {

        val dummyCsrfTokenCookie = "CSRF-TOKEN=DUMMY_API_KEY"

        val server = MockWebServer()

        server.enqueue(
                MockResponse().setBody("{}").setHeader("Set-Cookie", "$dummyCsrfTokenCookie; Path=/")
        )

        val apiClient = FoodSharingAPIClient(server.url("/api").toString())
        Assert.assertNotNull(apiClient)

        val request1 = server.takeRequest(5, TimeUnit.SECONDS)
        Assert.assertNotNull(request1)

        Assert.assertEquals("/api/user/login", request1!!.path.toString())
        val reqData = JSONObject(request1.body.readUtf8())
        Assert.assertEquals(reqData.getString("email"), FoodSharingAPIClient.EMAIL)
        Assert.assertEquals(reqData.getString("password"), FoodSharingAPIClient.PASSWORD)

        server.enqueue(
                MockResponse().setBody("""
[
   {
      "id":1773,
      "regionId":149,
      "name":"Fair-Teiler Priesterseminar",
      "description":"Es gibt einen Kühl-Gefrierkombi derzeit vor Ort, ein Kasten soll noch kommen.\r\n\r\nDerzeit ist das Einfahrtstor von 6 Uhr bis 23 Uhr geöffnet. Hinaus kommt man immer durch einen Not Taster Links vom Einfahrtstor.",
      "address":"Bürgergasse 2",
      "city":"Graz",
      "postcode":"8010",
      "lat":47.0720184,
      "lon":15.4416965,
      "createdAt":"2020-07-02T00:00:00+02:00",
      "picture":"picture/5efdf1c255dd1.jpg"
   },
   {
      "id":434,
      "regionId":149,
      "name":"Fair-Teiler Reitschulgasse Weltladen",
      "description":"Dieser zentrale \"FAIR-Teiler\" ist jederzeit zugänglich und sehr Nahe am Jakominiplatz.\r\n\r\nEr ist beim Weltladen links neben dem kleinen Friseur, links in einem ehemaligem Geschäfts-Eingang. Dadurch ist er ein bischen versteckt und vor Sonne, Regen und Schnee geschützt. :)\r\n\r\n\r\nAls foodsaver findest du ihn intern als angelegten Betrieb: \"Fair-Teiler Putzteam Weltladen - Reitschulgasse\".\r\n\r\nEinfach danach suchen, eintragen und mithelfen - DANKE  ;-)",
      "address":"Reitschulgasse 14",
      "city":"Graz",
      "postcode":"8010",
      "lat":47.0667686,
      "lon":15.444902299999967,
      "createdAt":"2015-06-19T00:00:00+02:00",
      "picture":"picture/5ee4af2ea0b81.jpg"
   }
]
        """.trimMargin()))

        val result = apiClient.getNearbyFoodSharePoints(47.070713, 15.439504)

        Assert.assertEquals(2, result.size)
        Assert.assertEquals(1773, result[0].id)
        Assert.assertEquals("Fair-Teiler Reitschulgasse Weltladen", result[1].name)
        Assert.assertEquals(47.0667686, result[1].lat, 0.0)

        val request2 = server.takeRequest(5, TimeUnit.SECONDS)
        Assert.assertNotNull(request1)

        val usedPath = request2!!.path.toString()
        Assert.assertEquals("/api/foodSharePoints/nearby?lat=47.070713&lon=15.439504&distance=20", usedPath)

        Assert.assertEquals(dummyCsrfTokenCookie, request2.getHeader("Cookie"))

        server.shutdown()
    }


}