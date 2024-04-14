package ui.screen.moodRate

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import data.EmojiList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import ui.component.MoodGridItem
import ui.component.MoodRateView
import ui.screen.emojis.model.EmojiUiModel
import ui.viewmodel.MoodStateScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodStateBottomSheet(
    onDismiss: () -> Unit = {},
    selectedDateTimeStamp: Long = 0L,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
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
    listState: LazyListState = rememberLazyListState(),
) {

    val viewModel = koinInject<MoodStateScreenViewModel>()
    val emojiList by viewModel.emojiListStateFlow.collectAsState()
    val moodRate by viewModel.moodRate.collectAsState()

    val tz = TimeZone.currentSystemDefault()
    val dateTime = Instant.fromEpochMilliseconds(selectedTimeStamp).toLocalDateTime(tz)
    val shortDayName = dateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercaseChar() }
    val monthName = dateTime.month.name.lowercase().replaceFirstChar { it.uppercaseChar() }
    val formattedSelectedDate = remember {
        shortDayName + ", " +
                dateTime.dayOfMonth.toString() + " " + monthName + " " +
                dateTime.year
    }

    LaunchedEffect(Unit) {
        viewModel.getEmojiByTimeStampRange(selectedTimeStamp)
    }

    Surface {
        Column(
            modifier = modifier.then(Modifier.fillMaxSize()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MoodRateView(
                modifier = Modifier.fillMaxWidth(),
                moodRate = moodRate,
                loadingState = false
            )
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
                formattedSelectedDate = formattedSelectedDate,
                emojiHistoryList = emojiList.toPersistentList()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun EmojiListByDate(
    lazyListState: LazyGridState,
    formattedSelectedDate: String,
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
