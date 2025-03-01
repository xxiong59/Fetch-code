package com.example.fetch_demo.data

import android.util.Log
import com.example.fetch_demo.network.FetchApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchDataRepository(private val fetchApiService: FetchApiService): BaseRepository {
    override suspend fun fetchData(): Result<Map<Int, List<FetchDemoDataItem>>> {
        return try {
            withContext(Dispatchers.IO) {
                try {
                    val response = fetchApiService.fetchData()
                    if (response.isSuccessful) {
                        val data = response.body() ?: emptyList()
                        val filteredData = data.filter { !it.name.isNullOrEmpty() }
                        val sortedData = filteredData.sortedWith(
                            compareBy<FetchDemoDataItem> {it.listId}
                                .thenBy {
                                    val name = it.name ?: ""
                                    val numberStr = name.replace("Item ", "").trim()
                                    try {
                                        numberStr.toInt()
                                    } catch (e: NumberFormatException) {
                                        print(e)
                                        0
                                    }
                                }
                        )
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