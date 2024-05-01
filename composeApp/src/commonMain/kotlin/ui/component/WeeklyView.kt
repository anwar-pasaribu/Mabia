package ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import getScreenSizeInfo
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.extension.bouncingClickable
import ui.extension.delayedAlpha

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WeekView(
    modifier: Modifier = Modifier,
    onMonthNameClick: () -> Unit,
    onWeekDayClick: (Long) -> Unit,
) {
    val tz = TimeZone.currentSystemDefault()
    val today = Clock.System.todayIn(tz)
    Surface(
        modifier = modifier,
        color = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
        ) {

            val dateAtFirstOfWeek = today.minus(today.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
            val listOfDaysThisWeek = List(7) { index ->
                dateAtFirstOfWeek.plus(index, DateTimeUnit.DAY)
            }

            WeeklyViewGrid(
                upperLabel = today.year.toString(),
                lowerLabel = today.month.name.take(3),
                onClick = {
                    onMonthNameClick()
                }
            )

            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier.width(1.dp).height(32.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerHighest,
                        RoundedCornerShape(.5.dp)
                    )
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(10.dp))

            listOfDaysThisWeek.forEach { date ->
                val isToday = date.dayOfWeek.isoDayNumber == today.dayOfWeek.isoDayNumber
                val todayDayOfWeek = today.dayOfWeek.isoDayNumber
                val isFuture = date.dayOfWeek.isoDayNumber > todayDayOfWeek
                val dayOfMonth = date.dayOfMonth
                val upperLabel = date.dayOfWeek.name
                    .take(3).lowercase().replaceFirstChar { it.uppercaseChar() }
                val lowerLabel = dayOfMonth.toString()

                WeeklyViewGrid(
                    modifier = Modifier.alpha(if (isFuture) .4F else 1F),
                    upperLabel = upperLabel,
                    lowerLabel = lowerLabel,
                    isToday = isToday,
                    clickEnabled = !isFuture,
                    onClick = {
                        val nowTimeStamp = Clock.System.now().toLocalDateTime(tz).time
                        val dateTime = LocalDateTime(date = date, time = nowTimeStamp)
                        onWeekDayClick(dateTime.toInstant(tz).toEpochMilliseconds())
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun RowScope.WeeklyViewGrid(
    modifier: Modifier = Modifier,
    upperLabel: String,
    lowerLabel: String,
    isToday: Boolean = false,
    clickEnabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.then(
            Modifier
                .weight(1F)
                .clip(RoundedCornerShape(8.dp))
                .clickable(enabled = clickEnabled) {
                    onClick()
                }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = upperLabel,
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            modifier = Modifier.wrapContentWidth(),
            text = lowerLabel,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                )
            ),
            textAlign = TextAlign.Center
        )
        if (isToday) {
            Box(
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.inversePrimary,
                    RoundedCornerShape(1.5.dp)
                ).width(16.dp).height(3.dp),
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HorizontalMonthlyEmojiView(
    selectedEmoji: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var selectedEmojiUnicode = selectedEmoji
    Box(
        modifier = modifier.then(Modifier),
        contentAlignment = Alignment.Center
    ) {

        val today: LocalDate = Clock.System.todayIn(
            TimeZone.currentSystemDefault()
        )
        val todaysDayOfMonth = today.dayOfMonth
        val dateViewListState = rememberLazyListState()
        val dayOneToTodayDate = (todaysDayOfMonth downTo 1).toImmutableList()
        val halfScreenWidth = (getScreenSizeInfo().wDP / 2) - 22.dp

        LazyRow(
            modifier = Modifier.fillMaxWidth().size(56.dp),
            state = dateViewListState,
            reverseLayout = true,
            contentPadding = PaddingValues(
                start = 6.dp,
                top = 6.dp,
                end = halfScreenWidth,
                bottom = 6.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(dayOneToTodayDate) {
                Box(
                    modifier = Modifier
                        .bouncingClickable(true) {
                            onClick()
                        }
                        .clip(CircleShape)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .aspectRatio(1F),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "$it", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        LaunchedEffect(key1 = selectedEmojiUnicode) {
            dateViewListState.animateScrollToItem(0)
        }
        LaunchedEffect(key1 = selectedEmojiUnicode) {
            delay(300)
            selectedEmojiUnicode = ""
        }
        AnimatedContent(
            targetState = selectedEmojiUnicode,
            transitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            }
        ) { targetValue ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .aspectRatio(1F)
                    .delayedAlpha(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = targetValue,
                    modifier = Modifier.animateEnterExit(
                        enter = scaleIn(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            ),
                            initialScale = 2F
                        ),
                        exit = fadeOut()
                    ),
                    fontSize = TextUnit(32F, TextUnitType.Sp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}