package com.example.hackernews.domain.datasource

import com.example.hackernews.data.api.HackerNewsApi
import com.example.hackernews.data.model.HackerNewsItem
import com.example.hackernews.domain.model.Resource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class HackerNewsDataSource @Inject constructor(
    private val hackerNewsApi: HackerNewsApi,
    private val dispatcher: CoroutineDispatcher
) : BaseDataSource() {
    suspend fun getNewsStories(): Resource<List<Int>> {
        val result = getResult(dispatcher) {
            hackerNewsApi.getNewsStories()
        }
        val errorMessage = "Get news stories error"
        return if (result.status == Resource.Status.SUCCESS) {
            val data = result.data
            if (data.isNullOrEmpty()) {
                Resource.error(result.errorCode, errorMessage)
            } else {
                Resource.success(data)
            }
        } else {
            Resource.error(result.errorCode, result.message ?: errorMessage)
        }
    }

    suspend fun getNewsItem(id: Int): Resource<HackerNewsItem> {
        val result = getResult(dispatcher) {
            hackerNewsApi.getNewsItem(id = id)
        }
        val errorMessage = "Get news item error"
        return if (result.status == Resource.Status.SUCCESS) {
            val data = result.data
            if (data == null) {
                Resource.error(result.errorCode, errorMessage)
            } else {
                Resource.success(data)
            }
        } else {
            Resource.error(result.errorCode, result.message ?: errorMessage)
        }
    }

}