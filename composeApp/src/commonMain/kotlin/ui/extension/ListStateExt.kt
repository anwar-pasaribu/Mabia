package ui.extension

import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow

@Composable
fun LazyListState.onDragging() {
    val dragState = this.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(dragState) {
        snapshotFlow { dragState.value }
            .collect {
                if (it) {
                    println("Dragging...")
                } else {
                    println("Dragging... not")
                }
            }
    }
}