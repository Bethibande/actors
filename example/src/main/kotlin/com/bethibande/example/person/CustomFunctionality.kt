package com.bethibande.example.person

import com.bethibande.actors.AbstractActor
import com.bethibande.actors.behavior.Behavior
import com.bethibande.example.person.commands.PersonCommand
import kotlinx.coroutines.CompletableDeferred

data class PersonCommandGetNameAndAge(
    val deferred: CompletableDeferred<Pair<String, Int>>
): PersonCommand {
    companion object {
        init {
            // Adds the behavior to all actors of the Person type, also affects existing actors.
            PersonActor.BehaviorMap.add(PersonCommandGetNameAndAge::class.java, PersonBehaviorGetNameAndAge())
        }
    }
}

class PersonBehaviorGetNameAndAge: Behavior<PersonCommandGetNameAndAge, PersonState> {
    override suspend fun accept(command: PersonCommandGetNameAndAge, state: PersonState, actor: AbstractActor<*, *>) {
        command.deferred.complete(state.name to state.age)
    }
}

suspend fun Person.getNameAndAge(): Pair<String, Int> {
    val deferred = CompletableDeferred<Pair<String, Int>>()
    send(PersonCommandGetNameAndAge(deferred))
    return deferred.await()
}