package com.example.brainboost.games.wurdle.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T>(
    private val initialState: T,
) {

    private val stateFlow = MutableStateFlow<T>(initialState)

    fun state(): StateFlow<T> = stateFlow

    internal fun updateState(newState: T.() -> T) {
        stateFlow.value = newState(stateFlow.value)
    }

    fun currentState(): T = state().value
}