package com.bethibande.actors

import kotlinx.coroutines.channels.Channel

abstract class AbstractActor<C> {

    private val channel = Channel<C>()

    suspend fun send(command: C) {
        channel.send(command)
    }

}