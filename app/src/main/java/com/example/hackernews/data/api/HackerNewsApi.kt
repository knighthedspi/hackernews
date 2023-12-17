package com.example.hackernews.data.api

import com.example.hackernews.data.model.HackerNewsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HackerNewsApi {
    @GET("/v0/newstories.json")
    suspend fun getNewsStories(
        @Query("print") print: String = "pretty"
    ): Response<List<Int>>

    @GET("/v0/item/{id}.json")
    suspend fun getNewsItem(@Path("id") id: Int, @Query("print") print: String = "pretty"): Response<HackerNewsItem>
}