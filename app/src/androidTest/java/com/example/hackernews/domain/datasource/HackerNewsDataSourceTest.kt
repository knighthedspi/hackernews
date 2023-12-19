package com.example.hackernews.domain.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hackernews.data.api.HackerNewsApi
import com.example.hackernews.domain.model.Resource
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
class HackerNewsDataSourceTest {
    @get:Rule
    val rule = HiltAndroidRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var api: HackerNewsApi

    private lateinit var dataSource: HackerNewsDataSource

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        dataSource = HackerNewsDataSource(api, Dispatchers.IO)
    }

    @Test
    fun testGetStories(): Unit = runBlocking {
        val ids = items.map { it.id }
        Mockito.`when`(
            api.getNewsStories()
        ).thenReturn(
            Response.success(ids)
        )

        val result = dataSource.getNewsStories()
        assertThat(result.status, equalTo(Resource.Status.SUCCESS))

        val data = result.data
        assertThat(data?.size, equalTo(ids.size))
        data?.forEachIndexed { index, id ->
            assertThat(id, equalTo(ids[index]))
        }
    }

    @Test
    fun testGetNewsItems(): Unit = runBlocking {
        val id = 0L
        Mockito.`when`(
            api.getNewsItem(id)
        ).thenReturn(
            Response.success(items[id.toInt()])
        )

        val result = dataSource.getNewsItem(id)
        assertThat(result.status, equalTo(Resource.Status.SUCCESS))

        val data = result.data
        assertThat(data, equalTo(items[id.toInt()]))
    }

}