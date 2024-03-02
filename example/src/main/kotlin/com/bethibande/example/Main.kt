package com.bethibande.example

import com.bethibande.example.person.Person
import com.bethibande.example.person.PersonActor
import com.bethibande.example.person.PersonState
import com.bethibande.example.person.getNameAndAge
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val actor = PersonActor(PersonState("Max", 17))
        val api = Person(actor)

        runBlocking {
            launch { actor.run() }

            println("${api.getName()}: ${api.getAge()}")
            api.setAge(18)
            println("${api.getName()}: ${api.getAge()}")

            // Custom behavior/command (see com.bethibande.example.person.CustomFunctionality.kt)
            val (name, age) = api.getNameAndAge()
            println("Custom: $name, $age")
        }
    }

}