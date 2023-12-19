package com.example.hackernews.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hackernews.data.model.HackerNewsItem
import com.example.hackernews.domain.repository.HackerNewsRepository
import timber.log.Timber
import javax.inject.Inject

class HackerNewsPagingSource @Inject constructor(
    private val repository: HackerNewsRepository
) : PagingSource<Int, HackerNewsItem>() {
    override fun getRefreshKey(state: PagingState<Int, HackerNewsItem>) =
        ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2).coerceAtLeast(0)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HackerNewsItem> {
        return try {
            val startIndex = params.key ?: 0
            val newsItems = repository.fetchNewsStories(startIndex, params.loadSize)
            LoadResult.Page(
                data = newsItems,
                prevKey = null,
                nextKey = startIndex + params.loadSize
            )
        } catch (e: Exception) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }
}