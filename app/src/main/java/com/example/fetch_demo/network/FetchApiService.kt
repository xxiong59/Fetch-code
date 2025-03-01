package com.example.fetch_demo.network

import com.example.fetch_demo.data.FetchDemoDataItem
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Retrofit service interface for making API requests to the Fetch hiring endpoint.
 * Defines network operations for fetching data items.
 */
interface FetchApiService {
    /**
     * Fetches a list of data items from the "hiring.json" endpoint.
     *
     * @return A Retrofit Response object containing a list of FetchDemoDataItems if successful
     */
    @GET("hiring.json")
    suspend fun fetchData(): Response<List<FetchDemoDataItem>>

    companion object {
        /**
         * Base URL for the Fetch hiring API.
         */
        private const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"

        /**
         * Factory method to create an instance of FetchApiService.
         *
         * @return A configured implementation of the FetchApiService interface
         */
        fun create(): FetchApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FetchApiService::class.java)
        }
    }
}