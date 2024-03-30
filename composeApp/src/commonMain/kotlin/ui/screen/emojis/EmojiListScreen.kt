package ui.screen.emojis

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import getScreenSizeInfo
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import moe.tlaster.precompose.stateholder.LocalSavedStateHolder
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import ui.component.GlassyButton
import ui.component.HomeCardDisplay
import ui.component.MoodGridItem
import ui.component.WeekView
import ui.extension.bouncingClickable
import ui.extension.delayedAlpha
import ui.viewmodel.EmojiListScreenViewModel

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun EmojiListScreen(
    onScreenStateChanged: (Int) -> Unit = {},
    openHistoryScreen: () -> Unit = {},
) {

    val hazeState = remember { HazeState() }
    val selectedEmojiUnicodes = remember { mutableStateListOf<String>("") }
    var selectedEmojiUnicode by remember { mutableStateOf("") }
    val lazyListState = rememberLazyGridState()

    val stateHolder = LocalSavedStateHolder.current
    val viewModel = koinInject<EmojiListScreenViewModel> {
        parametersOf(stateHolder)
    }
    val emojiListFlowState by viewModel.emojiListStateFlow.collectAsState()
    val showOnboardingBottomMenu by viewModel.showOnboardingBottomMenu.collectAsState()

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
                            viewModel.greetingText.value,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                )

                if (!showOnboardingBottomMenu) {
                    WeekView(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
                        openHistoryScreen()
                    }
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

        var mainEmojiListVisibility by remember { mutableStateOf(false) }
        LaunchedEffect(mainEmojiListVisibility) {
            mainEmojiListVisibility = true
        }

        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = mainEmojiListVisibility,
                enter = fadeIn(animationSpec = tween(1000))
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize().haze(state = hazeState),
                    state = lazyListState,
                    columns = GridCells.Adaptive(128.dp),
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
                        MoodGridItem(
                            content = item.emojiUnicode.trim(),
                        ) { selectedUnicode ->
                            selectedEmojiUnicodes.add(selectedUnicode)
                            selectedEmojiUnicode = selectedUnicode
                            viewModel.saveSelectedEmojiUnicode(selectedUnicode)
                        }
                    }

                    item {
                        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                    }
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = selectedEmojiUnicodes.size >= 4 && showOnboardingBottomMenu,
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
                            Text(text = "Selesai")
                        }
                    ) {
                        viewModel.setOnboardingAlmostFinished()
                        onScreenStateChanged(3)
                    }
                }
            }
        }
    }
}