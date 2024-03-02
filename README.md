[![wakatime](https://wakatime.com/badge/user/72047d25-7643-4124-9850-1dd48ddf85f0/project/018d4022-e313-4df5-8fc4-262be0c59caf.svg)](https://wakatime.com/badge/user/72047d25-7643-4124-9850-1dd48ddf85f0/project/018d4022-e313-4df5-8fc4-262be0c59caf)
# Actors
A simple actor based programming framework for kotlin.

> [!WARNING]
> This project is still WIP and lacking in features.
> The implementation also still needs some work and refactoring, especially the KSP processor.
> This framework is not ready for use!

### Example (WIP)
Please note, this API is still WIP and lacking in features, especially the creation of actor instances
will be simpler and not require manually launching the actor.

[PersonState.kt](example/src/main/kotlin/com/bethibande/example/person/PersonState.kt)
```kotlin
import com.bethibande.actors.annotations.ActorState

@ActorState
data class PersonState(
    var name: String,
    var age: Int,
)
```
[Main.kt](example/src/main/kotlin/com/bethibande/example/Main.kt)
```kotlin
val actor = PersonActor(PersonState("Max", 17)) // actor containing message-channel
val api = Person(actor) // generated class, containing getter and setter functions

runBlocking {
    // run actor in a new coroutine
    launch { actor.run() }
    
    // use generated api
    println("${api.getName()}: ${api.getAge()}")
    
    api.setAge(18)
    
    println("${api.getName()}: ${api.getAge()}")

    // Custom behavior/command (see com.bethibande.example.person.CustomFunctionality.kt)
    val (name, age) = api.getNameAndAge()
    println("Custom: $name, $age")
}
```
[CustomFunctionality.kt](example/src/main/kotlin/com/bethibande/example/person/CustomFunctionality.kt)
```kotlin
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
    override suspend fun accept(command: PersonCommandGetNameAndAge, state: PersonState) {
        command.deferred.complete(state.name to state.age)
    }
}

suspend fun Person.getNameAndAge(): Pair<String, Int> {
    val deferred = CompletableDeferred<Pair<String, Int>>()
    send(PersonCommandGetNameAndAge(deferred))
    return deferred.await()
}
```

### Dependencies
- [Google KSP](https://github.com/google/ksp) for processing symbols like annotations
- [Kotlinpoet](https://github.com/square/kotlinpoet) for generating kotlin source code
- [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous programming