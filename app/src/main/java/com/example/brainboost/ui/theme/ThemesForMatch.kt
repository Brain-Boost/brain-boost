package com.example.brainboost.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.brainboost.R

interface HolidayTheme {
    val backgroundPortrait: Int
    val backgroundLandscape: Int
    val cardback: Int
    val cardBaseColor: Color
    val textColor: Color
    val cardFrontBaseColor: Color
    val matchedOutlineColor: Color
    val iconColor: Color
    val imageMap: Map<Int, Int>

    fun getImageResourceForNumber(number: Int): Int?
    fun getNextTheme(): HolidayTheme
}

class BrainBoostTheme(
    override val backgroundPortrait: Int = R.drawable.background_portrait_blue,
    override val backgroundLandscape: Int = R.drawable.bg_ls_blue,
    override val cardback: Int = R.drawable.bb_back_of_card,
    override val cardBaseColor: Color = Colors.tan,
    override val textColor: Color = Colors.white,
    override val cardFrontBaseColor: Color = Colors.blueWhite,
    override val matchedOutlineColor: Color = Colors.darkGreen,
    override val iconColor: Color = Colors.blueGray,
    override val imageMap: Map<Int, Int> = mapOf(
        1 to R.drawable.brain_boost_for_card,
        2 to R.drawable.brain1,
        3 to R.drawable.brain2,
        4 to R.drawable.brain3,
        5 to R.drawable.brain4,
        6 to R.drawable.brain5,
        7 to R.drawable.brain6,
        8 to R.drawable.brain7,
        9 to R.drawable.bb9,
    )
): HolidayTheme {
    override fun getImageResourceForNumber(number: Int): Int? {
        return imageMap[number]
    }
    override fun getNextTheme(): HolidayTheme {
        return BrainBoostPinkTheme()
    }
}
class BrainBoostPinkTheme(
    override val backgroundPortrait: Int = R.drawable.background_portrait_pink,
    override val backgroundLandscape: Int = R.drawable.bg_ls_pink,
    override val cardback: Int = R.drawable.bb_back_of_card_blue,
    override val cardBaseColor: Color = Colors.tan,
    override val textColor: Color = Colors.white,
    override val cardFrontBaseColor: Color = Colors.pink33,
    override val matchedOutlineColor: Color = Colors.darkGreen,
    override val iconColor: Color = Colors.pink33,
    override val imageMap: Map<Int, Int> = mapOf(
        1 to R.drawable.brain_boost_for_card,
        2 to R.drawable.brain1,
        3 to R.drawable.brain2,
        4 to R.drawable.brain3,
        5 to R.drawable.brain4,
        6 to R.drawable.brain5,
        7 to R.drawable.brain6,
        8 to R.drawable.brain7,
        9 to R.drawable.bb9,
    )
) : HolidayTheme {
    override fun getImageResourceForNumber(number: Int): Int? {
        return imageMap[number]
    }

    override fun getNextTheme(): HolidayTheme {
        return BrainBoostTheme()
    }
}