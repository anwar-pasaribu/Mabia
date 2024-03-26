package ui.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlinx.datetime.until
import org.koin.compose.koinInject
import ui.component.CalendarView
import ui.component.MoodGridItem
import ui.screen.emojis.model.EmojiUiModel
import ui.viewmodel.HistoryScreenViewModel
import androidx.compose.foundation.lazy.items as lazyColumnItems

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun HistoryScreen(onBack: () -> Unit = {}) {

    val viewModel = koinInject<HistoryScreenViewModel>()
    val emojiHistoryList by viewModel.emojiListStateFlow.collectAsState()

    val hazeState = remember { HazeState() }
    var formattedSelectedDate by remember { mutableStateOf("") }
    val lazyListState = rememberLazyGridState()
    val lazyColumnListState = rememberLazyListState()

    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        EmojiBottomSheet(
            onDismiss = {
                showSheet = false
            },
            lazyListState = lazyListState,
            formattedSelectedDate = formattedSelectedDate,
            emojiHistoryList = emojiHistoryList.toImmutableList()
        )
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    modifier = Modifier.fillMaxWidth().hazeChild(
                        state = hazeState,
                        style = HazeMaterials.thin(MaterialTheme.colorScheme.background)
                    ),
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                    navigationIcon = {
                        IconButton(
                            onClick = { onBack() },
                            modifier = Modifier.size(32.dp),
                            content = {
                                Icon(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = rememberVectorPainter(
                                        image = Icons.AutoMirrored.Filled.KeyboardArrowLeft
                                    ),
                                    contentDescription = null
                                )
                            }
                        )
                    },
                    title = {
                        Text(
                            "History",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                )
            }
        },
    ) { contentPadding ->

        // TODO Date logic should start date from user first join
        val today: LocalDate = Clock.System.todayIn(
            TimeZone.currentSystemDefault()
        )
        val todayMonthNumber = today.monthNumber
        val localDateListThisYear = mutableListOf<LocalDate>()
        (1 until (todayMonthNumber)).forEach {
            val start = LocalDate(year = today.year, monthNumber = it, dayOfMonth = 1)
            val end = start.plus(1, DateTimeUnit.MONTH)
            val totalDayCountInTheMonth = start.until(end, DateTimeUnit.DAY)
            localDateListThisYear.add(
                LocalDate(year = today.year, monthNumber = it, dayOfMonth = totalDayCountInTheMonth)
            )
        }
        localDateListThisYear.add(today)

        LaunchedEffect(true) {
            lazyColumnListState.scrollToItem(localDateListThisYear.size)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().haze(state = hazeState),
            state = lazyColumnListState,
            contentPadding = PaddingValues(
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                top = contentPadding.calculateTopPadding() + 16.dp,
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                bottom = contentPadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            lazyColumnItems(items = localDateListThisYear) { dateItem ->
                CalendarView(
                    month = dateItem,
                    date = listOf(
                        dateItem to (dateItem.dayOfMonth == today.dayOfMonth),
                    ).toImmutableList(),
                    onClick = { selectedDate ->
                        showSheet = true
                        val tz = TimeZone.currentSystemDefault()
                        val nowTimeStamp = Clock.System.now().toLocalDateTime(tz).time
                        val dateTime = LocalDateTime(date = selectedDate, time = nowTimeStamp)
                        viewModel.getEmojiByTimeStampRange(
                            dateTime.toInstant(tz).toEpochMilliseconds()
                        )

                        val shortDayName = dateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercaseChar() }
                        val monthName = dateTime.month.name.lowercase().replaceFirstChar { it.uppercaseChar() }
                        formattedSelectedDate = shortDayName + ", " +
                            dateTime.dayOfMonth.toString() + " " + monthName + " " +
                                dateTime.year
                    },
                )
            }

            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiBottomSheet(
    onDismiss: () -> Unit,
    lazyListState: LazyGridState,
    formattedSelectedDate: String,
    emojiHistoryList: ImmutableList<EmojiUiModel>
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val noInset = BottomSheetDefaults.windowInsets
        .only(WindowInsetsSides.Bottom)
        .exclude(WindowInsets.navigationBars)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        windowInsets = noInset,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = formattedSelectedDate,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                        content = item.emojiUnicode.trim(),
                    ) { selectedUnicode ->

                    }
                }

                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                }
            }
        }
    }
}