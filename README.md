[![wakatime](https://wakatime.com/badge/user/72047d25-7643-4124-9850-1dd48ddf85f0/project/018d4022-e313-4df5-8fc4-262be0c59caf.svg)](https://wakatime.com/badge/user/72047d25-7643-4124-9850-1dd48ddf85f0/project/018d4022-e313-4df5-8fc4-262be0c59caf)
# Actors
A simple actor based programming framework for kotlin

### Example

```kotlin
import java.util.UUID
import com.bethibande.actors.annotations.ActorModel
import java.time.LocalDateTime

// Auto generates actor implementation using message channels for thread-safety
// Each actor will be a coroutine with a channel sending and receiving messages
// All messages with the same actor will the processed by the same coroutine
@ActorModel
data class PersonModel(
    val id: UUID,
    val birthDate: LocalDateTime,
    var name: String,
    val appointments: MutableList<Appointment>,
)

// Totally thread-safe, uses suspending functions and message channels
val person = PersonActor.new(UUID.randomUUID(), LocalDateTime.now())
    .withName("some name")
    .withAppointment(firstAppointment)
    .withAppointment(secondAppointment)

val name = person.getName()
val appointments = person.getAppointments()

// Creates an immutable view of the actor without set or add functions.
// This view will reflect All changes made to the original actor.
// Views can be configured to be atomic copies of the original or simply call the getter functions of the original.
// Getting a value of a copied view is faster and non-blocking but consumes more memory and cpu time
val immutableView: PersonActorView = person.view()
```

### Dependencies
- [Google KSP](https://github.com/google/ksp) for processing symbols like annotations
- [Kotlinpoet](https://github.com/square/kotlinpoet) for generating kotlin source code
- [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous programming