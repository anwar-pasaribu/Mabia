package ui.component.bubble


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WatchGridLayout(
    modifier: Modifier = Modifier,
    rowItemsCount: Int,
    itemSize: Dp,
    state: WatchGridState = rememberWatchGridState(),
    content: @Composable () -> Unit,
) {

    check(rowItemsCount > 0) { "rowItemsCount must be positive" }
    check(itemSize > 0.dp) { "itemSize must be positive" }

    val itemSizePx = with(LocalDensity.current) { itemSize.roundToPx() }
    val itemConstraints = Constraints.fixed(width = itemSizePx, height = itemSizePx)

    Layout(
        modifier = modifier
            .drag(state)
            .clipToBounds(),
        content = content
    ) { measurables, layoutConstraints ->

        val placeables = measurables.map { measurable -> measurable.measure(itemConstraints) }
        val cells = List(placeables.size) { index ->
            val x = index % rowItemsCount
            val y = (index - x) / rowItemsCount
            Cell(x, y)
        }

        state.config = WatchGridConfig(
            layoutWidthPx = layoutConstraints.maxWidth,
            layoutHeightPx = layoutConstraints.maxHeight,
            itemSizePx = itemSizePx,
            cells = cells
        )

        layout(layoutConstraints.maxWidth, layoutConstraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val position = state.getPositionFor(index)
                val scale = state.getScaleFor(position)
                placeable.placeWithLayer(
                    position = position,
                    layerBlock = {
                        this.scaleX = scale
                        this.scaleY = scale
                    }
                )
            }
        }
    }
}