package com.example.brainboost.games.wurdle.usecase

import com.example.brainboost.games.wurdle.repository.LevelRepository

class ResetLevels(
    private val levelRepository: LevelRepository,
) {
    fun execute() {
        levelRepository.reset()
    }
}