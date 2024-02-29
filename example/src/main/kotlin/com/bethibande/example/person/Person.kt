package com.bethibande.example.person

import com.bethibande.actors.Actor
import kotlinx.coroutines.CompletableDeferred

class Person(ctx: ActorContext<Command>): Actor<Command>(ctx) {

    private var name: String? = null
    private var age: Int? = null

    override suspend fun receive(command: Command) {
        when (command) {
            is Command.CommandSetName -> name = command.name
            is Command.CommandSetAge -> age = command.age
            is Command.CommandGetName -> command.deferred.complete(name)
            is Command.CommandGetAge -> command.deferred.complete(age)
        }
    }

    suspend fun setName(name: String) {
        send(Command.CommandSetName(name))
    }

    suspend fun setAge(age: Int) {
        send(Command.CommandSetAge(age))
    }

    suspend fun getName(): String? {
        return CompletableDeferred<String?>()
            .also { send(Command.CommandGetName(it)) }
            .await()
    }

    suspend fun getAge(): Int? {
        return CompletableDeferred<Int?>()
            .also { send(Command.CommandGetAge(it)) }
            .await()
    }

    override fun toString(): String {
        return "Person(${type()}, ${id()}, name=$name, age=$age)"
    }
}