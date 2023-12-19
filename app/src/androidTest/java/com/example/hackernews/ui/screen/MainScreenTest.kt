package com.example.hackernews.ui.screen

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hackernews.ui.theme.HackerNewsTheme
import com.example.hackernews.utils.items
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val activityRule = IntentsRule()

    @Test
    fun testRenderItems() {
        val tag = "testColumn"
        composeTestRule.setContent {
            val listState = rememberLazyListState()
            val items = MutableStateFlow(PagingData.from(items)).collectAsLazyPagingItems()
            HackerNewsTheme {
                MainScreen(items = items, modifier = Modifier.testTag(tag), state = listState)
            }
        }

        // test if items are rendered correctly
        val lazyColumn = composeTestRule.onNodeWithTag(tag)
        lazyColumn.assertIsDisplayed()
        lazyColumn.onChildren().assertCountEquals(items.size)
        items.forEachIndexed { index, item ->
            item.title?.let {
                lazyColumn.onChildAt(index).assertTextContains(it)
            }
        }

        // test to verify if the user clicks on the first item, the correct link is opened in the browser.
        val testIndex = 0
        lazyColumn.onChildAt(testIndex).performClick()
        intending(
            allOf(
                not(isInternal()),
                hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(items[testIndex].url))
            )
        ).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
    }
}