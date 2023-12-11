package com.pet.core.api

import com.pet.core.process.Event

interface Changeable<T, K> {

    fun addOnChangeListener(listener: T): Boolean

    fun update(newValue: K)
}

interface Eventable<T, K: Event> {

    fun addOnEventListener(callback: T): Boolean

    fun fire(newValue: K)
}