package ui.screen.moodRate

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import data.EmojiList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import ui.component.MoodGridItem
import ui.component.MoodRateView
import ui.screen.emojis.model.EmojiUiModel
import ui.screen.onboarding.MoodRatePagerDisplay
import ui.viewmodel.MoodStateScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodStateBottomSheet(
    onDismiss: () -> Unit = {},
    showFullScreen: Boolean = true,
    selectedDateTimeStamp: Long = 0L,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = showFullScreen
    )

    val noInset = BottomSheetDefaults.windowInsets
        .only(WindowInsetsSides.Bottom)
        .exclude(WindowInsets.navigationBars)

    val alphaAnimatable = remember { Animatable(0F) }

    LaunchedEffect(modalBottomSheetState.targetValue) {
        if (modalBottomSheetState.targetValue == SheetValue.PartiallyExpanded) {
            alphaAnimatable.animateTo(1F)
        } else {
            alphaAnimatable.animateTo(0F)
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                modifier = Modifier.alpha(alphaAnimatable.value)
            )
        },
        windowInsets = noInset,
    ) {
        MoodStateScreen(
            modifier = Modifier,
            selectedTimeStamp = selectedDateTimeStamp,
        )
    }
}

@Composable
fun MoodStateScreen(
    modifier: Modifier,
    emojiGridListState: LazyGridState = rememberLazyGridState(),
    selectedTimeStamp: Long = 0L,
) {

    val viewModel = koinInject<MoodStateScreenViewModel>()
    val emojiList by viewModel.emojiListStateFlow.collectAsState()
    val moodRate by viewModel.moodRate.collectAsState()
    var toggleMoodRatePagerView by remember { mutableStateOf(false) }

    val targetDateTimeStamp =
        if (selectedTimeStamp != 0L) selectedTimeStamp
        else Clock.System.now().toEpochMilliseconds()

    val tz = TimeZone.currentSystemDefault()
    val dateTime = Instant.fromEpochMilliseconds(targetDateTimeStamp).toLocalDateTime(tz)
    val shortDayName = dateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercaseChar() }
    val monthName = dateTime.month.name.lowercase().replaceFirstChar { it.uppercaseChar() }
    val formattedSelectedDate = remember {
        shortDayName + ", " +
                dateTime.dayOfMonth.toString() + " " + monthName + " " +
                dateTime.year
    }

    LaunchedEffect(Unit) {
        viewModel.getEmojiByTimeStampRange(targetDateTimeStamp)
    }

    Surface {
        Column(
            modifier = modifier.then(Modifier.fillMaxSize()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) {

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Mood Kamu",
                    style = MaterialTheme.typography.headlineMedium
                )

                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp),
                    onClick = {
                        toggleMoodRatePagerView = !toggleMoodRatePagerView
                    }
                ) {
                    Icon(
                        imageVector = if (toggleMoodRatePagerView) Icons.Default.Close else Icons.Default.Info,
                        contentDescription = "Toggle Mood Rate"
                    )
                }
            }

            AnimatedContent(
                targetState = toggleMoodRatePagerView,
                transitionSpec = {
                    (fadeIn()).togetherWith(fadeOut())
                }
            ) {
                if (toggleMoodRatePagerView) {
                    Column {
                        MoodRatePagerDisplay(
                            selectedMoodStateIndex = moodRate - 1
                        )
                    }
                } else {
                    Column {
                        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                            MoodRateView(
                                modifier = Modifier.fillMaxWidth(),
                                moodRate = moodRate,
                                loadingState = false
                            )
                        }
                        val moodLabel = EmojiList.moodPleasantness[moodRate].orEmpty()
                        AnimatedContent(
                            targetState = moodLabel,
                            label = "AnimatedMoodSummaryBottomSheet"
                        ) { targetVal ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = targetVal,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = formattedSelectedDate,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        EmojiListByDate(
                            lazyListState = emojiGridListState,
                            emojiHistoryList = emojiList.toPersistentList()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmojiListByDate(
    lazyListState: LazyGridState,
    emojiHistoryList: ImmutableList<EmojiUiModel>,
) {

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        columns = GridCells.Adaptive(72.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = emojiHistoryList, key = { it.id }) { item ->
            MoodGridItem(
                content = item.emojiUnicode,
            )
        }

        item {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
        }
    }
}
