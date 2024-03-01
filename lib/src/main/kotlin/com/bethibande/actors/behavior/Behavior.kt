package com.bethibande.actors.behavior

/**
 * A behavior is a response to an actor receiving a command.
 * The behavior may freely mutate the [AbstractActor]s state or perform some other kind of action.
 * A Behavior should not have its own state as the same behavior instance may be used for multiple actor instances of the same type.
 * Therefore, if a behavior has its own state, it must be thread-safe and independent of any actors.
 * @param C the type of commands the behavior can handle.
 * @param S the state type of the actor.
 */
interface Behavior<C, S> {

    fun accept(command: C, state: S)

}