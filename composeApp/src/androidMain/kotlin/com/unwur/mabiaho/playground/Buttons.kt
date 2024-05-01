package com.unwur.mabiaho.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ui.theme.MyAppTheme


@Preview(showBackground = true)
@Composable
private fun ButtonPrev() {
    MyAppTheme {

//        CloseButton(Modifier) {
//
//        }

        ProgressView()
    }
}

@Composable
fun ProgressView() {
    Box {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(
                    CircleShape
                )
                .background(Color.Black)
                .size(128.dp),
            contentAlignment = Alignment.Center
        ) {

            val strokeWidth = 5.dp

            CircularProgressIndicator(
                modifier = Modifier,
                color = Color.LightGray,
                strokeWidth = strokeWidth
            )
        }
    }
}