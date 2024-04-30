package ui.screen.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import data.EmojiList
import org.koin.compose.koinInject
import ui.component.GlassyButton
import ui.component.MoodRateView
import ui.viewmodel.MainViewModel

private val MoodRateDescriptions by lazy {
    listOf(
        "Menggambarkan suasana atau interaksi yang membuat seseorang merasa puas, tenang, dan mungkin bahkan terangkat semangatnya karena hal-hal positif yang terjadi.",
        "\"Pleasant\" sama dengan \"Menyenangkan\". Ini menggambarkan sesuatu yang disukai dan membuat Anda merasa enak. ",
        "Menggambarkan sesuatu yang agak menyenangkan tapi tidak terlalu istimewa.  Bisa dinikmati sedikit, namun tidak meninggalkan kesan kuat. ",
        "Kondisi emosional yang tidak condong ke senang maupun tidak senang.  Anda menerima situasi apa adanya, tanpa merasa antusias atau terganggu.  Mirip seperti air putih, perasaan ini datar dan tidak memiliki rasa khusus.",
        "Menggambarkan sesuatu yang tidak sepenuhnya buruk, namun juga tidak bisa dikatakan menyenangkan.  Ada kesan kurang nyaman atau tidak memuaskan, meski tidak sampai level mengganggu.",
        "Sesuatu yang tidak disukai atau membuat Anda merasa tidak nyaman.  Ini bisa menggambarkan pengalaman yang mengganggu, menyebalkan, atau tidak menyenangkan.",
        "Sesuatu yang amat tidak disukai dan membuat Anda merasa tidak nyaman.  Ini bisa menggambarkan pengalaman buruk, percakapan yang menegangkan, atau suasana yang tidak menyenangkan."
    )
}

@Composable
fun MoodRateOnboardingScreen(onFinish: () -> Unit = {}) {

    val viewModel = koinInject<MainViewModel>()

    MoodRateOnboarding {
        viewModel.setMoodStateOnboardingSeen()
        onFinish()
    }

}

@Composable
fun MoodRateOnboarding(onCtaAction: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                text = "Mood Level",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
            )

            MoodRatePagerDisplay()

            GlassyButton(
                modifier = Modifier
                    .padding(top = 32.dp),
                buttonText = {
                    Text(
                        modifier = Modifier.defaultMinSize(minWidth = 128.dp),
                        text = "Finish",
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                    )
                }
            ) {
                onCtaAction()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoodRatePagerDisplay(
    selectedMoodStateIndex: Int = 0
) {
    val moodRateList = (1..7).toList()
    val horizontalPagerState = rememberPagerState(
        initialPage = if (selectedMoodStateIndex < 0) 0 else selectedMoodStateIndex,
        pageCount = { moodRateList.size }
    )

    HorizontalPager(
        state = horizontalPagerState,
        modifier = Modifier.aspectRatio(3F / 4F),
        pageSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 32.dp)
    ) { pageIndex ->

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MoodRateView(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerHighest),
                moodRate = moodRateList[pageIndex],
                loadingState = false
            )

            val moodLabel = EmojiList.moodPleasantness[
                moodRateList[pageIndex]
            ].orEmpty()
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = moodLabel,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(72.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(1.dp)
                        )
                        .height(2.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = MoodRateDescriptions[pageIndex],
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}