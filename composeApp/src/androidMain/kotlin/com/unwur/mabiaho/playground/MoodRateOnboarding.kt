package com.unwur.mabiaho.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ui.screen.onboarding.MoodRateOnboarding
import ui.theme.MyAppTheme


@Preview
@Composable
private fun MoodOnboardingScreenPrev() {
    MyAppTheme {
        MoodRateOnboarding() {

        }
    }
}