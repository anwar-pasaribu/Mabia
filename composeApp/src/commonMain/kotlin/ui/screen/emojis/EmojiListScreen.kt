package ui.screen.emojis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import moe.tlaster.precompose.stateholder.LocalSavedStateHolder
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import ui.component.GlassyButton
import ui.component.HomeCardDisplay
import ui.component.MoodGridItem
import ui.extension.FadeAnimation
import ui.screen.emojis.model.EmojiUiModel
import ui.viewmodel.EmojiListScreenViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun EmojiListScreen(onScreenStateChanged: (Int) -> Unit = {}) {

    val hazeState = remember { HazeState() }
    val selectedEmojiUnicodes = remember { mutableStateListOf<String>() }
    val lazyListState = rememberLazyGridState()

    val stateHolder = LocalSavedStateHolder.current
    val viewModel = koinInject<EmojiListScreenViewModel> {
        parametersOf(stateHolder)
    }
    val emojiListFlowState by viewModel.emojiListStateFlow.collectAsState()

    Scaffold(
        topBar = {
            Column {
                LargeTopAppBar(
                    modifier = Modifier.fillMaxWidth().hazeChild(
                        state = hazeState,
                        style = HazeMaterials.thin(MaterialTheme.colorScheme.background)
                    ),
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                    title = {
                        Text("halo,", style = MaterialTheme.typography.headlineLarge)
                    }
                )
                FadeAnimation(true) {
                    HomeCardDisplay(
                        modifier = Modifier.padding(16.dp)
                            .shadow(
                                elevation = 16.dp,
                                clip = true,
                                shape = MaterialTheme.shapes.large
                            ),
                    )
                }
            }
        },
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().animateContentSize()) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize().haze(state = hazeState),
                state = lazyListState,
                columns = GridCells.Fixed(3),
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
                        content = item.emojiUnicode,
                        selected = item.selected
                    ) { selectedUnicode ->
                        emojiListFlowState[item.id].selected = !emojiListFlowState[item.id].selected
                        if (item.selected) {
                            selectedEmojiUnicodes.add(selectedUnicode.trim())
                        } else {
                            selectedEmojiUnicodes.remove(selectedUnicode.trim())
                        }
                    }
                }

//                this.emojiGridContent(emojiListFlowState, 3, lazyListState) {
//                        selectedUnicode ->
//                    emojiListFlowState[item.id].selected = !emojiListFlowState[item.id].selected
//                    if (item.selected) {
//                        selectedEmojiUnicodes.add(selectedUnicode.trim())
//                    } else {
//                        selectedEmojiUnicodes.remove(selectedUnicode.trim())
//                    }
//                }

                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = selectedEmojiUnicodes.size >= 1,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it /* BOTTOM to UP*/}),
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
                        onScreenStateChanged(3)
                    }
                }
            }
        }
    }
}

fun LazyGridScope.emojiGridContent(
    emojiList: List<EmojiUiModel>,
    columns: Int,
    state: LazyGridState,
    onItemSelected: (String) -> Unit
) {
    items(emojiList.count()) { index ->
        val (delay, easing) = state.calculateDelayAndEasing(index, columns)
        val animation = tween<Float>(durationMillis = 300, delayMillis = delay, easing = easing)
        val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
        val (scale, alpha) = scaleAndAlpha(args = args, animation = animation)
        val emoji = emojiList[index]
        MoodGridItem(
            modifier = Modifier.graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale),
            content = emoji.emojiUnicode,
        ) {
            onItemSelected(it)
        }
    }
}

@Composable
fun LazyGridState.calculateDelayAndEasing(index: Int, columnCount: Int): Pair<Int, Easing> {
    val row = index / columnCount
    val column = index % columnCount
    val firstVisibleRow = firstVisibleItemIndex
    val visibleRows = layoutInfo.visibleItemsInfo.count() - 3
    val scrollingToBottom = firstVisibleRow < row
    val isFirstLoad = visibleRows == 0
    val rowDelay = 200 * when {
        isFirstLoad -> row // initial load
        scrollingToBottom -> visibleRows + firstVisibleRow - row // scrolling to bottom
        else -> 1 // scrolling to top
    }
    val scrollDirectionMultiplier = if (scrollingToBottom || isFirstLoad) 1 else -1
    val columnDelay = column * 150 * scrollDirectionMultiplier
    val easing = if (scrollingToBottom || isFirstLoad) LinearOutSlowInEasing else FastOutSlowInEasing
    return rowDelay + columnDelay to easing
}

enum class State { PLACING, PLACED }

data class ScaleAndAlphaArgs(
    val fromScale: Float,
    val toScale: Float,
    val fromAlpha: Float,
    val toAlpha: Float
)

@Composable
fun scaleAndAlpha(
    args: ScaleAndAlphaArgs,
    animation: FiniteAnimationSpec<Float>
): Pair<Float, Float> {
    val transitionState = remember { MutableTransitionState(State.PLACING).apply { targetState = State.PLACED } }
    val transition = updateTransition(transitionState)
    val alpha by transition.animateFloat(transitionSpec = { animation }) { state ->
        when (state) {
            State.PLACING -> args.fromAlpha
            State.PLACED -> args.toAlpha
        }
    }
    val scale by transition.animateFloat(transitionSpec = { animation }) { state ->
        when (state) {
            State.PLACING -> args.fromScale
            State.PLACED -> args.toScale
        }
    }
    return alpha to scale
}