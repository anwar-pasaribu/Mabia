package com.unwur.mabiaho.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import data.EmojiList
import ui.component.bubble.IconRounded
import ui.component.bubble.WatchGridLayout
import ui.theme.MyAppTheme

@Preview(showBackground = true)
@Composable
fun WatchGridLayoutPreview() {
    MyAppTheme {
        WatchGridLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            rowItemsCount = 5,
            itemSize = 80.dp
        ) {
            EmojiList.generateEmojiForUI().forEach { res ->
                IconRounded(
                    emojiUnicode = res.emojiUnicode
                )
            }
        }
    }
}