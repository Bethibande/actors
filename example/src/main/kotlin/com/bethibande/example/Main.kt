package com.bethibande.example

import com.bethibande.example.person.Person
import com.bethibande.example.person.PersonState
import com.bethibande.example.person.getNameAndAge
import kotlinx.coroutines.runBlocking

object Main {

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        // Create actor-system
        val system = Person.localActorSystem()
        // Create a new actor
        val person: Person = system.new(PersonState("Max", 17))

        // Use actor
        println("${person.getName()}: ${person.getAge()}")
        person.setAge(18)
        println("${person.getName()}: ${person.getAge()}")

        // Custom behavior/command (see com.bethibande.example.person.CustomFunctionality.kt)
        val (name, age) = person.getNameAndAge()
        println("Custom: $name, $age")

        // Send close command
        person.close()
    }

}