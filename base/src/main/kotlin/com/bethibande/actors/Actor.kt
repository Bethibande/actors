package com.bethibande.actors

interface Actor<STATUS> {

    fun status(): STATUS

    fun actorKey(): ActorKey<Actor<STATUS>>

    fun actorParent(): Actor<*>?

    suspend fun sendCommand(command: Command)

    /**
     * @return true if the actor is executed on the current instance.
     */
    fun isLocal(): Boolean

}