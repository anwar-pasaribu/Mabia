package ui.screen.history

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import org.koin.compose.koinInject
import ui.component.ChatBubbleItem
import ui.component.EmojiCalendarView
import ui.screen.ai.model.ChatMessage
import ui.screen.moodRate.MoodStateBottomSheet
import androidx.compose.foundation.lazy.items as lazyColumnItems

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class,
)
@Composable
fun HistoryScreen(onBack: () -> Unit = {}) {

    val viewModel = koinInject<HistoryScreenViewModel>()
    val calendarList by viewModel.calenderListStateFlow.collectAsState()

    val hazeState = remember { HazeState() }
    val lazyColumnListState = rememberLazyListState()

    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCalenderData()
    }

    if (showSheet) {
        MoodStateBottomSheet(
            onDismiss = {
                showSheet = false
            },
            selectedDateTimeStamp = viewModel.selectedTimeStamp.collectAsState().value,
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth().hazeChild(
                    state = hazeState,
                    style = HazeMaterials.thin(MaterialTheme.colorScheme.background)
                ),
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                navigationIcon = {
                    IconButton(
                        onClick = { onBack() },
                        content = {
                            Icon(
                                painter = rememberVectorPainter(
                                    image = Icons.AutoMirrored.Filled.ArrowBack
                                ),
                                contentDescription = "Back"
                            )
                        }
                    )
                },
                title = {
                    Text(
                        "History",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        },
    ) { contentPadding ->

        LaunchedEffect(calendarList) {
            lazyColumnListState.scrollToItem(calendarList.size)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().haze(state = hazeState),
            state = lazyColumnListState,
            contentPadding = PaddingValues(
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                top = contentPadding.calculateTopPadding() + 16.dp,
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                bottom = contentPadding.calculateBottomPadding() + WindowInsets.systemBars.asPaddingValues()
                    .calculateBottomPadding()
            ),
        ) {

            lazyColumnItems(
                items = calendarList,
                key = { it.month.monthNumber }
            ) { emojiCalendarItem ->
                EmojiCalendarView(
                    monthEmojiData = emojiCalendarItem,
                    onClick = { selectedDate ->
                        showSheet = true
                        viewModel.getEmojiByTimeStampRange(selectedDate.day)
                    },
                )
            }
        }
    }
}

@Composable
private fun ChatList(
    chatMessages: List<ChatMessage>,
    listState: LazyListState,
) {
    val messages by remember {
        derivedStateOf { chatMessages.reversed() }
    }
    LazyColumn(
        state = listState,
        reverseLayout = true,
    ) {
        lazyColumnItems(
            items = messages,
            key = { it.id },
        ) { message ->
            ChatBubbleItem(message)
        }
        item {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
        }
    }

    LaunchedEffect(true) {
        listState.scrollToItem(0)
    }
}