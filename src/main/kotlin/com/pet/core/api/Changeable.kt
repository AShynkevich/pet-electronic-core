package com.pet.core.api

import com.pet.core.process.Event

open class Change<T>(val oldValue: T, val newValue: T)

interface Changeable<Type, K : Change<Type>, Listener> {

    fun addOnChangeListener(listener: Listener): Boolean

    fun update(change: K)
}

interface Eventable<T, K : Event> {

    fun addOnEventListener(callback: T): Boolean

    fun fire(event: K)
}