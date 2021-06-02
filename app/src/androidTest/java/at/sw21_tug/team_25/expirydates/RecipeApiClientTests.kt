package at.sw21_tug.team_25.expirydates

import androidx.test.ext.junit.runners.AndroidJUnit4
import at.sw21_tug.team_25.expirydates.utils.RecipeAPIClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class RecipeApiClientTests {

    /*
    @Test
    fun canQueryRealServer() {
        val api = RecipeAPIClient()
        val items = api.getRecipeForIngredient("tomato", 2)
        Assert.assertNotNull(items)
    }
    */

    @Test
    fun canRequestUrlForRecipe() {

        val server = MockWebServer()

        server.enqueue(
                MockResponse().setBody(
                        """
                {
                   "sourceUrl":"SOURCE_URL",
                   "spoonacularSourceUrl":"SPOONACULAR_SOURCE_URL",
                   "id":5
                }
                """.trimIndent())
        )

        val apiClient = RecipeAPIClient(server.url("").toString())
        Assert.assertNotNull(apiClient)

        val url = apiClient.getRecipeUrl(5)
        val request1 = server.takeRequest(5, TimeUnit.SECONDS)
        Assert.assertNotNull(request1)

        Assert.assertEquals("/5/information?apiKey=${RecipeAPIClient.API_KEY}", request1!!.path.toString())
        Assert.assertEquals("SPOONACULAR_SOURCE_URL", url)

        server.shutdown()
    }

    @Test
    fun canRequestRecipeInfos() {

        val server = MockWebServer()

        server.enqueue(
                MockResponse().setBody(
                        """
                [
                    {
                        "id": 5,
                        "title": "DemoRecipe",
                        "image": "IMAGE_URL"
                    }
                ]
                """.trimIndent())
        )
        server.enqueue(
                MockResponse().setBody(
                        """
                {
                   "sourceUrl":"SOURCE_URL",
                   "spoonacularSourceUrl":"SPOONACULAR_SOURCE_URL",
                   "id":5
                }
                """.trimIndent())
        )

        val apiClient = RecipeAPIClient(server.url("").toString())
        Assert.assertNotNull(apiClient)

        val infos = apiClient.getRecipeForIngredient("tomato", 1)
        val request1 = server.takeRequest(5, TimeUnit.SECONDS)
        Assert.assertNotNull(request1)

        Assert.assertEquals("/findByIngredients?ingredients=tomato&number=1&ignorePantry=true&ranking=1&apiKey=${RecipeAPIClient.API_KEY}", request1!!.path.toString())

        Assert.assertEquals(1, infos.size)
        Assert.assertEquals(5, infos[0].id)
        Assert.assertEquals("DemoRecipe", infos[0].title)
        Assert.assertEquals("IMAGE_URL", infos[0].imageURL)
        Assert.assertEquals("SPOONACULAR_SOURCE_URL", infos[0].recipeURL)

        server.shutdown()
    }
}
