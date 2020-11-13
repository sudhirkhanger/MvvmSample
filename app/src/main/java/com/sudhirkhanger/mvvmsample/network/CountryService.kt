package com.sudhirkhanger.mvvmsample.network

import com.sudhirkhanger.mvvmsample.ui.CountriesResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CountryService {

    @GET("28fabb9d-e60f-11ea-b735-4b96f0d0a707")
    suspend fun countries(): Response<CountriesResponse>

    companion object {
        private const val BASE_URL = "https://jsonblob.com/api/jsonBlob/"

        fun create(): CountryService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CountryService::class.java)
        }
    }
}