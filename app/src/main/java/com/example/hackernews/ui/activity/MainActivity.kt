package com.example.hackernews.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.hackernews.R
import com.example.hackernews.ui.screen.MainScreen
import com.example.hackernews.ui.theme.HackerNewsTheme
import com.example.hackernews.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val items = viewModel.newsPager.collectAsLazyPagingItems()
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            val scrollToTopScoreItem = {
                coroutineScope.launch {
                    val itemList = items.itemSnapshotList.items
                    val highestScoreItem = itemList.maxBy { it.score ?: 0 }
                    val highestScoreIndex =
                        itemList.indexOfFirst { it.score == highestScoreItem.score }
                    listState.animateScrollToItem(index = highestScoreIndex)
                }
            }
            HackerNewsTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(text = stringResource(id = R.string.app_name))
                            },
                            actions = {
                                IconButton(onClick = { scrollToTopScoreItem.invoke() }) {
                                    Icon(
                                        imageVector = Icons.Filled.KeyboardArrowUp,
                                        contentDescription = "Most highly ranked article"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    MainScreen(items = items, modifier = Modifier.padding(it), state = listState)
                }
            }
        }
    }
}