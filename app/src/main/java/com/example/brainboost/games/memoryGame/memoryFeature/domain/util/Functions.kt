package com.example.brainboost.memoryGame.memoryFeature.domain.util

import com.example.brainboost.memoryGame.memoryFeature.domain.model.MemoryCard

fun generateCardsArray(matches: Int): Array<MemoryCard> {
    val singles = 1.. matches
    val doubles = singles + singles
    return doubles.shuffled().map { MemoryCard(it) }.toTypedArray()
}