package com.example.opennewactivitybutton.memoryGame.memoryFeature.presentation.memoryScreen

import com.example.opennewactivitybutton.memoryGame.memoryFeature.domain.model.MemoryCard
import com.example.opennewactivitybutton.memoryGame.memoryFeature.domain.util.generateCardsArray
import com.example.opennewactivitybutton.memoryGame.memoryFeature.presentation.util.NumericalValues.starting_pairs
import com.example.opennewactivitybutton.ui.theme.HolidayTheme
import com.example.opennewactivitybutton.ui.theme.ThanksgivingTheme

data class MemoryState(
    val cards: Array<MemoryCard> = generateCardsArray(starting_pairs),
    val card1: Int? = null,
    val card2: Int? = null,
    val pairCount: Int = starting_pairs,
    val pairsMatched: Int = 0,
    val clickCount: Int = 0,
    val currentTheme: HolidayTheme = ThanksgivingTheme()
) {
}