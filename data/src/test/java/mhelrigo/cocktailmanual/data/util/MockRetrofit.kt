package mhelrigo.cocktailmanual.data.util

import mhelrigo.cocktailmanual.data.BuildConfig
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MockRetrofit() {
    companion object Factory {
        lateinit var mockWebServer: MockWebServer
        fun mockRetrofit() : Retrofit =
             Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mockWebServer.url("https://www.thecocktaildb.com/api/json/v2/${BuildConfig.API_KEY}/"))
                .build()

    }
}