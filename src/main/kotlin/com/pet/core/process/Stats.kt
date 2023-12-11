package com.pet.core.process

data class Stats(
    val name: String,
    val food: Int = MAX_STATS_VALUE,
    val health: Int = MAX_STATS_VALUE,
    val mood: Int = MAX_STATS_VALUE,
    val alive: Boolean = true,
) {
    companion object {
        const val MAX_STATS_VALUE: Int = 10
    }
}
