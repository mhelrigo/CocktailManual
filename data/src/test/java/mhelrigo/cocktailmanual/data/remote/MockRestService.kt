package mhelrigo.cocktailmanual.data.remote

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MockRestService() {
    companion object Factory {
        fun mockRetrofit(mockWebServer: MockWebServer): Retrofit =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(mockWebServer.url("/"))
                .build()

        fun mockResponse(): MockResponse =
            MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
    }
}