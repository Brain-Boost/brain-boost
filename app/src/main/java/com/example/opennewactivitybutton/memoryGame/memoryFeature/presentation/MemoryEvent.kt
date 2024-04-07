package com.example.opennewactivitybutton.memoryGame.memoryFeature.presentation

sealed class MemoryEvent {
    data class CardClick(val cardId: Int): MemoryEvent()
    object AddPair: MemoryEvent()
    object ReducePairs: MemoryEvent()
    object ChangeTheme: MemoryEvent()
    object ResetGame: MemoryEvent()
}