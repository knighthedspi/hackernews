package com.example.hackernews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.hackernews.domain.paging.HackerNewsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pagingSource: HackerNewsPagingSource
) : ViewModel() {
    companion object {
        const val PAGE_SIZE = 10
    }

    val newsPager = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        pagingSource
    }.flow
}