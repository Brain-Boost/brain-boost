package com.example.brainboost.games.wurdle.repository

import com.example.brainboost.games.wurdle.models.Word

interface WordRepository {
    val lastLevel: Long
    fun find(word: Word): Boolean
    fun random(): Word
    fun getWordForLevel(currentLevelNumber: Long): Word
}

