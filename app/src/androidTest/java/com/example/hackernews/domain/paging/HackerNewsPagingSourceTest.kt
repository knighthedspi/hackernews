package com.example.hackernews.domain.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hackernews.data.api.HackerNewsApi
import com.example.hackernews.domain.datasource.HackerNewsDataSource
import com.example.hackernews.domain.repository.HackerNewsRepository
import com.example.hackernews.utils.MainDispatcherRule
import com.example.hackernews.utils.items
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HackerNewsPagingSourceTest {
    @get:Rule
    val rule = HiltAndroidRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var api: HackerNewsApi

    private lateinit var dataSource: HackerNewsDataSource
    private lateinit var repository: HackerNewsRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        dataSource = HackerNewsDataSource(api, Dispatchers.IO)
        repository = HackerNewsRepository(dataSource)
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
        val ids = items.map { it.id }
        Mockito.`when`(
            api.getNewsStories()
        ).thenReturn(
            Response.success(ids)
        )
        ids.forEachIndexed { index, id ->
            Mockito.`when`(
                api.getNewsItem(id)
            ).thenReturn(
                Response.success(items[index])
            )
        }

        val source = HackerNewsPagingSource(repository)
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = items.size,
                placeholdersEnabled = false
            )
        )
        assertThat(
            result,
            equalTo(
                PagingSource.LoadResult.Page(
                    data = items,
                    prevKey = null,
                    nextKey = items.size
                )
            )
        )
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runBlocking {
        Mockito.`when`(
            api.getNewsStories()
        ).thenReturn(
            Response.success(listOf())
        )

        val source = HackerNewsPagingSource(repository)
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = items.size,
                placeholdersEnabled = false
            )
        )

        assertThat(
            result,
            equalTo(
                PagingSource.LoadResult.Page(
                    data = listOf(),
                    prevKey = null,
                    nextKey = items.size
                )
            )
        )
    }
}