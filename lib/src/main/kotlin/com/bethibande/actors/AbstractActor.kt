package com.bethibande.actors

import com.bethibande.actors.behavior.Behavior
import kotlinx.coroutines.channels.Channel

abstract class AbstractActor<C, S>(private val behavior: Behavior<C, S>, initialState: S) {

    private val state = initialState
    private val channel = Channel<C>()

    suspend fun send(command: C) {
        channel.send(command)
    }

    suspend fun run() {
        for (command in channel) {
            behavior.accept(command, state)
        }
    }

}
