package com.example.hackernews.domain.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hackernews.data.api.HackerNewsApi
import com.example.hackernews.domain.datasource.HackerNewsDataSource
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
class HackerNewsRepositoryTest {
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
    fun testFetchNewsStories(): Unit = runBlocking {
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

        val newsItems = repository.fetchNewsStories(0, items.size)
        assertThat(newsItems.size, equalTo(items.size))
        newsItems.forEachIndexed { index, item ->
            assertThat(item, equalTo(items[index]))
        }
    }
}