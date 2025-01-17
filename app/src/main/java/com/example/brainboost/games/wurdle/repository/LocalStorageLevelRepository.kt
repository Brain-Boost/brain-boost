package com.example.brainboost.games.wurdle.repository

import android.content.SharedPreferences
import com.example.brainboost.games.wurdle.models.Level
import com.example.brainboost.games.wurdle.repository.LevelRepository
import kotlin.math.max

class LocalStorageLevelRepository(
    private val sharedPreferences: SharedPreferences,
) : LevelRepository {
    private var lastLevel: Long
        get() {
            return sharedPreferences.getLong("LastLevel", 1)
        }
        set(value) {
            sharedPreferences.edit().putLong("LastLevel", value).commit()
        }

    override fun getCurrentLevelNumber(): Long {
        return lastLevel
    }

    override fun levelPassed(level: Level) {
        val settingLevel = max(level.number + 1, lastLevel)
        lastLevel = settingLevel
    }

    override fun reset() {
        lastLevel = 1
    }
}