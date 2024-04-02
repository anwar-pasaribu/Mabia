import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.letsKoinStart

fun main() = application {
    letsKoinStart()
    Window(onCloseRequest = ::exitApplication, title = "Mabia") {
        App()
    }
}