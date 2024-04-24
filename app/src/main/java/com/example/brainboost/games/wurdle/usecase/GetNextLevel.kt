package com.example.brainboost.games.wurdle.usecase

import com.example.brainboost.games.wurdle.models.Level
import com.example.brainboost.games.wurdle.repository.LevelRepository
import com.example.brainboost.games.wurdle.repository.WordRepository

class GetNextLevel(
    private val wordRepository: WordRepository,
    private val levelRepository: LevelRepository,
) {
    fun execute(): Level? {
        val currentLevelNumber = levelRepository.getCurrentLevelNumber()
        if (currentLevelNumber >= wordRepository.lastLevel + 1) return null
        return wordRepository.getWordForLevel(currentLevelNumber).let { word ->
            Level(currentLevelNumber, word)
        }
    }
}