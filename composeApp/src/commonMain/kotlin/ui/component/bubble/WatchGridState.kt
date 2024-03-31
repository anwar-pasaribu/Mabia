package ui.component.bubble


import androidx.compose.ui.unit.IntOffset

interface WatchGridState : OffsetState {

    var config: WatchGridConfig

    fun getPositionFor(index: Int): IntOffset
    fun getScaleFor(position: IntOffset): Float
}