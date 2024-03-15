import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import data.emojiList
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.component.MoodGridItem
import ui.theme.MyAppTheme

@OptIn(
    ExperimentalResourceApi::class, ExperimentalMaterial3Api::class,
    ExperimentalHazeMaterialsApi::class
)
@Composable
@Preview
fun App() {
    MyAppTheme {

        val hazeState = remember { HazeState() }

        Scaffold(
            topBar = {
                LargeTopAppBar(
                    modifier = Modifier.fillMaxWidth().hazeChild(
                        state = hazeState,
                        style = HazeMaterials.thin(MaterialTheme.colorScheme.background)
                    ),
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                    title = {
                        Text("Today", style = MaterialTheme.typography.headlineLarge)
                    }
                )
            },
        ) { contentPadding ->

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize().haze(state = hazeState),
                columns = GridCells.Adaptive(120.dp),
                contentPadding = PaddingValues(
                    start = contentPadding.calculateStartPadding(LayoutDirection.Ltr) + 8.dp,
                    top = contentPadding.calculateTopPadding() + 16.dp,
                    end = contentPadding.calculateEndPadding(LayoutDirection.Ltr) + 8.dp,
                    bottom = contentPadding.calculateBottomPadding()

                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(emojiList) {
                    Box(modifier = Modifier.fillMaxSize().aspectRatio(1f)) {
                        MoodGridItem {
                            Box(
                                modifier = Modifier.fillMaxSize().clip(
                                    CircleShape
                                ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = it,
                                    fontSize = TextUnit(64F, TextUnitType.Sp)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                }
            }

        }
    }
}