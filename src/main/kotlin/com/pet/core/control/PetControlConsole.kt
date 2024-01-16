package com.pet.core.control

import com.pet.core.api.PetControl
import com.pet.core.process.Event
import com.pet.core.process.Pet
import com.pet.core.process.State
import com.pet.core.process.Stats
import kotlin.random.Random

class PetControlConsole(stat: Stats) : PetControl {

    private val pet = Pet(stat)

    init {
        pet.addOnChangeListener {
            if (it.newValue.state == it.oldValue.state) {
                return@addOnChangeListener
            }

            when (it.newValue.state) {
                State.ILL -> {
                    showMessage("I'm not well :(((")
                }

                State.HEALTHY -> {
                    showMessage("Wow! It's better than before! ^_^")
                }

                State.BORED -> {
                    showMessage("I'm a little bit depressive :(((")
                }

                State.HAPPY -> {
                    showMessage("I'm happy so much!!! ^_^")
                }

                State.HUNGRY -> {
                    showMessage("I'd like to eat an elephant 0_0")
                }

                State.FULL -> {
                    showMessage("I'm had a snack!! ^_^")
                }
            }
        }

        pet.addOnEventListener {
            when (it) {
                Event.DEATH -> {
                    showMessage("Good bye my friend...\nThank you, I was happy...")
                    pet.stop()
                }

                Event.CRAP -> {
                    showMessage("Oooops, I'm dirty a bit! o_0")
                }

                Event.ILLNESS -> {}
            }
        }
        pet.run()
    }

    override fun showStatus() {
        pet.getStats().apply {
            println("MOOD: ${mood.value}, \t[I'm ${mood.state}]")
            println("FOOD: ${food.value}, \t[I'm ${food.state}]")
            println("HEALTH: ${health.value}, \t[I'm ${health.state}]\n")
        }
    }

    override fun play() {
        println("Playing...")
        Thread.sleep(1500)
        pet.adjustMood(Random.nextInt(2, 6))
        println("We've played!!!\n")
        showStatus()
    }

    override fun clean() {
        println("Cleaning...")
        Thread.sleep(1500)
        pet.adjustMood(Random.nextInt(0, 4))
        pet.adjustHealth(Random.nextInt(1, 3))
        pet.isCrapped = false
        println("Was cleaned!!!\n")
        showStatus()
    }

    override fun feed() {
        println("Feeding...")
        Thread.sleep(1500)
        pet.adjustFood(Random.nextInt(3, 6))
        pet.adjustMood(Random.nextInt(0, 3))
        println("Was feed!!!\n")
        showStatus()
    }

    override fun treat() {
        println("Treating...")
        Thread.sleep(1500)
        pet.adjustHealth(Random.nextInt(3, 7))
        println("Was treated!!!\n")
        showStatus()
    }

    override fun exit() {
        println("Stopping a pet..")
        pet.stop()
    }

    private fun showMessage(message: String) {
        println()
        println("*".repeat(message.length))
        println(message)
        println("*".repeat(message.length))
        println()
    }
}