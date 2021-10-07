package mhelrigo.cocktailmanual.data.remote

import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import mhelrigo.cocktailmanual.data.entity.drink.DrinksApiEntity
import mhelrigo.cocktailmanual.data.repository.drink.remote.DrinkApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

const val SEARCH_BY_NAME_PARAM = "Margarita"
const val SEARCH_BY_INGREDIENT = "Gin"
const val DRINK_ID = "11007"
const val FAKE_JSON_RESPONSE =
    "{\"drinks\":[{\"idDrink\":\"11007\",\"strDrink\":\"Margarita\",\"strDrinkAlternate\":null,\"strTags\":\"IBA,ContemporaryClassic\",\"strVideo\":null,\"strCategory\":\"Ordinary Drink\",\"strIBA\":\"Contemporary Classics\",\"strAlcoholic\":\"Alcoholic\",\"strGlass\":\"Cocktail glass\",\"strInstructions\":\"Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.\",\"strInstructionsES\":null,\"strInstructionsDE\":\"Reiben Sie den Rand des Glases mit der Limettenscheibe, damit das Salz daran haftet. Achten Sie darauf, dass nur der \\u00e4u\\u00dfere Rand angefeuchtet wird und streuen Sie das Salz darauf. Das Salz sollte sich auf den Lippen des Genie\\u00dfers befinden und niemals in den Cocktail einmischen. Die anderen Zutaten mit Eis sch\\u00fctteln und vorsichtig in das Glas geben.\",\"strInstructionsFR\":null,\"strInstructionsIT\":\"Strofina il bordo del bicchiere con la fetta di lime per far aderire il sale.\\r\\nAvere cura di inumidire solo il bordo esterno e cospargere di sale.\\r\\nIl sale dovrebbe presentarsi alle labbra del bevitore e non mescolarsi mai al cocktail.\\r\\nShakerare gli altri ingredienti con ghiaccio, quindi versarli delicatamente nel bicchiere.\",\"strInstructionsZH-HANS\":null,\"strInstructionsZH-HANT\":null,\"strDrinkThumb\":\"https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/5noda61589575158.jpg\",\"strIngredient1\":\"Tequila\",\"strIngredient2\":\"Triple sec\",\"strIngredient3\":\"Lime juice\",\"strIngredient4\":\"Salt\",\"strIngredient5\":null,\"strIngredient6\":null,\"strIngredient7\":null,\"strIngredient8\":null,\"strIngredient9\":null,\"strIngredient10\":null,\"strIngredient11\":null,\"strIngredient12\":null,\"strIngredient13\":null,\"strIngredient14\":null,\"strIngredient15\":null,\"strMeasure1\":\"1 1\\/2 oz \",\"strMeasure2\":\"1\\/2 oz \",\"strMeasure3\":\"1 oz \",\"strMeasure4\":null,\"strMeasure5\":null,\"strMeasure6\":null,\"strMeasure7\":null,\"strMeasure8\":null,\"strMeasure9\":null,\"strMeasure10\":null,\"strMeasure11\":null,\"strMeasure12\":null,\"strMeasure13\":null,\"strMeasure14\":null,\"strMeasure15\":null,\"strImageSource\":\"https:\\/\\/commons.wikimedia.org\\/wiki\\/File:Klassiche_Margarita.jpg\",\"strImageAttribution\":\"Cocktailmarler\",\"strCreativeCommonsConfirmed\":\"Yes\",\"dateModified\":\"2015-08-18 14:42:59\"},{\"idDrink\":\"11118\",\"strDrink\":\"Blue Margarita\",\"strDrinkAlternate\":null,\"strTags\":null,\"strVideo\":null,\"strCategory\":\"Ordinary Drink\",\"strIBA\":null,\"strAlcoholic\":\"Alcoholic\",\"strGlass\":\"Cocktail glass\",\"strInstructions\":\"Rub rim of cocktail glass with lime juice. Dip rim in coarse salt. Shake tequila, blue curacao, and lime juice with ice, strain into the salt-rimmed glass, and serve.\",\"strInstructionsES\":null,\"strInstructionsDE\":\"Den Rand des Cocktailglases mit Limettensaft einreiben. Den Rand in grobes Salz tauchen. Tequila, blauen Curacao und Limettensaft mit Eis sch\\u00fctteln, in das mit Salz umh\\u00fcllte Glas abseihen und servieren.\",\"strInstructionsFR\":null,\"strInstructionsIT\":\"Strofinare il bordo del bicchiere da cocktail con succo di lime. Immergere il bordo nel sale grosso. Shakerare tequila, blue curacao e succo di lime con ghiaccio, filtrare nel bicchiere bordato di sale e servire.\",\"strInstructionsZH-HANS\":null,\"strInstructionsZH-HANT\":null,\"strDrinkThumb\":\"https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/bry4qh1582751040.jpg\",\"strIngredient1\":\"Tequila\",\"strIngredient2\":\"Blue Curacao\",\"strIngredient3\":\"Lime juice\",\"strIngredient4\":\"Salt\",\"strIngredient5\":null,\"strIngredient6\":null,\"strIngredient7\":null,\"strIngredient8\":null,\"strIngredient9\":null,\"strIngredient10\":null,\"strIngredient11\":null,\"strIngredient12\":null,\"strIngredient13\":null,\"strIngredient14\":null,\"strIngredient15\":null,\"strMeasure1\":\"1 1\\/2 oz \",\"strMeasure2\":\"1 oz \",\"strMeasure3\":\"1 oz \",\"strMeasure4\":\"Coarse \",\"strMeasure5\":null,\"strMeasure6\":null,\"strMeasure7\":null,\"strMeasure8\":null,\"strMeasure9\":null,\"strMeasure10\":null,\"strMeasure11\":null,\"strMeasure12\":null,\"strMeasure13\":null,\"strMeasure14\":null,\"strMeasure15\":null,\"strImageSource\":null,\"strImageAttribution\":null,\"strCreativeCommonsConfirmed\":\"Yes\",\"dateModified\":\"2015-08-18 14:51:53\"},{\"idDrink\":\"17216\",\"strDrink\":\"Tommy's Margarita\",\"strDrinkAlternate\":null,\"strTags\":\"IBA,NewEra\",\"strVideo\":null,\"strCategory\":\"Ordinary Drink\",\"strIBA\":\"New Era Drinks\",\"strAlcoholic\":\"Alcoholic\",\"strGlass\":\"Old-Fashioned glass\",\"strInstructions\":\"Shake and strain into a chilled cocktail glass.\",\"strInstructionsES\":null,\"strInstructionsDE\":\"Sch\\u00fctteln und in ein gek\\u00fchltes Cocktailglas abseihen.\",\"strInstructionsFR\":null,\"strInstructionsIT\":\"Shakerare e filtrare in una coppetta da cocktail ghiacciata.\",\"strInstructionsZH-HANS\":null,\"strInstructionsZH-HANT\":null,\"strDrinkThumb\":\"https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/loezxn1504373874.jpg\",\"strIngredient1\":\"Tequila\",\"strIngredient2\":\"Lime Juice\",\"strIngredient3\":\"Agave syrup\",\"strIngredient4\":null,\"strIngredient5\":null,\"strIngredient6\":null,\"strIngredient7\":null,\"strIngredient8\":null,\"strIngredient9\":null,\"strIngredient10\":null,\"strIngredient11\":null,\"strIngredient12\":null,\"strIngredient13\":null,\"strIngredient14\":null,\"strIngredient15\":null,\"strMeasure1\":\"4.5 cl\",\"strMeasure2\":\"1.5 cl\",\"strMeasure3\":\"2 spoons\",\"strMeasure4\":null,\"strMeasure5\":null,\"strMeasure6\":null,\"strMeasure7\":null,\"strMeasure8\":null,\"strMeasure9\":null,\"strMeasure10\":null,\"strMeasure11\":null,\"strMeasure12\":null,\"strMeasure13\":null,\"strMeasure14\":null,\"strMeasure15\":null,\"strImageSource\":null,\"strImageAttribution\":null,\"strCreativeCommonsConfirmed\":\"No\",\"dateModified\":\"2017-09-02 18:37:54\"},{\"idDrink\":\"16158\",\"strDrink\":\"Whitecap Margarita\",\"strDrinkAlternate\":null,\"strTags\":null,\"strVideo\":null,\"strCategory\":\"Other\\/Unknown\",\"strIBA\":null,\"strAlcoholic\":\"Alcoholic\",\"strGlass\":\"Margarita\\/Coupette glass\",\"strInstructions\":\"Place all ingredients in a blender and blend until smooth. This makes one drink.\",\"strInstructionsES\":null,\"strInstructionsDE\":\"Alle Zutaten in einen Mixer geben und mischen.\",\"strInstructionsFR\":null,\"strInstructionsIT\":\"Metti tutti gli ingredienti in un frullatore e frulla fino a che non diventa liscio.\",\"strInstructionsZH-HANS\":null,\"strInstructionsZH-HANT\":null,\"strDrinkThumb\":\"https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/srpxxp1441209622.jpg\",\"strIngredient1\":\"Ice\",\"strIngredient2\":\"Tequila\",\"strIngredient3\":\"Cream of coconut\",\"strIngredient4\":\"Lime juice\",\"strIngredient5\":null,\"strIngredient6\":null,\"strIngredient7\":null,\"strIngredient8\":null,\"strIngredient9\":null,\"strIngredient10\":null,\"strIngredient11\":null,\"strIngredient12\":null,\"strIngredient13\":null,\"strIngredient14\":null,\"strIngredient15\":null,\"strMeasure1\":\"1 cup \",\"strMeasure2\":\"2 oz \",\"strMeasure3\":\"1\\/4 cup \",\"strMeasure4\":\"3 tblsp fresh \",\"strMeasure5\":null,\"strMeasure6\":null,\"strMeasure7\":null,\"strMeasure8\":null,\"strMeasure9\":null,\"strMeasure10\":null,\"strMeasure11\":null,\"strMeasure12\":null,\"strMeasure13\":null,\"strMeasure14\":null,\"strMeasure15\":null,\"strImageSource\":null,\"strImageAttribution\":null,\"strCreativeCommonsConfirmed\":\"No\",\"dateModified\":\"2015-09-02 17:00:22\"},{\"idDrink\":\"12322\",\"strDrink\":\"Strawberry Margarita\",\"strDrinkAlternate\":null,\"strTags\":null,\"strVideo\":null,\"strCategory\":\"Ordinary Drink\",\"strIBA\":null,\"strAlcoholic\":\"Alcoholic\",\"strGlass\":\"Cocktail glass\",\"strInstructions\":\"Rub rim of cocktail glass with lemon juice and dip rim in salt. Shake schnapps, tequila, triple sec, lemon juice, and strawberries with ice, strain into the salt-rimmed glass, and serve.\",\"strInstructionsES\":null,\"strInstructionsDE\":\"Cocktailglasrand mit Zitronensaft und Tauchrand in Salz wenden. Schnaps, Tequila, Triple-Sec, Zitronensaft und Erdbeeren mit Eis sch\\u00fctteln, in das salzige Glas sieben und servieren.\",\"strInstructionsFR\":null,\"strInstructionsIT\":\"Strofinare il bordo del bicchiere da cocktail con succo di limone e immergerlo nel sale. Shakerare grappa, tequila, triple sec, succo di limone e fragole con ghiaccio, filtrare nel bicchiere bordato di sale e servire.\",\"strInstructionsZH-HANS\":null,\"strInstructionsZH-HANT\":null,\"strDrinkThumb\":\"https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/tqyrpw1439905311.jpg\",\"strIngredient1\":\"Strawberry schnapps\",\"strIngredient2\":\"Tequila\",\"strIngredient3\":\"Triple sec\",\"strIngredient4\":\"Lemon juice\",\"strIngredient5\":\"Strawberries\",\"strIngredient6\":\"Salt\",\"strIngredient7\":null,\"strIngredient8\":null,\"strIngredient9\":null,\"strIngredient10\":null,\"strIngredient11\":null,\"strIngredient12\":null,\"strIngredient13\":null,\"strIngredient14\":null,\"strIngredient15\":null,\"strMeasure1\":\"1\\/2 oz \",\"strMeasure2\":\"1 oz \",\"strMeasure3\":\"1\\/2 oz \",\"strMeasure4\":\"1 oz \",\"strMeasure5\":\"1 oz \",\"strMeasure6\":null,\"strMeasure7\":null,\"strMeasure8\":null,\"strMeasure9\":null,\"strMeasure10\":null,\"strMeasure11\":null,\"strMeasure12\":null,\"strMeasure13\":null,\"strMeasure14\":null,\"strMeasure15\":null,\"strImageSource\":null,\"strImageAttribution\":null,\"strCreativeCommonsConfirmed\":\"No\",\"dateModified\":\"2015-08-18 14:41:51\"},{\"idDrink\":\"178332\",\"strDrink\":\"Smashed Watermelon Margarita\",\"strDrinkAlternate\":null,\"strTags\":null,\"strVideo\":null,\"strCategory\":\"Cocktail\",\"strIBA\":null,\"strAlcoholic\":\"Alcoholic\",\"strGlass\":\"Collins glass\",\"strInstructions\":\"In a mason jar muddle the watermelon and 5 mint leaves together into a puree and strain. Next add the grapefruit juice, juice of half a lime and the tequila as well as some ice. Put a lid on the jar and shake. Pour into a glass and add more ice. Garnish with fresh mint and a small slice of watermelon.\",\"strInstructionsES\":null,\"strInstructionsDE\":null,\"strInstructionsFR\":null,\"strInstructionsIT\":\"In un barattolo di vetro pestare l'anguria e 5 foglie di menta insieme, filtrare il contenuto in un bicchiere. Quindi aggiungere il succo di pompelmo, il succo di mezzo lime, la tequila e un po 'di ghiaccio. Metti un coperchio sul barattolo e agita. Versare in un bicchiere e aggiungere altro ghiaccio. Guarnire con menta fresca e una fettina di anguria.\",\"strInstructionsZH-HANS\":null,\"strInstructionsZH-HANT\":null,\"strDrinkThumb\":\"https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/dztcv51598717861.jpg\",\"strIngredient1\":\"Watermelon\",\"strIngredient2\":\"Mint\",\"strIngredient3\":\"Grapefruit Juice\",\"strIngredient4\":\"Lime\",\"strIngredient5\":\"Tequila\",\"strIngredient6\":\"Watermelon\",\"strIngredient7\":\"Mint\",\"strIngredient8\":null,\"strIngredient9\":null,\"strIngredient10\":null,\"strIngredient11\":null,\"strIngredient12\":null,\"strIngredient13\":null,\"strIngredient14\":null,\"strIngredient15\":null,\"strMeasure1\":\"1\\/2 cup\",\"strMeasure2\":\"5\",\"strMeasure3\":\"1\\/3 Cup\",\"strMeasure4\":\"Juice of 1\\/2\",\"strMeasure5\":\"1 shot\",\"strMeasure6\":\"Garnish with\",\"strMeasure7\":\"Garnish with\",\"strMeasure8\":null,\"strMeasure9\":null,\"strMeasure10\":null,\"strMeasure11\":null,\"strMeasure12\":null,\"strMeasure13\":null,\"strMeasure14\":null,\"strMeasure15\":null,\"strImageSource\":null,\"strImageAttribution\":null,\"strCreativeCommonsConfirmed\":\"No\",\"dateModified\":null}]}"

@RunWith(MockitoJUnitRunner::class)
class DrinkApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockResponse: MockResponse

    private lateinit var drinkApi: DrinkApi

    private lateinit var drinksApiEntity: DrinksApiEntity

    private lateinit var gson: Gson

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        gson = Gson()
        drinkApi = MockRestService.mockRetrofit(mockWebServer).create(DrinkApi::class.java)

        mockResponse = MockRestService.mockResponse()
            .setResponseCode(200)
            .setBody(FAKE_JSON_RESPONSE)

        mockWebServer.enqueue(mockResponse)

        drinksApiEntity = gson.fromJson(FAKE_JSON_RESPONSE, DrinksApiEntity::class.java)
    }

    @Test
    fun getRandomly() = runBlocking {
        val getRandomly = drinkApi.getRandomly()

        assertEquals(getRandomly.drinkEntities.size, drinksApiEntity.drinkEntities.size)
    }

    @Test
    fun getByPopularity() = runBlocking {
        val getByPopularity = drinkApi.getByPopularity()

        assertEquals(getByPopularity.drinkEntities.size, drinksApiEntity.drinkEntities.size)
    }

    @Test
    fun getLatest() = runBlocking {
        val getLatest = drinkApi.getLatest()

        assertEquals(getLatest.drinkEntities.size, drinksApiEntity.drinkEntities.size)
    }

    @Test
    fun searchByName() = runBlocking {
        val searchByName = drinkApi.searchByName(SEARCH_BY_NAME_PARAM)

        assertEquals(searchByName.drinkEntities.size, drinksApiEntity.drinkEntities.size)
    }

    @Test
    fun searchByIngredient() = runBlocking {
        val searchByIngredient = drinkApi.searchByIngredient(SEARCH_BY_INGREDIENT)

        assertEquals(searchByIngredient.drinkEntities.size, drinksApiEntity.drinkEntities.size)
    }

    @Test
    fun getDetails() = runBlocking {
        val getDetails = drinkApi.getDetails(DRINK_ID)

        assertEquals(getDetails.drinkEntities.size, drinksApiEntity.drinkEntities.size)
    }
}