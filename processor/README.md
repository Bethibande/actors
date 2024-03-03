# KSP Processor
Generates actor implementations from actor state annotated using @ActorState

- [Overview](#overview)

### Overview
For each annotated state class, commands, behaviors, api and an actor class are generated.
Custom behaviors and commands can be added at any time, see [CustomFunctionality.kt](../example/src/main/kotlin/com/bethibande/example/person/CustomFunctionality.kt).

The generated API class is the class that should be used in applications.
It contains getter & setter functions for all the state fields.
The generated actor class is primarily for internal use and contains the actor loop,
receiving messages and passing them to the configured behavior(s).
