package com.example.brainboost.memoryGame.memoryFeature.domain.model

class MemoryCard(
    var value: Int,
    var isBackDisplayed: Boolean = true,
    var matchFound: Boolean = false
) {
    fun flipCard(){
        isBackDisplayed = !isBackDisplayed
    }
}