package data

import ui.screen.emojis.model.EmojiUiModel

object EmojiList {

    private val emojiList by lazy {
        listOf(
            "\uD83D\uDE00", // 😀 Grinning Face
            "\uD83D\uDE01", // 😁 Beaming Face with Smiling Eyes
            "\uD83D\uDE02", // 😂 Face with Tears of Joy
            "\uD83D\uDE03", // 😃 Grinning Face with Big Eyes
            "\uD83D\uDE04", // 😄 Grinning Face with Smiling Eyes
            "\uD83D\uDE05", // 😅 Grinning Face with Sweat
            "\uD83D\uDE06", // 😆 Grinning Squinting Face
            "\uD83D\uDE07", // 😇 Smiling Face with Halo
            "\uD83D\uDE08", // 😈 Smiling Face with Horns
            "\uD83D\uDE09", // 😉 Winking Face
            "\uD83D\uDE0A", // 😊 Smiling Face with Smiling Eyes
            "\uD83D\uDE0B", // 😋 Face Savoring Food
            "\uD83D\uDE0C", // 😌 Relieved Face
            "\uD83D\uDE0D", // 😍 Smiling Face with Heart-Eyes
            "\uD83D\uDE0E", // 😎 Smiling Face with Sunglasses
            "\uD83D\uDE0F", // 😏 Smirking Face
            "\uD83D\uDE10", // 😐 Neutral Face
            "\uD83D\uDE11", // 😑 Expressionless Face
            "\uD83D\uDE12", // 😒 Unamused Face
            "\uD83D\uDE13", // 😓 Downcast Face with Sweat
            "\uD83D\uDE14", // 😔 Pensive Face
            "\uD83D\uDE15", // 😕 Confused Face
            "\uD83D\uDE16", // 😖 Confounded Face
            "\uD83D\uDE17", // 😗 Kissing Face
            "\uD83D\uDE18", // 😘 Face Blowing a Kiss
            "\uD83D\uDE19", // 😙 Kissing Face with Smiling Eyes
            "\uD83D\uDE1A", // 😚 Kissing Face with Closed Eyes
            "\uD83D\uDE1B", // 😛 Face with Tongue
            "\uD83D\uDE1C", // 😜 Winking Face with Tongue
            "\uD83D\uDE1D", // 😝 Squinting Face with Tongue
            "\uD83D\uDE1E", // 😞 Disappointed Face
            "\uD83D\uDE1F", // 😟 Worried Face
            "\uD83D\uDE20", // 😠 Angry Face
            "\uD83D\uDE21", // 😡 Pouting Face
            "\uD83D\uDE22", // 😢 Crying Face
            "\uD83D\uDE23", // 😣 Persevering Face
            "\uD83D\uDE24", // 😤 Face with Steam From Nose
            "\uD83D\uDE25", // 😥 Sad but Relieved Face
            "\uD83D\uDE26", // 😦 Frowning Face with Open Mouth
            "\uD83D\uDE27", // 😧 Anguished Face
            "\uD83D\uDE28", // 😨 Fearful Face
            "\uD83D\uDE29", // 😩 Weary Face
            "\uD83D\uDE2A", // 😪 Sleepy Face
            "\uD83D\uDE2B", // 😫 Tired Face
            "\uD83D\uDE2C", // 😬 Grimacing Face
            "\uD83D\uDE2D", // 😭 Loudly Crying Face
            "\uD83D\uDE2E", // 😮 Face with Open Mouth
            "\uD83D\uDE2F", // 😯 Hushed Face
            "\uD83D\uDE30", // 😰 Face with Open Mouth & Cold Sweat
            "\uD83D\uDE31", // 😱 Face Screaming in Fear
            "\uD83D\uDE32", // 😲 Astonished Face
            "\uD83D\uDE33", // 😳 Flushed Face
            "\uD83D\uDE34", // 😴 Sleeping Face
            "\uD83D\uDE35", // 😵 Dizzy Face
            "\uD83D\uDE36", // 😶 Face Without Mouth
            "\uD83D\uDE37", // 😷 Face with Medical Mask
            "\uD83D\uDE2E\u200D\uD83D\uDCA8", // 😮‍💨face exhaling
            "\uD83D\uDE38", // 😸 Grinning Cat Face
            "\uD83D\uDE39", // 😹 Cat with Tears of Joy
            "\uD83D\uDE3A", // 😺 Smiling Cat Face
            "\uD83D\uDE3B", // 😻 Heart-Eyes Cat
            "\uD83D\uDE3C", // 😼 Cat with Wry Smile
            "\uD83D\uDE3D", // 😽 Kissing Cat
            "\uD83D\uDE3E", // 😾 Pouting Cat
            "\uD83D\uDE3F", // 😿 Crying Cat
            "\uD83D\uDE40", // 🙀 Weary Cat
            "\uD83D\uDE41", // 🙁 Slightly Frowning Face
            "\uD83D\uDE42", // 🙂 Slightly Smiling Face
            "\uD83D\uDE43", // 🙃 Upside-Down Face
            "\uD83D\uDE44", // 🙄 Face with Rolling Eyes
            "\uD83D\uDE45", // 🙅 Person Gesturing No
            "\uD83D\uDE46", // 🙆 Person Gesturing OK
            "\uD83D\uDE47", // 🙇 Person Bowing
            "\uD83D\uDE48", // 🙈 See-No-Evil Monkey
            "\uD83D\uDE49", // 🙉 Hear-No-Evil Monkey
            "\uD83D\uDE4A", // 🙊 Speak-No-Evil Monkey
            "\uD83D\uDE4B", // 🙋 Happy Person Raising One Hand
            "\uD83D\uDE4C", // 🙌 Person Raising Both Hands in Celebration
            "\uD83D\uDE4D", // 🙍 Person Frowning
            "\uD83D\uDE4E", // 🙎 Person Pouting
            "\uD83D\uDE4F", // 🙏 Person with Folded Hands
            "\uD83E\uDD10", // 🤐 Zipper-Mouth Face
            "\uD83E\uDD11", // 🤑 Money-Mouth Face
            "\uD83E\uDD13", // 🤓 Nerd Face
            "\uD83E\uDD14", // 🤔 Thinking Face
            "\uD83E\uDD15", // 🤕 Face with Head-Bandage
            "\uD83E\uDD17", // 🤗 Hugging Face
            "\uD83E\uDD18", // 🤘 Sign of the Horns
            "\uD83E\uDD75", // 🥵 Hot Face
            "\uD83E\uDD76", // 🥶 Cold Face
            "\uD83E\uDD8A", // 🦊 Fox Face
            "\uD83E\uDDCD", // 🧍 Person Standing
            "\uD83E\uDDCE", // 🧎 Person Kneeling
            "\uD83E\uDDCF", // 🧏 Person Standing: Medium-Light Skin Tone
            "\uD83E\uDDD0", // 🧐 Face with Monocle
            "\uD83E\uDDD1", // 🧑‍🦱 Person: Curly Hair
            "\uD83E\uDDD2", // 🧑‍🦰 Person: Red Hair
            "\uD83E\uDDD3", // 🧑‍🦳 Person: White Hair
            "\uD83E\uDDD4", // 🧑‍🦲 Person: Bald
            "\uD83E\uDDD5", // 🧑‍🦱‍👓 Person with Curly Hair: Glasses
            "\uD83E\uDDD6", // 🧑‍🦰‍👓 Person with Red Hair: Glasses
            "\uD83E\uDDD7", // 🧑‍🦳‍👓 Person with White Hair: Glasses
            "\uD83E\uDDD8", // 🧑‍🦲‍👓 Person with Bald: Glasses
            "\uD83E\uDDD9", // 🧑‍🦱‍🔬 Person with Curly Hair: Lab Coat
            "\uD83E\uDDDA", // 🧑‍🦰‍🔬 Person with Red Hair: Lab Coat
            "\uD83E\uDDDB", // 🧑‍🦳‍🔬 Person with White Hair: Lab Coat
            "\uD83E\uDDDC", // 🧑‍🦲‍🔬 Person with Bald: Lab Coat
            "\uD83E\uDDDD", // 🧑‍🦱‍💼 Person with Curly Hair: Business Suit
            "\uD83E\uDDDE", // 🧑‍🦰‍💼 Person with Red Hair: Business Suit
        )
    }

    val moodPleasantness = mapOf(
        -1 to "No Data",
        1 to "Very Pleasant", // Very Pleasant
        2 to "Pleasant", //Pleasant
        3 to "Slightly Pleasant", // Slightly Pleasant
        4 to "Neutral", // Neutral
        5 to "Slightly Unpleasant", // Slightly Unpleasant
        6 to "Unpleasant", // Unpleasant
        7 to "Very Unpleasant", // Very Unpleasant
    )

    val MOOD_UNKNOWN = -1
    val MOOD_1 = 1
    val MOOD_2 = 2
    val MOOD_3 = 3
    val MOOD_4 = 4
    val MOOD_5 = 5
    val MOOD_6 = 6
    val MOOD_7 = 7

    val emojiWeights = mapOf(
        "\uD83D\uDE00" to 1, // 😀 Grinning Face
        "\uD83D\uDE01" to 1, // 😁 Beaming Face with Smiling Eyes
        "\uD83D\uDE02" to 1, // 😂 Face with Tears of Joy
        "\uD83D\uDE03" to 1, // 😃 Grinning Face with Big Eyes
        "\uD83D\uDE04" to 1, // 😄 Grinning Face with Smiling Eyes
        "\uD83D\uDE05" to 2, // 😅 Grinning Face with Sweat
        "\uD83D\uDE06" to 2, // 😆 Grinning Squinting Face
        "\uD83D\uDE07" to 1, // 😇 Smiling Face with Halo
        "\uD83D\uDE08" to 6, // 😈 Smiling Face with Horns
        "\uD83D\uDE09" to 2, // 😉 Winking Face
        "\uD83D\uDE0A" to 1, // 😊 Smiling Face with Smiling Eyes
        "\uD83D\uDE0B" to 1, // 😋 Face Savoring Food
        "\uD83D\uDE0C" to 1, // 😌 Relieved Face
        "\uD83D\uDE0D" to 1, // 😍 Smiling Face with Heart-Eyes
        "\uD83D\uDE0E" to 2, // 😎 Smiling Face with Sunglasses
        "\uD83D\uDE0F" to 2, // 😏 Smirking Face
        "\uD83D\uDE10" to 4, // 😐 Neutral Face
        "\uD83D\uDE11" to 4, // 😑 Expressionless Face
        "\uD83D\uDE12" to 5, // 😒 Unamused Face
        "\uD83D\uDE13" to 5, // 😓 Downcast Face with Sweat
        "\uD83D\uDE14" to 5, // 😔 Pensive Face
        "\uD83D\uDE15" to 5, // 😕 Confused Face
        "\uD83D\uDE16" to 6, // 😖 Confounded Face
        "\uD83D\uDE17" to 1, // 😗 Kissing Face
        "\uD83D\uDE18" to 1, // 😘 Face Blowing a Kiss
        "\uD83D\uDE19" to 1, // 😙 Kissing Face with Smiling Eyes
        "\uD83D\uDE1A" to 1, // 😚 Kissing Face with Closed Eyes
        "\uD83D\uDE1B" to 1, // 😛 Face with Tongue
        "\uD83D\uDE1C" to 1, // 😜 Winking Face with Tongue
        "\uD83D\uDE1D" to 1, // 😝 Squinting Face with Tongue
        "\uD83D\uDE1E" to 5, // 😞 Disappointed Face
        "\uD83D\uDE1F" to 5, // 😟 Worried Face
        "\uD83D\uDE20" to 6, // 😠 Angry Face
        "\uD83D\uDE21" to 6, // 😡 Pouting Face
        "\uD83D\uDE22" to 7, // 😢 Crying Face
        "\uD83D\uDE23" to 6, // 😣 Persevering Face
        "\uD83D\uDE24" to 6, // 😤 Face with Steam From Nose
        "\uD83D\uDE25" to 5, // 😥 Sad but Relieved Face
        "\uD83D\uDE26" to 5, // 😦 Frowning Face with Open Mouth
        "\uD83D\uDE27" to 6, // 😧 Anguished Face
        "\uD83D\uDE28" to 6, // 😨 Fearful Face
        "\uD83D\uDE29" to 6, // 😩 Weary Face
        "\uD83D\uDE2A" to 6, // 😪 Sleepy Face
        "\uD83D\uDE2B" to 6, // 😫 Tired Face
        "\uD83D\uDE2C" to 6, // 😬 Grimacing Face
        "\uD83D\uDE2D" to 7, // 😭 Loudly Crying Face
        "\uD83D\uDE2E" to 4, // 😮 Face with Open Mouth
        "\uD83D\uDE2F" to 4, // 😯 Hushed Face
        "\uD83D\uDE30" to 5, // 😰 Face with Open Mouth & Cold Sweat
        "\uD83D\uDE31" to 7, // 😱 Face Screaming in Fear
        "\uD83D\uDE32" to 6, // 😲 Astonished Face
        "\uD83D\uDE33" to 5, // 😳 Flushed Face
        "\uD83D\uDE34" to 5, // 😴 Sleeping Face
        "\uD83D\uDE35" to 5, // 😵 Dizzy Face
        "\uD83D\uDE36" to 4, // 😶 Face Without Mouth
        "\uD83D\uDE37" to 6, // 😷 Face with Medical Mask
        "\uD83D\uDE38" to 1, // 😸 Grinning Cat Face
        "\uD83D\uDE39" to 1, // 😹 Cat with Tears of Joy
        "\uD83D\uDE3A" to 1, // 😺 Smiling Cat Face
        "\uD83D\uDE3B" to 1, // 😻 Heart-Eyes Cat
        "\uD83D\uDE3C" to 1, // 😼 Cat with Wry Smile
        "\uD83D\uDE3D" to 1, // 😽 Kissing Cat
        "\uD83D\uDE3E" to 5, // 😾 Pouting Cat
        "\uD83D\uDE3F" to 7, // 😿 Crying Cat
        "\uD83D\uDE40" to 6, // 🙀 Weary Cat
        "\uD83D\uDE41" to 5, // 🙁 Slightly Frowning Face
        "\uD83D\uDE42" to 1, // 🙂 Slightly Smiling Face
        "\uD83D\uDE43" to 4, // 🙃 Upside-Down Face
        "\uD83D\uDE44" to 5, // 🙄 Face with Rolling Eyes
        "\uD83D\uDE45" to 5, // 🙅 Person Gesturing No
        "\uD83D\uDE46" to 5, // 🙆 Person Gesturing OK
        "\uD83D\uDE47" to 1, // 🙇 Person Bowing
        "\uD83D\uDE48" to 2, // 🙈 See-No-Evil Monkey
        "\uD83D\uDE49" to 2, // 🙉 Hear-No-Evil Monkey
        "\uD83D\uDE4A" to 2, // 🙊 Speak-No-Evil Monkey
        "\uD83D\uDE4B" to 2, // 🙋 Happy Person Raising One Hand
        "\uD83D\uDE4C" to 2, // 🙌 Person Raising Both Hands in Celebration
        "\uD83D\uDE4D" to 5, // 🙍 Person Frowning
        "\uD83D\uDE4E" to 5, // 🙎 Person Pouting
        "\uD83D\uDE4F" to 1, // 🙏 Person with Folded Hands
        "\uD83E\uDD10" to 4, // 🤐 Zipper-Mouth Face
        "\uD83E\uDD11" to 5, // 🤑 Money-Mouth Face
        "\uD83E\uDD13" to 3, // 🤓 Nerd Face
        "\uD83E\uDD14" to 3, // 🤔 Thinking Face
        "\uD83E\uDD15" to 5, // 🤕 Face with Head-Bandage
        "\uD83E\uDD17" to 1, // 🤗 Hugging Face
        "\uD83E\uDD18" to 1, // 🤘 Sign of the Horns
        "\uD83E\uDD75" to 6, // 🥵 Hot Face
        "\uD83E\uDD76" to 6, // 🥶 Cold Face
        "\uD83E\uDD8A" to 4, // 🦊 Fox Face
        "\uD83E\uDDCD" to 2, // 🧍 Person Standing
        "\uD83E\uDDCE" to 2, // 🧎 Person Kneeling
        "\uD83E\uDDCF" to 2, // 🧏 Person Standing: Medium-Light Skin Tone
        "\uD83E\uDDD0" to 3, // 🧐 Face with Monocle
        "\uD83E\uDDD1" to 4, // 🧑‍🦱 Person: Curly Hair
        "\uD83E\uDDD2" to 4, // 🧑‍🦰 Person: Red Hair
        "\uD83E\uDDD3" to 4, // 🧑‍🦳 Person: White Hair
        "\uD83E\uDDD4" to 4, // 🧑‍🦲 Person: Bald
        "\uD83E\uDDD5" to 4, // 🧑‍🦱‍👓 Person with Curly Hair: Glasses
        "\uD83E\uDDD6" to 4, // 🧑‍🦰‍👓 Person with Red Hair: Glasses
        "\uD83E\uDDD7" to 4, // 🧑‍🦳‍👓 Person with White Hair: Glasses
        "\uD83E\uDDD8" to 4, // 🧑‍🦲‍👓 Person with Bald: Glasses
        "\uD83E\uDDD9" to 4, // 🧑‍🦱‍🔬 Person with Curly Hair: Lab Coat
        "\uD83E\uDDDA" to 4, // 🧑‍🦰‍🔬 Person with Red Hair: Lab Coat
        "\uD83E\uDDDB" to 4, // 🧑‍🦳‍🔬 Person with White Hair: Lab Coat
        "\uD83E\uDDDC" to 4, // 🧑‍🦲‍🔬 Person with Bald: Lab Coat
        "\uD83E\uDDDD" to 4, // 🧑‍🦱‍💼 Person with Curly Hair: Business Suit
        "\uD83E\uDDDE" to 4, // 🧑‍🦰‍💼 Person with Red Hair: Business Suit
    )

    val emojiList2 by lazy {
        listOf(
            "\uD83E\uDDE1", // 🧑‍🦱‍🎤 Person with Curly Hair: Tuxedo
            "\uD83E\uDDE2", // 🧑‍🦰‍🎤 Person with Red Hair: Tuxedo
            "\uD83E\uDDE3", // 🧑‍🦳‍🎤 Person with White Hair: Tuxedo
            "\uD83E\uDDE4", // 🧑‍🦲‍🎤 Person with Bald: Tuxedo
            "\uD83E\uDDE5", // 🧑‍🦱‍🎨 Person with Curly Hair: Artist
            "\uD83E\uDDE6", // 🧑‍🦰‍🎨 Person with Red Hair: Artist
            "\uD83E\uDDE7", // 🧑‍🦳‍🎨 Person with White Hair: Artist
            "\uD83E\uDDE8", // 🧑‍🦲‍🎨 Person with Bald: Artist
        )
    }

    fun generateEmojiForUI(): List<EmojiUiModel> = emojiList.mapIndexed { index, emojiItem ->
        EmojiUiModel(id = index, emojiUnicode = emojiItem)
    }
}