package com.example.fetch_demo.data

interface BaseRepository {
    suspend fun fetchData() : Result<Any>
}