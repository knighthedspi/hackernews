package com.example.hackernews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.hackernews.domain.paging.HackerNewsPagingSource
import com.example.hackernews.domain.repository.HackerNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: HackerNewsRepository
) : ViewModel() {
    companion object {
        const val PAGE_SIZE = 10
    }

    private lateinit var pagingSource: HackerNewsPagingSource

    val newsPager = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        HackerNewsPagingSource(repository).also { pagingSource = it }
    }.flow

    fun invalidateDataSource() {
        pagingSource.invalidate()
    }
}