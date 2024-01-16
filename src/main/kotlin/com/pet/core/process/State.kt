package com.pet.core.process

import com.pet.core.api.Change
import com.pet.core.api.Changeable

enum class State {
    ILL, HEALTHY,
    HUNGRY, FULL,
    BORED, HAPPY,
}

class StateProperty(
    value: Int,
    private val minState: State,
    private val maxState: State,
) : Changeable<StateItem, StateChange, (StateChange) -> Unit> {

    @Volatile
    private var statValue = value

    @Volatile
    private var state = getState(statValue)

    private val onChangeListeners: MutableList<(newState: StateChange) -> Unit> = arrayListOf()

    fun adjustValue(value: Int) {
        val oldValue = StateItem(statValue, state)
        statValue = add(statValue, value)
        state = getState(statValue)
        update(StateChange(oldValue, StateItem(statValue, state)))
    }

    fun getInfo() = StateItem(statValue, state)

    override fun addOnChangeListener(listener: (change: StateChange) -> Unit) = onChangeListeners.add(listener)

    override fun update(change: StateChange) = onChangeListeners.forEach { it.invoke(change) }

    private fun getState(value: Int): State {
        val percent = 100 / Stats.MAX_STATS_VALUE * value
        return if (percent > 45) {
            maxState
        } else {
            minState
        }
    }

    private fun add(oldValue: Int, value: Int): Int {
        if (value > Stats.MAX_STATS_VALUE) {
            throw IllegalArgumentException("Don't put more MAX ${Stats.MAX_STATS_VALUE}")
        }
        val result = oldValue + value
        return when {
            result > Stats.MAX_STATS_VALUE -> Stats.MAX_STATS_VALUE
            result < 0 -> 0
            else -> result
        }
    }
}

data class StateItem(val value: Int, val state: State)

data class StateInfo(
    val mood: StateItem,
    val health: StateItem,
    val food: StateItem,
)

class StateChange(oldValue: StateItem, newValue: StateItem) : Change<StateItem>(oldValue, newValue)
