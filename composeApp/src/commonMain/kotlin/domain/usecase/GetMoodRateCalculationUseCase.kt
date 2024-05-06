package domain.usecase

import data.EmojiList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class GetMoodRateCalculationUseCase {
    suspend operator fun invoke(emojiList: List<String>): Int {

        val emojiFrequencyCounter: Map<String, Int> = emojiList
            .groupingBy { it }
            .eachCount()

        val emojiFrequencyPercentage = generateEmojiFrequencyPercentage(emojiFrequencyCounter)
        val moodRate = calculateMoodRating(emojiFrequencyPercentage)

        return moodRate
    }

    private fun generateEmojiFrequencyPercentage(emojiUnicodeList: Map<String, Int>): Map<String, Double> {
        val percentages = mutableMapOf<String, Double>()
        if (emojiUnicodeList.isEmpty()) {
            return percentages
        }

        val totalEmojis = emojiUnicodeList.values.sum()


        for ((emoji, count) in emojiUnicodeList) {
            val percentage = (count.toDouble() / totalEmojis) * 100
            percentages[emoji] = percentage
        }

        return percentages

    }

    /**
     * In Russell's circumplex model, affective states are represented in a two-dimensional space: valence (pleasure) and arousal (activation). Valence ranges from unpleasant to pleasant, while arousal ranges from low to high activation. We can use this model to assign mood ratings based on the valence of the emojis.
     *
     * Here's an approach to calculating the mood rating using Russell's circumplex model:
     */
    private suspend fun calculateMoodRating(emojiMap: Map<String, Double>): Int {

        return withContext(Dispatchers.Default) {
            var totalWeightedFrequency = 0.0
            var totalFrequency = 0.0

            emojiMap.forEach { (emoji, frequency) ->
                val weight = EmojiList.emojiWeights[emoji]
                if (weight != null) {
                    totalWeightedFrequency += frequency * weight
                    totalFrequency += frequency
                }
            }


            // Round the average pleasantness level to the nearest integer
            return@withContext try {
                // Calculate the weighted average pleasantness level
                val averagePleasantness = totalWeightedFrequency / totalFrequency
                averagePleasantness.roundToInt()
            } catch (e: IllegalArgumentException) {
                EmojiList.MOOD_UNKNOWN
            } catch (die: ArithmeticException) {
                EmojiList.MOOD_UNKNOWN
            }
        }
    }
}