package ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import ui.extension.bouncingClickable

@Composable
private fun CalendarCell(
    date: LocalDate,
    signal: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = date.dayOfMonth.toString()
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .padding(2.dp)
            .background(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
            )
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        if (signal) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.errorContainer
                    )
            )
        }
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

fun getWeekDays(startFromSunday: Boolean): ImmutableList<Int> {
    val lista = (1..7).toList()
    return (if (startFromSunday) lista.take(7) + lista.drop(1) else lista).toImmutableList()
}

private fun Int.getDayOfWeek3Letters(): String {
    return DayOfWeek(
        this@getDayOfWeek3Letters
    ).name.take(3).lowercase().replaceFirstChar { it.uppercaseChar() }
}

@Composable
private fun WeekdayCell(weekday: Int, modifier: Modifier = Modifier) {
    val text = weekday.getDayOfWeek3Letters()
    Box(
        modifier = modifier.fillMaxSize().padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun CalendarGrid(
    date: ImmutableList<Pair<LocalDate, Boolean>>,
    onClick: (LocalDate) -> Unit,
    startFromSunday: Boolean,
    modifier: Modifier = Modifier,
) {
    val year = date.first().first.year
    val monthNumber = date.first().first.monthNumber
    val dayOfMonth = date.first().first.dayOfMonth
    val day1ThisMonth = date.first().first.minus(dayOfMonth - 1, DateTimeUnit.DAY)
    val diffDaysLastMonth = kotlin.math.abs(day1ThisMonth.dayOfWeek.isoDayNumber - 1)
    val weekdays = getWeekDays(startFromSunday)

    CalendarCustomLayout(modifier = modifier) {
        weekdays.forEach {
            WeekdayCell(weekday = it)
        }

        // Adds Spacers to align the first day of the month to the correct weekday
        repeat(diffDaysLastMonth) {
            Spacer(modifier = Modifier.fillMaxSize())
        }

        for (day in 1..if (startFromSunday) dayOfMonth else dayOfMonth - 1) {
            Box(
                modifier = Modifier
                    .bouncingClickable {
                        onClick(LocalDate(year = year, monthNumber = monthNumber, dayOfMonth = day))
                    }
                    .padding(2.dp)
                    .background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$day")
            }
        }
        date.forEach {
            CalendarCell(
                date = it.first,
                signal = it.second,
                onClick = { onClick(it.first) }
            )
        }
    }
}

@Composable
private fun CalendarCustomLayout(
    modifier: Modifier = Modifier,
    horizontalGapDp: Dp = 2.dp,
    verticalGapDp: Dp = 2.dp,
    content: @Composable () -> Unit,
) {
    val horizontalGap = with(LocalDensity.current) {
        horizontalGapDp.roundToPx()
    }
    val verticalGap = with(LocalDensity.current) {
        verticalGapDp.roundToPx()
    }
    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val totalWidthWithoutGap = constraints.maxWidth - (horizontalGap * 6)
        val singleWidth = totalWidthWithoutGap / 7

        val xPos: MutableList<Int> = mutableListOf()
        val yPos: MutableList<Int> = mutableListOf()
        var currentX = 0
        var currentY = 0
        measurables.forEach { _ ->
            xPos.add(currentX)
            yPos.add(currentY)
            if (currentX + singleWidth + horizontalGap > totalWidthWithoutGap) {
                currentX = 0
                currentY += singleWidth + verticalGap
            } else {
                currentX += singleWidth + horizontalGap
            }
        }

        val placeables: List<Placeable> = measurables.map { measurable ->
            measurable.measure(constraints.copy(maxHeight = singleWidth, maxWidth = singleWidth))
        }

        layout(
            width = constraints.maxWidth,
            height = currentY + singleWidth + verticalGap,
        ) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = xPos[index],
                    y = yPos[index],
                )
            }
        }
    }
}

@Composable
fun CalendarView(
    month: LocalDate,
    date: ImmutableList<Pair<LocalDate, Boolean>>?,
    displayNext: Boolean = false,
    displayPrev: Boolean = false,
    onClickNext: () -> Unit = {},
    onClickPrev: () -> Unit = {},
    onClick: (LocalDate) -> Unit,
    startFromSunday: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)) {
            val monthYearLabel =
                month.month.name.lowercase().replaceFirstChar { it.uppercaseChar() } +
                        " " + month.year
            Text(
                text = monthYearLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.align(Alignment.Center).padding(start = 16.dp),
            )
        }
        if (!date.isNullOrEmpty()) {
            CalendarGrid(
                date = date,
                onClick = onClick,
                startFromSunday = startFromSunday,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}