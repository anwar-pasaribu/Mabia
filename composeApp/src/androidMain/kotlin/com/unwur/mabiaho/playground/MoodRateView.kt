package com.unwur.mabiaho.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ui.component.MoodRateView
import ui.theme.MyAppTheme


@Preview
@Composable
private fun MoodPrev() {
    MyAppTheme {
        Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            MoodRateView(modifier = Modifier.wrapContentSize(), moodRate = 5)
        }
//        val moodList = (0..1).toList()
//        LazyColumn(modifier = Modifier.wrapContentWidth(), ) {
//            item {
//                MoodRateView(modifier = Modifier.fillMaxSize(), moodRate = 3)
//            }
//            items(moodList) {
//                MoodRateView(modifier = Modifier.fillMaxSize(), moodRate = it)
//            }
//        }
    }
}