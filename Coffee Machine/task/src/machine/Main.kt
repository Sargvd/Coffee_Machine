package machine

import java.lang.Integer.min

object CoffeeMachine {
    private enum class States {
        MAIN_CYCLE_INPUT,
        COFFEE_TYPE_INPUT,
        FILL_INPUT;
    }
    private var fillInputStep = 0
    private var currentState = States.MAIN_CYCLE_INPUT
    private var money = 550
    private val stockLevels = arrayOf(400, 540, 120, 9)
    private val labels = arrayOf("water", "milk", "coffee beans", "disposable cups")
    private val labelPrefix = arrayOf("ml of ", "ml of ", "grams of ", "")

    fun parseInput(input: String) {
        when (currentState) {
            States.MAIN_CYCLE_INPUT -> when(input) {
                "buy" -> {
                    print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
                    currentState = States.COFFEE_TYPE_INPUT
                }
                "fill" -> {
                    print("Write how many ${labelPrefix[fillInputStep]}${labels[fillInputStep]} do you want to add: ")
                    currentState = States.FILL_INPUT
                }
                "remaining" -> printStatus()
                "take" -> {
                    println("I gave you \$$money")
                    money = 0
                }
            }
            States.COFFEE_TYPE_INPUT -> currentState = when(input) {
                "back" -> States.MAIN_CYCLE_INPUT
                else -> {
                    makeCoffee(input)
                    States.MAIN_CYCLE_INPUT
                }
            }
            States.FILL_INPUT -> {
                stockLevels[fillInputStep] += input.toInt()
                fillInputStep++
                if (fillInputStep == stockLevels.size) currentState=States.MAIN_CYCLE_INPUT
                else print("Write how many ${labelPrefix[fillInputStep]}${labels[fillInputStep]} do you want to add: ")
            }
        }
    }

    private fun makeCoffee(type: String) {
        val stockRequired = when(type) {
            "1" -> arrayOf(250, 0, 16, 1)
            "2" -> arrayOf(350, 75, 20, 1)
            "3" -> arrayOf(200, 100, 12, 1)
            else -> arrayOf(0, 0, 0, 0)
        }
        var canDo = true

        for (i in stockRequired.indices) {
            if (stockRequired[i] > stockLevels[i]) {
                println("Sorry, not enough ${labels[i]}!")
                canDo = false
            }
        }

        if (canDo) {
            money += arrayOf(4, 7, 6)[type.toInt()-1]
            for (i in stockRequired.indices) stockLevels[i] -= stockRequired[i]
            println("I have enough resources, making you a coffee!")
        }
    }

    private fun printStatus() {
        println("The coffee machine has:")
        for (i in stockLevels.indices) println("${stockLevels[i]} of ${labels[i]}")
        println("\$${money} of money")
    }
}

fun main() {
    val cm = CoffeeMachine
    while(true) {
        print("Write action (buy, fill, take, remaining, exit: ")
        when (val input = readLine()!!) {
            "exit" -> break
            else -> cm.parseInput(input)
        }
    }
}
