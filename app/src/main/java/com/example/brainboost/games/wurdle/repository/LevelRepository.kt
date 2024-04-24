package com.example.brainboost.games.wurdle.repository

import com.example.brainboost.games.wurdle.models.Level

interface LevelRepository {
    fun getCurrentLevelNumber(): Long
    fun levelPassed(level: Level)
    fun reset()
}