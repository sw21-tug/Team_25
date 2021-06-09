package at.sw21_tug.team_25.expirydates.utils

import androidx.room.Room
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.data.ExpItemMigrations
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder


data class RecipeInfo(val id: Int, val title: String, val imageURL: String, val recipeURL: String)

class RecipeAPIClient(private val baseUrl: String = DEFAULT_API_URL) {

    companion object {
        const val DEFAULT_API_URL = "https://api.spoonacular.com/recipes/"
        const val API_KEY = "6f3d772af5a447369c4d716db641ab18"
    }

    private var client: OkHttpClient = OkHttpClient.Builder().build()

    fun inDebugMode(): Boolean {
        return try {
            Class.forName("at.sw21_tug.team_25.expirydates.RecipeApiClientTests")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    fun getRecipeForIngredient(ingredient: String, numItems: Int = 1): List<RecipeInfo> {
        val result = mutableListOf<RecipeInfo>()

        if (inDebugMode()) {
            if (ingredient == "Tomato") {
                result.add(RecipeInfo(5, "TomatoDummyRecipe", "https://upload.wikimedia.org/wikipedia/commons/3/3a/Tomato_Ketchup.png", "https://de.wikipedia.org/wiki/Tomate"))
            }
            return result
        }

        val ingredientEncoded = URLEncoder.encode(ingredient, "utf-8")
        val request = Request.Builder()
            .url("${baseUrl}findByIngredients?ingredients=$ingredientEncoded&number=$numItems&ignorePantry=true&ranking=1&apiKey=$API_KEY")
            .get()
            .build()

        val resp = client.newCall(request).execute()

        if (!resp.isSuccessful) {
            return result
//            throw Exception("unable to getRecipeForIngredient")
        }

        if (resp.body == null) {
            return result
//            throw Exception("Server responded without body")
        }

        val responseBody = JSONArray(resp.body!!.string())

        for (i in 0 until responseBody.length()) {
            val item = responseBody.getJSONObject(i)

            val id = item.getInt("id")
            val title = item.getString("title")
            val image = item.getString("image")
            val url = this.getRecipeUrl(id)
            val point = RecipeInfo(id, title, image, url)
            result.add(point)
        }

        return result
    }

    fun getRecipeUrl(id: Int): String {

        if (inDebugMode()) {
            return ""
        }

        val request = Request.Builder()
            .url("${baseUrl}$id/information?apiKey=$API_KEY")
            .get()
            .build()

        val resp = client.newCall(request).execute()

        if (!resp.isSuccessful) {
            return ""
//            throw Exception("unable to getRecipeUrl")
        }

        if (resp.body == null) {
            return ""
//            throw Exception("Server responded without body")
        }

        val responseBody = JSONObject(resp.body!!.string())
        resp.close()

        if (responseBody.has("spoonacularSourceUrl")) {
            return responseBody.getString("spoonacularSourceUrl")
        }
        if (responseBody.has("sourceUrl")) {
            return responseBody.getString("sourceUrl")
        }

        return ""
    }
}
