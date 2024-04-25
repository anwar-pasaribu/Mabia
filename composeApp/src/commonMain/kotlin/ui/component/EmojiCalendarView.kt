package ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import data.EmojiList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import ui.extension.bouncingClickable
import ui.screen.emojis.model.DayEmojiData
import ui.screen.emojis.model.MonthEmojiData

@Composable
private fun EmojiCalendarCell(
    cellText: String,
    cellEmoji: String,
    cellEmojiBubbleColor: Color = Color.White,
    signal: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cellExtraContent: @Composable() (BoxScope.() -> Unit) = {},
) {
    val circleShape = remember { CircleShape }
    val cellEmojiBubbleColorWithAlpha = remember { cellEmojiBubbleColor.copy(alpha = .5F) }
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .bouncingClickable { onClick() }
                .aspectRatio(1f)
                .fillMaxSize()
                .padding(2.dp)
                .clip(circleShape)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                )
        ) {
            if (signal) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer
                        )
                )
            }
            Text(
                text = cellText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        cellExtraContent()

        Box(
            modifier = Modifier
                .graphicsLayer {
                    shape = CircleShape
                    shadowElevation = 8F
                }
                .clip(CircleShape)
                .fillMaxSize(.45F)
                .zIndex(11F)
                .background(Color.White, circleShape)
                .align(Alignment.BottomEnd),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(cellEmojiBubbleColorWithAlpha, circleShape)
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    textAlign = TextAlign.Center,
                    text = cellEmoji,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
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
private fun EmojiCalendarGrid(
    monthEmojiData: MonthEmojiData,
    onClick: (DayEmojiData) -> Unit,
    startFromSunday: Boolean,
    modifier: Modifier = Modifier,
    cellExtraContent: @Composable BoxScope.() -> Unit = {}
) {
    val date = monthEmojiData.month
    val dayOfMonth = date.dayOfMonth
    val day1ThisMonth = date.minus(dayOfMonth - 1, DateTimeUnit.DAY)
    val diffDaysLastMonth = kotlin.math.abs(day1ThisMonth.dayOfWeek.isoDayNumber - 1)
    val weekdays = remember { getWeekDays(startFromSunday) }

    EmojiCalendarCustomLayout(modifier = modifier) {
        weekdays.forEach {
            WeekdayCell(weekday = it)
        }

        // Adds Spacers to align the first day of the month to the correct weekday
        repeat(diffDaysLastMonth) {
            Spacer(modifier = Modifier.fillMaxSize())
        }

        monthEmojiData.dailyEmojis.forEach {
            EmojiCalendarCell(
                cellText = it.day.dayOfMonth.toString(),
                cellEmoji = it.emoji,
                cellEmojiBubbleColor = EmojiList.getBackgroundColorForMood(it.moodRateValue).first(),
                signal = it.day.dayOfYear == Clock.System.todayIn(TimeZone.currentSystemDefault()).dayOfYear,
                onClick = { onClick(it) },
                cellExtraContent = cellExtraContent
            )
        }
    }
}

@Composable
private fun EmojiCalendarCustomLayout(
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
        var isFirstRow = true // Flag to indicate whether it's the first row
        measurables.forEachIndexed { _, _ ->
            xPos.add(currentX)
            yPos.add(currentY)

            // Adjust height for the first row
            val cellHeight = if (isFirstRow) singleWidth / 2 else singleWidth

            if (currentX + singleWidth + horizontalGap > totalWidthWithoutGap) {
                currentX = 0
                currentY += cellHeight + verticalGap
                isFirstRow = false // After the first row, set the flag to false
            } else {
                currentX += singleWidth + horizontalGap
            }
        }

        val placeables: List<Placeable> = measurables.mapIndexed { index, measurable ->
            measurable.measure(
                constraints.copy(
                    maxHeight = if (index <= 6) singleWidth / 2 else singleWidth,
                    maxWidth = singleWidth
                )
            )
        }

        val calendarLayoutHeight =
            if (placeables.size != 42) currentY + singleWidth + verticalGap
            else currentY + verticalGap
        layout(
            width = constraints.maxWidth,
            height = calendarLayoutHeight,
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
fun EmojiCalendarView(
    monthEmojiData: MonthEmojiData,
    onClick: (DayEmojiData) -> Unit,
    startFromSunday: Boolean = false,
    modifier: Modifier = Modifier,
    cellExtraContent: @Composable BoxScope.() -> Unit = {}
) {
    val month = monthEmojiData.month
    val monthYearLabel = remember {
        month.month.name.lowercase().replaceFirstChar { it.uppercaseChar() } +
                " " + month.year
    }
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = monthYearLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.align(Alignment.Center),
            )
        }
        if (!monthEmojiData.dailyEmojis.isEmpty()) {
            EmojiCalendarGrid(
                modifier = Modifier.padding(horizontal = 16.dp),
                monthEmojiData = monthEmojiData,
                onClick = onClick,
                startFromSunday = startFromSunday,
                cellExtraContent = cellExtraContent
            )
        }
    }
}