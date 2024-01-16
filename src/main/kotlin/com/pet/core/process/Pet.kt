package com.pet.core.process

import com.pet.core.api.Changeable
import com.pet.core.api.Eventable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class Pet(stats: Stats) :
    Changeable<StateItem, StateChange, (change: StateChange) -> Unit>,
    Eventable<(event: Event) -> Unit, Event> {
    val name = stats.name

    private val statsToAffect = listOf(StatsAffect.FOOD, StatsAffect.HEALTH, StatsAffect.MOOD)
    private val onStateChangeListeners: MutableList<(state: StateChange) -> Unit> = arrayListOf()
    private val onEventListeners: MutableList<(event: Event) -> Unit> = arrayListOf()

    @Volatile
    private var mood = StateProperty(stats.mood, State.BORED, State.HAPPY)

    @Volatile
    private var health = StateProperty(stats.health, State.ILL, State.HEALTHY)

    @Volatile
    private var food = StateProperty(stats.food, State.HUNGRY, State.FULL)

    @Volatile
    private var alive = true

    @Volatile
    private var isRunning = true

    @Volatile
    var isCrapped = false

    fun getStats() = StateInfo(mood.getInfo(), health.getInfo(), food.getInfo())

    fun adjustMood(value: Int) {
        mood.adjustValue(value)
    }

    fun adjustHealth(value: Int) {
        health.adjustValue(value)
        if (health.getInfo().value == 0) {
            alive = false
            fire(Event.DEATH)
        }
    }

    fun adjustFood(value: Int) {
        food.adjustValue(value)
        if (health.getInfo().value == 0) {
            alive = false
            fire(Event.DEATH)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun run() {
        GlobalScope.launch {
            mainLoop()
        }
    }

    fun stop() {
        isRunning = false
    }

    override fun addOnEventListener(callback: (event: Event) -> Unit) = onEventListeners.add(callback)

    override fun fire(event: Event) = onEventListeners.forEach { it.invoke(event) }

    override fun addOnChangeListener(listener: (state: StateChange) -> Unit) = onStateChangeListeners.add(listener)

    override fun update(change: StateChange) = onStateChangeListeners.forEach { it.invoke(change) }

    private fun mainLoop() {
        val onChangeCallBack: (StateChange) -> Unit = { newState ->
            update(newState)
        }
        mood.addOnChangeListener(onChangeCallBack)
        health.addOnChangeListener(onChangeCallBack)
        food.addOnChangeListener(onChangeCallBack)

        while (isRunning && alive) {
            Thread.sleep(1000L * SECONDS_INTERVAL)
            val destiny = Random.nextInt(5)
            if (destiny + 1 > statsToAffect.size) {
                continue
            }

            val value = Random.nextInt(1, 3)
            when (statsToAffect[destiny]) {
                StatsAffect.MOOD -> adjustMood(
                    if (isCrapped) {
                        -(value + 2)
                    } else {
                        -value
                    }
                )

                StatsAffect.HEALTH -> adjustHealth(-value)
                StatsAffect.FOOD -> adjustFood(-value)
            }

            when (Random.nextInt(0, 21)) {
                in 3..8 -> {
                    fire(Event.CRAP)
                    isCrapped = true
                    adjustMood(-Random.nextInt(2, 4))
                    adjustHealth(-Random.nextInt(0, 3))
                }

                in 17..20 -> {
                    fire(Event.ILLNESS)
                    adjustMood(-Random.nextInt(0, 3))
                    adjustHealth(-Random.nextInt(4, 6))
                }

                else -> continue
            }
        }
    }

    companion object {
        const val SECONDS_INTERVAL = 10
    }

    enum class StatsAffect {
        MOOD, HEALTH, FOOD
    }
}