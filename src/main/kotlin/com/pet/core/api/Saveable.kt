package com.pet.core.api

import com.pet.core.process.Stats

interface Saveable {
    fun save(stat: Stats)
    fun load(): Stats
}