package ui.screen.emojis

import PlayHapticAndSound
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.flow.MutableStateFlow
import moe.tlaster.precompose.stateholder.LocalSavedStateHolder
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import ui.component.EmojiFlyingUpCanvas
import ui.component.GlassyButton
import ui.component.HomeCardDisplay
import ui.component.MoodGridItem
import ui.component.WeekView
import ui.screen.moodRate.MoodStateBottomSheet

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class
)
@Composable
fun EmojiListScreen(
    onScreenStateChanged: (Int) -> Unit = {},
    openHistoryScreen: () -> Unit = {},
) {

    val hazeState = remember { HazeState() }
    val selectedEmojiUnicodes = remember { mutableStateListOf("") }
    var selectedEmojiUnicode by remember { mutableStateOf("") }
    var selectedEmojiOffset by remember { mutableStateOf(Offset.Zero) }
    val lazyListState = rememberLazyGridState()

    val stateHolder = LocalSavedStateHolder.current
    val viewModel = koinInject<EmojiListScreenViewModel> {
        parametersOf(stateHolder)
    }
    val greetingText by remember { mutableStateOf(viewModel.getGreeting()) }
    val emojiListFlowState by viewModel.emojiListStateFlow.collectAsState()
    val showOnboardingBottomMenu by viewModel.showOnboardingBottomMenu.collectAsState()

    val selectedEmojiUnicodeAndOffset =
        remember { mutableStateOf(MutableStateFlow(Pair("", Offset.Zero))) }

    var selectedDateTimeStamp by remember { mutableStateOf(0L) }
    var moodStateBottomSheetStateShowed by remember { mutableStateOf(false) }

    if (selectedEmojiUnicode.isNotEmpty()) {
        PlayHapticAndSound(selectedEmojiUnicode)
    }

    LaunchedEffect(true) {
        viewModel.loadAllEmoji()
    }

    if (moodStateBottomSheetStateShowed) {
        MoodStateBottomSheet(
            onDismiss = {
                moodStateBottomSheetStateShowed = false
            },
            showFullScreen = true,
            selectedDateTimeStamp = selectedDateTimeStamp
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth().hazeChild(
                    state = hazeState,
                    style = HazeMaterials.regular(MaterialTheme.colorScheme.background)
                ).background(Color.Transparent)
            ) {
                CenterAlignedTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                    title = {
                        Text(
                            greetingText,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                )

                if (!showOnboardingBottomMenu) {
                    WeekView(modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        onMonthNameClick = {
                            openHistoryScreen()
                        },
                        onWeekDayClick = {
                            selectedDateTimeStamp = it
                            moodStateBottomSheetStateShowed = true
                        }
                    )
                }

                if (showOnboardingBottomMenu) {
                    HomeCardDisplay(
                        modifier = Modifier.padding(16.dp)
                            .shadow(
                                elevation = 16.dp,
                                clip = true,
                                shape = MaterialTheme.shapes.large
                            ),
                        extraMsg = selectedEmojiUnicodes.joinToString(" ").trim()
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
        },
    ) { contentPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize().haze(state = hazeState),
                state = lazyListState,
                columns = GridCells.Adaptive(100.dp),
                contentPadding = PaddingValues(
                    start = contentPadding.calculateStartPadding(LayoutDirection.Ltr) + 8.dp,
                    top = contentPadding.calculateTopPadding() + 16.dp,
                    end = contentPadding.calculateEndPadding(LayoutDirection.Ltr) + 8.dp,
                    bottom = contentPadding.calculateBottomPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = emojiListFlowState, key = { it.id }) { item ->

                    MoodGridItem(content = item.emojiUnicode.trim()) { selectedUnicode, offset ->
                        selectedEmojiUnicodes.add(selectedUnicode)
                        selectedEmojiUnicode = selectedUnicode
                        viewModel.saveSelectedEmojiUnicode(selectedUnicode)

                        selectedEmojiOffset = offset

                        selectedEmojiUnicodeAndOffset.value.value = Pair(selectedUnicode, offset)
                    }
                }

                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                }
            }

            EmojiFlyingUpCanvas(selectedEmojiUnicodeAndOffset.value)

            val showOnboardingFinishHelperClue = selectedEmojiUnicodes.size >= 4 && showOnboardingBottomMenu
            AnimatedVisibility(
                visible = showOnboardingFinishHelperClue,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                Box(modifier = Modifier.fillMaxSize().hazeChild(
                    state = hazeState,
                    style = HazeMaterials.thin(MaterialTheme.colorScheme.background)
                ).clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {}
                ))
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = showOnboardingFinishHelperClue,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it /* BOTTOM to UP*/ }),
                exit = fadeOut().plus(slideOutVertically(targetOffsetY = { it }))
            ) {
                val gradientColors = listOf(
                    Color(0x00000000),  // Transparent
                    MaterialTheme.colorScheme.secondaryContainer, // Soft black
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = gradientColors,
                                startY = 0.0F,
                            )
                        )
                ) {
                    GlassyButton(
                        modifier = Modifier.align(Alignment.TopCenter).padding(top = 32.dp),
                        buttonText = {
                            Text(text = "Lihat Mood Kamu")
                        }
                    ) {
                        moodStateBottomSheetStateShowed = true
                        viewModel.setOnboardingAlmostFinished()
                    }
                }
            }
        }
    }
}