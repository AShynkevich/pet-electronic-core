import com.pet.core.control.PetControlConsole
import com.pet.core.process.Stats

fun main() {
    val name = stringInput("Hello! How would you name your pet?")
    println("Hello I'm $name. I'm happy to be your friend!")

    val control = PetControlConsole(Stats(name))
    while (true) {
        println(
            """
       ===================     
       0) Exit
       1) Check Status
       2) Feed me
       3) Play with me
       4) Treat me
       5) Cleaning
       ===================
    """
        )
        when (intInput("Please enter your action: ")) {
            0 -> {
                control.exit()
                break
            }

            1 -> control.showStatus()
            2 -> control.feed()
            3 -> control.play()
            4 -> control.treat()
            5 -> control.clean()
            else -> println("The option does not exist\n\n")
        }
    }

    println("See you! ^_^")
}

fun stringInput(messagePrompt: String): String {
    while (true) {
        println(messagePrompt)
        val value = readlnOrNull()
        if (value.isNullOrBlank()) {
            println("The value should not be empty\n\n")
            continue
        }
        return value
    }
}

fun intInput(messagePrompt: String): Int {
    while (true) {
        val value = stringInput(messagePrompt)
        try {
            return value.toInt()
        } catch (ex: NumberFormatException) {
            println("Please enter a correct number!\n\n")
        }
    }
}