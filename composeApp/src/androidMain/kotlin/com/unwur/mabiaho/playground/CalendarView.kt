package com.unwur.mabiaho.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import ui.component.CalendarView
import ui.theme.MyAppTheme

@Preview(showBackground = true)
@Composable
private fun CalenderViewPreview() {
    MyAppTheme {
        val today: LocalDate = Clock.System.todayIn(
            TimeZone.currentSystemDefault()
        ).plus(0, DateTimeUnit.MONTH)
        val buldep: LocalDate = Clock.System.todayIn(
            TimeZone.currentSystemDefault()
        ).minus(1, DateTimeUnit.MONTH)
        Column {

            CalendarView(
                month = today,
                date = listOf(
                    today to true,
                    today.plus(1, DateTimeUnit.DAY) to false
                ).toImmutableList(),
                displayNext = false,
                displayPrev = true,
                onClickNext = { /*TODO*/ },
                onClickPrev = { /*TODO*/ },
                onClick = {

                },
                startFromSunday = false
            )
            CalendarView(
                month = buldep,
                date = listOf(
                    buldep to true,
                    buldep.plus(1, DateTimeUnit.DAY) to false
                ).toImmutableList(),
                displayNext = false,
                displayPrev = true,
                onClickNext = { /*TODO*/ },
                onClickPrev = { /*TODO*/ },
                onClick = {

                },
                startFromSunday = false
            )
        }
    }
}