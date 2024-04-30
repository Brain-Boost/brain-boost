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
    override val backgroundLandscape: Int = R.drawable.background_landscape_thanksgiving,
    override val cardback: Int = R.drawable.bb_back_of_card,
    override val cardBaseColor: Color = Colors.tan,
    override val textColor: Color = Colors.white,
    override val cardFrontBaseColor: Color = Colors.blueWhite,
    override val matchedOutlineColor: Color = Colors.darkGreen,
    override val iconColor: Color = Colors.black,
    override val imageMap: Map<Int, Int> = mapOf(
        1 to R.drawable.brain_boost_for_card,
        2 to R.drawable.brain1,
        3 to R.drawable.brain2,
        4 to R.drawable.brain3,
        5 to R.drawable.brain4,
        6 to R.drawable.bb6,
        7 to R.drawable.bb7,
        8 to R.drawable.bb8,
        9 to R.drawable.bb9,
    )
): HolidayTheme {
    override fun getImageResourceForNumber(number: Int): Int? {
        return imageMap[number]
    }
    override fun getNextTheme(): HolidayTheme {
        return BrainBoostTheme()
    }
}
class HalloweenTheme(
    override val backgroundPortrait: Int = R.drawable.background_portrait_halloween,
    override val backgroundLandscape: Int = R.drawable.background_landscape_halloween,
    override val cardBaseColor: Color = Color.Black,
    override val cardback: Int = R.drawable.cardback_halloween,
    override val textColor: Color = Colors.blueWhite,
    override val cardFrontBaseColor: Color = Colors.blueWhite,
    override val matchedOutlineColor: Color = Colors.darkGreen,
    override val iconColor: Color = Colors.orange,
    override val imageMap: Map<Int, Int> = mapOf(
        1 to R.drawable.hw1,
        2 to R.drawable.hw2,
        3 to R.drawable.hw3,
        4 to R.drawable.hw4,
        5 to R.drawable.hw5,
        6 to R.drawable.hw6,
        7 to R.drawable.hw7,
        8 to R.drawable.hw8,
        9 to R.drawable.hw9
    )
) : HolidayTheme {
    override fun getImageResourceForNumber(number: Int): Int? {
        return imageMap[number]
    }

    override fun getNextTheme(): HolidayTheme {
        return HalloweenTheme()
    }
}