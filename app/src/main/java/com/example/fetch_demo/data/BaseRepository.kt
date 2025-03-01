package com.example.fetch_demo.data

/**
 * Base repository interface defining the data operations for the application.
 * All concrete repository implementations should implement this interface.
 */
interface BaseRepository {
    /**
     * Fetches data from a data source.
     *
     * @return A Result object containing either the successfully fetched data or an error.
     *         The actual data type is determined by the implementing class.
     */
    suspend fun fetchData() : Result<Any>
}