
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ui.component.ShareableView
import ui.theme.MyAppTheme

@Preview
@Composable
private fun ShareablePrev() {

    MyAppTheme {
        ShareableView(
            moodRate = 1,
            formattedSelectedDate = "20 Mei 1993",
            emojiList = listOf("😀", "😃", "😄", "😁", "😆", "😅", "😂", "😊", "😎", "🤩")
        )
    }
}