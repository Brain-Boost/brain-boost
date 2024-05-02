package com.example.brainboost.games.wurdle.viewmodel

import android.util.Log
import com.example.brainboost.games.wurdle.models.Level
import com.example.brainboost.games.wurdle.repository.LevelRepository
import com.example.brainboost.games.wurdle.usecase.GetNextLevel
import com.example.brainboost.games.wurdle.usecase.ResetLevels
import com.example.brainboost.ringer.AlarmRing

class LevelsViewModel(
    private val levelRepository: LevelRepository,
    private val getNextLevel: GetNextLevel,
    private val resetLevels: ResetLevels,
) : BaseViewModel<LevelsViewModel.State>(State()) {
    data class State(
        val currentLevel: Level? = null,
        val lastLevelReached: Boolean = false,

        )

    init {
        updateLevel()
    }

    fun levelPassed(alarmRing: AlarmRing) {
        Log.d("Wurdle-Stuff", "Wurdle win condition met!")
        currentState().currentLevel?.let { levelRepository.levelPassed(it) }
        if (alarmRing.isPlaying()) {
            alarmRing.stopRingtone()
        }
        updateLevel()

    }

    private fun updateLevel() {
        val nextLevel = getNextLevel.execute()
        if (nextLevel == null) {
            updateState { copy(lastLevelReached = true, currentLevel = null) }
            return
        }
        updateState {
            copy(currentLevel = nextLevel, lastLevelReached = false)
        }
    }

    fun reset() {
        resetLevels.execute()
        updateLevel()
    }
}