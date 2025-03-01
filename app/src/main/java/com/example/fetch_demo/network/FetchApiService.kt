package com.example.fetch_demo.network

import com.example.fetch_demo.data.FetchDemoDataItem
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface FetchApiService {
    @GET("hiring.json")
    suspend fun fetchData(): Response<List<FetchDemoDataItem>>

    companion object {
        private const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"

        fun create(): FetchApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FetchApiService::class.java)
        }
    }
}