# KSP Processor
Generates actor implementations from actor state annotated by @ActorState

- [Overview](#overview)
- [Generated files](#generated-files)
- [Generated files examples](#generated-files-examples)
    - [PersonCommand.kt](#personcommandkt)
    - [PersonCommandGetAge.kt](#personcommandgetagekt)
    - [PersonCommandSetAge.kt](#personcommandsetagekt)
    - [PersonBehaviorGetAge.kt](#personbehaviorgetagekt)
    - [PersonBehaviorSetAge.kt](#personbehaviorsetagekt)
    - [Person.kt](#personkt)
    - [PersonActor.kt](#personactorkt)
- [Custom Commands & Behavior](#custom-commands--behavior)

### Overview
For each annotated state class, commands, behaviors, api and an actor class are generated.
Custom behaviors and commands can be added at any time, see [CustomFunctionality.kt](../example/src/main/kotlin/com/bethibande/example/person/CustomFunctionality.kt).

The generated API class is the class that should be used in applications.
It contains getter & setter functions for all the state fields.
The generated actor class is primarily for internal use and contains the actor loop,
receiving messages and passing them to the configured behavior(s).

### Generated files
```kotlin
@ActorState
data class PersonState(
    val name: String,
    var age: Int,
)
```
This state will result in the following classes:
```text
package of state class
|-> Person.kt (api class, containing getters & setters)
|-> PersonActor.kt (actor class, contains config for generated behaviors)
|-> commands
    |-> PersonCommand.kt (interface)
    |-> PersonCommandGetAge.kt (command used for retrieving age field)
    |-> PersonCommandGetName.kt
    |-> PersonCommandSetAge.kt (command used for setting age field)
|-> behavior
    |-> PersonBehaviorGetAge.kt (handles get age command by reading from state)
    |-> PersonBehaviorGetName.kt
    |-> PersonBehaviorSetAge.kt (handles set age command by updating state)
```
Note: no PersonCommandSetName.kt file was generated as the name field is not mutable.

### Generated files examples
#### PersonCommand.kt
```kotlin
interface PersonCommand
```
#### PersonCommandGetAge.kt
```kotlin
data class PersonCommandGetAge(
    val deferred: CompletableDeferred<Int>,
): PersonCommand
```
#### PersonCommandSetAge.kt
```kotlin
data class PersonCommandSetAge(
    val age: Int,
): PersonCommand
```
#### PersonBehaviorGetAge.kt
```kotlin
class PersonBehaviorGetAge : Behavior<PersonCommandGetAge, PersonState> {
    override suspend fun accept(command: PersonCommandGetAge, state: PersonState) {
        command.deferred.complete(state.age)
    }
}
```
#### PersonBehaviorSetAge.kt
```kotlin
class PersonBehaviorSetAge : Behavior<PersonCommandSetAge, PersonState> {
    override suspend fun accept(command: PersonCommandSetAge, state: PersonState) {
        state.age = command.age
    }
}
```
#### Person.kt
```kotlin
open class Person(
    private val actor: PersonActor,
) {

    open suspend fun send(command: PersonCommand) {
        actor.send(command)
    }

    suspend fun getName(): String {
        val deferred = CompletableDeferred<String>()
        send(PersonCommandGetName(deferred))
        return deferred.await()
    }

    suspend fun getAge(): Int {
        val deferred = CompletableDeferred<Int>()
        send(PersonCommandGetAge(deferred))
        return deferred.await()
    }

    suspend fun setAge(age: Int) {
        send(PersonCommandSetAge(age))
    }
}
```
#### PersonActor.kt
```kotlin
class PersonActor(
    initialState: PersonState,
    behavior: BehaviorMap<PersonCommand, PersonState> = BehaviorMap,
) : AbstractActor<PersonCommand, PersonState>(behavior, initialState) {
    companion object {
        val BehaviorMap: BehaviorMap<PersonCommand, PersonState> =
            BehaviorMap<PersonCommand, PersonState>()
                .with(PersonCommandGetName::class.java, PersonBehaviorGetName())
                .with(PersonCommandGetAge::class.java, PersonBehaviorGetAge())
                .with(PersonCommandSetAge::class.java, PersonBehaviorSetAge())

    }
}
```

### Custom Commands & Behavior
Add a custom command that retrieves both name and age in one operation/command.
```kotlin
data class PersonCommandGetNameAndAge(
    val deferred: CompletableDeferred<Pair<String, Int>>
): PersonCommand {
    companion object {
        init {
            // Add the behavior to all actors of the Person type, also affects existing actors.
            PersonActor.BehaviorMap.add(PersonCommandGetNameAndAge::class.java, PersonBehaviorGetNameAndAge())
        }
    }
}

// Custom behavior that will handle our behavior
class PersonBehaviorGetNameAndAge: Behavior<PersonCommandGetNameAndAge, PersonState> {
    override suspend fun accept(command: PersonCommandGetNameAndAge, state: PersonState) {
        command.deferred.complete(state.name to state.age)
    }
}

// Function for api class
suspend fun Person.getNameAndAge(): Pair<String, Int> {
    val deferred = CompletableDeferred<Pair<String, Int>>()
    send(PersonCommandGetNameAndAge(deferred))
    return deferred.await()
}
```