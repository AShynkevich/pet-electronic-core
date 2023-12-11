package com.pet.core.api

interface PetControl {

    fun showStatus()

    fun play() // affects: mood

    fun clean() // affects: health & mood

    fun feed() // affects: food

    fun treat() // affect: health

    fun exit()
}