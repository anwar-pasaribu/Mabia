package ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.extension.bouncingWithShadowClickable

@Composable
fun GlassyButton(
    modifier: Modifier = Modifier,
    buttonText: @Composable () -> Unit,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.then(Modifier
            .bouncingWithShadowClickable(enabled = enabled) { onClick() }
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(percent = 50))
            .padding(horizontal = 32.dp, vertical = 10.dp)
        )
    ) {
        buttonText()
    }
}