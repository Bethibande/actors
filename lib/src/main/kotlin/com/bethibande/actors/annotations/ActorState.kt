package com.bethibande.actors.annotations

/**
 * Annotates a class as the state for an actor,
 * the ksp processor will use this annotation to generate the actor implementation.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ActorState