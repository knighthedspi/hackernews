package com.example.hackernews.ui.composable

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hackernews.data.model.HackerNewsItem
import com.example.hackernews.ui.theme.Typography

@Composable
fun HackerNewsItem(
    item: HackerNewsItem,
    modifier: Modifier = Modifier
) {
    val currentTime = System.currentTimeMillis()
    val itemCreatedTime = item.time * 1000
    val differentTime = currentTime - itemCreatedTime
    val timeAgo = DateUtils.getRelativeTimeSpanString(
        itemCreatedTime,
        currentTime,
        when {
            differentTime < DateUtils.MINUTE_IN_MILLIS -> DateUtils.SECOND_IN_MILLIS
            DateUtils.MINUTE_IN_MILLIS < differentTime && differentTime <= DateUtils.HOUR_IN_MILLIS -> DateUtils.MINUTE_IN_MILLIS
            DateUtils.HOUR_IN_MILLIS < differentTime && differentTime <= DateUtils.DAY_IN_MILLIS -> DateUtils.HOUR_IN_MILLIS
            DateUtils.DAY_IN_MILLIS < differentTime && differentTime <= DateUtils.WEEK_IN_MILLIS -> DateUtils.DAY_IN_MILLIS
            else -> DateUtils.WEEK_IN_MILLIS
        }
    )
    val domain = item.url?.let {
        val regex = Regex("^(https?://)?(?:www\\.)?(\\w+)")
        val matchResult = regex.find(it)
        matchResult?.groupValues?.get(2)
    }
    Column(modifier = modifier) {
        val score = item.score ?: 0
        Text(text = item.title.orEmpty(), style = Typography.headlineSmall)
        Text(text = "($domain)", style = Typography.titleLarge)
        Text(text = "$score ${if (score > 1) "points" else "point"} by ${item.by} $timeAgo ", style = Typography.labelSmall)
        item.descendants?.let { descendants ->
            if (descendants > 0) {
                Text(
                    text = if (descendants > 1) "$descendants comments" else "$descendants comment",
                    style = Typography.labelSmall
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HackerNewsItemPreview() {
    HackerNewsItem(
        item = HackerNewsItem(
            id = 38680394,
            deleted = null,
            type = "story",
            by = "the-dude",
            time = 1702889048,
            text = null,
            dead = null,
            parent = null,
            poll = null,
            kids = null,
            url = "https://www.investors.com/news/electric-vehicle-subsidies-germany-ends-ev-bonus-abruptly-in-latest-blow-to-tesla/",
            score = 3,
            title = "Germany Ends Electric Vehicle Subsidies Abruptly in Latest Blow to Tesla",
            parts = null,
            descendants = 1
        )
    )
}