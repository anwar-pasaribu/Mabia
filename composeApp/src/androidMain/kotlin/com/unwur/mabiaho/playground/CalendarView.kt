package com.unwur.mabiaho.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import data.EmojiList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlinx.datetime.until
import ui.component.EmojiCalendarView
import ui.screen.emojis.model.DayEmojiData
import ui.screen.emojis.model.MonthEmojiData
import ui.theme.MyAppTheme

@Preview(showBackground = true)
@Composable
private fun CalenderViewPreview() {
    MyAppTheme {
        Column(
            modifier = Modifier
                .verticalScroll(
                    rememberScrollState()
                )
                .fillMaxSize(),
        ) {

            val today: LocalDate = Clock.System.todayIn(
                TimeZone.currentSystemDefault()
            )
            val calendarEmojis = mutableListOf<MonthEmojiData>()
            for (monthNum in 1..today.monthNumber) {
                val start = LocalDate(year = today.year, monthNumber = monthNum, dayOfMonth = 1)
                val thisMonth = today.monthNumber == monthNum
                val end = if (thisMonth) {
                    start.plus(today.dayOfMonth, DateTimeUnit.DAY)
                } else {
                    start.plus(1, DateTimeUnit.MONTH)
                }
                val totalDayCountInTheMonth = start.until(end, DateTimeUnit.DAY)

                val dailyEmojiList = mutableListOf<DayEmojiData>()
                for (day in start.dayOfMonth..totalDayCountInTheMonth) {
                    val emoji = EmojiList.generateEmojiForUI().random().emojiUnicode
                    dailyEmojiList.add(
                        DayEmojiData(
                            day = LocalDate(
                                year = today.year,
                                monthNumber = monthNum,
                                dayOfMonth = day
                            ),
                            emoji = emoji,
                            1
                        )
                    )
                }
                calendarEmojis.add(
                    MonthEmojiData(month = start, dailyEmojis = dailyEmojiList.toImmutableList())
                )
            }

            calendarEmojis.forEach { calendarEmojiItem ->
                EmojiCalendarView(
                    monthEmojiData = calendarEmojiItem,
                    onClick = {},
                    startFromSunday = false,
                    cellExtraContent = {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 2.dp)
                                .size(6.dp)
                                .background(Color.Red, CircleShape)
                                .align(Alignment.BottomCenter)
                        )
                    }
                )
            }

//            val emoji = EmojiList.generateEmojiForUI().random().emojiUnicode
//            val todayMonthNumber = today.monthNumber
//            val localDateListThisYear = mutableListOf<Pair<LocalDate, Boolean>>()
//            val start = LocalDate(year = today.year, monthNumber = todayMonthNumber, dayOfMonth = 1)
//            val end = start.plus(today.dayOfMonth, DateTimeUnit.DAY)
//            val totalDayCountInTheMonth = start.until(end, DateTimeUnit.DAY)
//            for (day in start.dayOfMonth..totalDayCountInTheMonth) {
//                localDateListThisYear.add(
//                    LocalDate(
//                        year = today.year,
//                        monthNumber = todayMonthNumber,
//                        dayOfMonth = day
//                    ) to (day == today.dayOfMonth)
//                )
//            }
//            CalendarView(
//                month = today,
//                date = localDateListThisYear.toImmutableList(),
//                onClick = {},
//                startFromSunday = false,
//                cellExtraContent = {
//                    val possiblePos = arrayListOf(
//                        Alignment.BottomEnd,
//                    )
//                    Box(
//                        modifier = Modifier
//                            .graphicsLayer {
//                                shape = CircleShape
//                                shadowElevation = 8F
//                            }
//                            .clip(CircleShape)
//                            .fillMaxSize(.45F)
//                            .zIndex(11F)
//                            .background(Color.White, CircleShape)
//                            .align(possiblePos.random()),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Box(modifier = Modifier.align(Alignment.Center)) {
//                            Text(
//                                modifier = Modifier.wrapContentSize(),
//                                textAlign = TextAlign.Center,
//                                text = emoji,
//                                style = MaterialTheme.typography.labelLarge
//                            )
//                        }
//                    }
//                }
//            )
        }
    }
}