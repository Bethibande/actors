# KSP processor
Generates actor implementations from actor state annotated by @ActorState

For each state, an interface, an implementation for the interface, command classes,
and a stub for handling the commands and mutating the actors state are generated.

The generated interface serves as the API used to interact with the actor like getter and setter functions.
The generated implementation and command interface are open and may be extended
to add additional functionality.