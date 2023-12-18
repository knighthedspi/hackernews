package com.example.hackernews.domain.repository

import com.example.hackernews.data.model.HackerNewsItem
import com.example.hackernews.domain.datasource.HackerNewsDataSource
import com.example.hackernews.domain.model.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

class HackerNewsRepository @Inject constructor(
    private val dataSource: HackerNewsDataSource
) {
    /*
        fetch the news stories with descending date order, up to 30 news
        @param startIndex index of the first item in the news stories response
        @param limit num of items to fetch
        @return list of news items
     */
    suspend fun fetchNewsStories(startIndex: Int = 0, limit: Int): List<HackerNewsItem> {
        val result = mutableListOf<HackerNewsItem>()
        try {
            coroutineScope {
                val newsStoriesDeferred = async {
                    dataSource.getNewsStories()
                }
                val newsStoriesResult = newsStoriesDeferred.await()
                if (newsStoriesResult.status == Resource.Status.SUCCESS) {
                    val newsStories = newsStoriesResult.data ?: listOf()
                    val newsItemsResult = newsStories
                        .subList(
                            startIndex,
                            minOf(startIndex + limit - 1, newsStories.size - 1)
                        )
                        .map {
                            async {
                                Pair(dataSource.getNewsItem(it), it)
                            }
                        }
                        .awaitAll()
                    newsItemsResult.forEach {
                        val newsItemResource = it.first
                        val newsItemId = it.second
                        if (newsItemResource.status == Resource.Status.SUCCESS) {
                            newsItemResource.data?.let { newsItem ->
                                result.add(newsItem)
                            }
                        } else {
                            Timber.e("Cannot get news item at $newsItemId")
                        }
                    }
                } else {
                    Timber.e("Cannot get news stories")
                    return@coroutineScope
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return result
    }

}