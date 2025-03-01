package com.example.fetch_demo.data

import com.example.fetch_demo.network.FetchApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository implementation that fetches and processes data from the Fetch API.
 * This repository handles fetching, filtering, sorting, and grouping of the data items.
 *
 * @property fetchApiService The API service used to make network requests.
 */
class FetchDataRepository(private val fetchApiService: FetchApiService): BaseRepository {
    /**
     * Fetches data from the API, processes it, and returns it grouped by listId.
     * Processing includes:
     * 1. Filtering out items with null or empty names
     * 2. Sorting items by listId, then by the numeric value in the name ("Item X")
     * 3. Grouping items by listId
     *
     * @return A Result containing either:
     *         - Success with a Map of listId to the sorted list of items in that group
     *         - Failure with the exception that occurred during fetching or processing
     */
    override suspend fun fetchData(): Result<Map<Int, List<FetchDemoDataItem>>> {
        return try {
            withContext(Dispatchers.IO) {
                try {
                    val response = fetchApiService.fetchData()
                    if (response.isSuccessful) {
                        // Get the data or default to empty list if null
                        val data = response.body() ?: emptyList()

                        // Filter out items with null or empty names
                        val filteredData = data.filter { !it.name.isNullOrEmpty() }

                        // Sort by listId first, then by the number in the item name
                        val sortedData = filteredData.sortedWith(
                            compareBy<FetchDemoDataItem> {it.listId}
                                .thenBy {
                                    val name = it.name ?: ""
                                    val numberStr = name.replace("Item ", "").trim()
                                    try {
                                        // Extract and parse the number from "Item X" format
                                        numberStr.toInt()
                                    } catch (e: NumberFormatException) {
                                        print(e)
                                        0  // Default to 0 if parsing fails
                                    }
                                }
                        )

                        // Group the sorted items by listId
                        val groupedData = sortedData.groupBy { it.listId }
                        Result.success(groupedData)
                    } else {
                        Result.failure(Exception("Failed to fetch data: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Result.failure(e)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}