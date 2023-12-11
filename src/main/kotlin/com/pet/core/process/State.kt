package com.pet.core.process

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
) : Changeable<(State) -> Unit, State> {

    @Volatile
    private var statValue = value

    @Volatile
    private var state = getState(statValue)

    private val onChangeListeners: MutableList<(newState: State) -> Unit> = arrayListOf()

    fun adjustValue(value: Int) {
        statValue = add(statValue, value)
        val oldValue = state
        state = getState(statValue)
        if (oldValue != state) {
            update(state)
        }
    }

    fun getInfo() = StateItem(statValue, state)

    override fun addOnChangeListener(listener: (newState: State) -> Unit) = onChangeListeners.add(listener)

    override fun update(newValue: State) = onChangeListeners.forEach { it.invoke(newValue) }

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
