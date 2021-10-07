package mhelrigo.cocktailmanual.data.remote

import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.entity.ingredient.IngredientsApiEntity
import mhelrigo.cocktailmanual.data.repository.ingredient.remote.IngredientApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

const val FAKE_JSON_RESPONSE_1 =
    "{\"drinks\":[{\"strIngredient1\":\"Light rum\"},{\"strIngredient1\":\"Applejack\"},{\"strIngredient1\":\"Gin\"},{\"strIngredient1\":\"Dark rum\"},{\"strIngredient1\":\"Sweet Vermouth\"},{\"strIngredient1\":\"Strawberry schnapps\"},{\"strIngredient1\":\"Scotch\"},{\"strIngredient1\":\"Apricot brandy\"},{\"strIngredient1\":\"Triple sec\"},{\"strIngredient1\":\"Southern Comfort\"},{\"strIngredient1\":\"Orange bitters\"},{\"strIngredient1\":\"Brandy\"},{\"strIngredient1\":\"Lemon vodka\"},{\"strIngredient1\":\"Blended whiskey\"},{\"strIngredient1\":\"Dry Vermouth\"},{\"strIngredient1\":\"Amaretto\"},{\"strIngredient1\":\"Tea\"},{\"strIngredient1\":\"Champagne\"},{\"strIngredient1\":\"Coffee liqueur\"},{\"strIngredient1\":\"Bourbon\"},{\"strIngredient1\":\"Tequila\"},{\"strIngredient1\":\"Vodka\"},{\"strIngredient1\":\"A\\u00f1ejo rum\"},{\"strIngredient1\":\"Bitters\"},{\"strIngredient1\":\"Sugar\"},{\"strIngredient1\":\"Kahlua\"},{\"strIngredient1\":\"demerara Sugar\"},{\"strIngredient1\":\"Dubonnet Rouge\"},{\"strIngredient1\":\"Watermelon\"},{\"strIngredient1\":\"Lime juice\"},{\"strIngredient1\":\"Irish whiskey\"},{\"strIngredient1\":\"Apple brandy\"},{\"strIngredient1\":\"Carbonated water\"},{\"strIngredient1\":\"Cherry brandy\"},{\"strIngredient1\":\"Creme de Cacao\"},{\"strIngredient1\":\"Grenadine\"},{\"strIngredient1\":\"Port\"},{\"strIngredient1\":\"Coffee brandy\"},{\"strIngredient1\":\"Red wine\"},{\"strIngredient1\":\"Rum\"},{\"strIngredient1\":\"Grapefruit juice\"},{\"strIngredient1\":\"Ricard\"},{\"strIngredient1\":\"Sherry\"},{\"strIngredient1\":\"Cognac\"},{\"strIngredient1\":\"Sloe gin\"},{\"strIngredient1\":\"Apple juice\"},{\"strIngredient1\":\"Pineapple juice\"},{\"strIngredient1\":\"Lemon juice\"},{\"strIngredient1\":\"Sugar syrup\"},{\"strIngredient1\":\"Milk\"},{\"strIngredient1\":\"Strawberries\"},{\"strIngredient1\":\"Chocolate syrup\"},{\"strIngredient1\":\"Yoghurt\"},{\"strIngredient1\":\"Mango\"},{\"strIngredient1\":\"Ginger\"},{\"strIngredient1\":\"Lime\"},{\"strIngredient1\":\"Cantaloupe\"},{\"strIngredient1\":\"Berries\"},{\"strIngredient1\":\"Grapes\"},{\"strIngredient1\":\"Kiwi\"},{\"strIngredient1\":\"Tomato juice\"},{\"strIngredient1\":\"Cocoa powder\"},{\"strIngredient1\":\"Chocolate\"},{\"strIngredient1\":\"Heavy cream\"},{\"strIngredient1\":\"Galliano\"},{\"strIngredient1\":\"Peach Vodka\"},{\"strIngredient1\":\"Ouzo\"},{\"strIngredient1\":\"Coffee\"},{\"strIngredient1\":\"Spiced rum\"},{\"strIngredient1\":\"Water\"},{\"strIngredient1\":\"Espresso\"},{\"strIngredient1\":\"Angelica root\"},{\"strIngredient1\":\"Orange\"},{\"strIngredient1\":\"Cranberries\"},{\"strIngredient1\":\"Johnnie Walker\"},{\"strIngredient1\":\"Apple cider\"},{\"strIngredient1\":\"Everclear\"},{\"strIngredient1\":\"Cranberry juice\"},{\"strIngredient1\":\"Egg yolk\"},{\"strIngredient1\":\"Egg\"},{\"strIngredient1\":\"Grape juice\"},{\"strIngredient1\":\"Peach nectar\"},{\"strIngredient1\":\"Lemon\"},{\"strIngredient1\":\"Firewater\"},{\"strIngredient1\":\"Lemonade\"},{\"strIngredient1\":\"Lager\"},{\"strIngredient1\":\"Whiskey\"},{\"strIngredient1\":\"Absolut Citron\"},{\"strIngredient1\":\"Pisco\"},{\"strIngredient1\":\"Irish cream\"},{\"strIngredient1\":\"Ale\"},{\"strIngredient1\":\"Chocolate liqueur\"},{\"strIngredient1\":\"Midori melon liqueur\"},{\"strIngredient1\":\"Sambuca\"},{\"strIngredient1\":\"Cider\"},{\"strIngredient1\":\"Sprite\"},{\"strIngredient1\":\"7-Up\"},{\"strIngredient1\":\"Blackberry brandy\"},{\"strIngredient1\":\"Peppermint schnapps\"},{\"strIngredient1\":\"Creme de Cassis\"}]}"
const val FAKE_JSON_RESPONSE_2 =
    "{\"ingredients\":[{\"idIngredient\":\"552\",\"strIngredient\":\"Elderflower cordial\",\"strDescription\":\"Elderflower cordial is a soft drink made largely from a refined sugar and water solution and uses the flowers of the European elderberry. Historically the cordial has been popular in North Western Europe where it has a strong Victorian heritage.\",\"strType\":\"Cordial\",\"strAlcohol\":null,\"strABV\":null}]}"

@RunWith(MockitoJUnitRunner::class)
class IngredientApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockResponse: MockResponse

    private lateinit var ingredientApi: IngredientApi

    private lateinit var gson: Gson

    private lateinit var ingredientsApiEntity: IngredientsApiEntity

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        ingredientApi =
            MockRestService.mockRetrofit(mockWebServer).create(IngredientApi::class.java)

        mockResponse = MockRestService.mockResponse()

        gson = Gson()
    }

    @Test
    fun getAll() = runBlocking {
        mockResponse.setResponseCode(200).setBody(FAKE_JSON_RESPONSE_1)
        ingredientsApiEntity = gson.fromJson(FAKE_JSON_RESPONSE_1, IngredientsApiEntity::class.java)
        mockWebServer.enqueue(mockResponse)

        val getAll = ingredientApi.getAll()

        assertEquals(getAll.ingredientEntity.size, ingredientsApiEntity.ingredientEntity.size)
    }

    @Test
    fun getDetails() = runBlocking {
        mockResponse.setResponseCode(200).setBody(FAKE_JSON_RESPONSE_2)
        ingredientsApiEntity = gson.fromJson(FAKE_JSON_RESPONSE_2, IngredientsApiEntity::class.java)
        mockWebServer.enqueue(mockResponse)

        val getDetails = ingredientApi.getDetails(SEARCH_BY_INGREDIENT)

        assertEquals(getDetails.ingredientEntity.size, ingredientsApiEntity.ingredientEntity.size)
    }
}