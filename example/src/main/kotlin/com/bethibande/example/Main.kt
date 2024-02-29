package com.bethibande.example

import com.bethibande.actors.system.OldActorSystem
import com.bethibande.example.person.Person
import kotlinx.coroutines.runBlocking

object Main {

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val system = OldActorSystem.localSystem("test-system")

        val person = system.new(Person::class, ::Person)
        println(person)

        person.setName("Max")
        person.setAge(17)

        println(person)
    }

}