package com.bethibande.actors

import kotlinx.coroutines.channels.Channel

abstract class AbstractActor<MODEL, MESSAGE: Message>(private val model: MODEL): Actor<MODEL> {

    private val channel = Channel<Message>()

    protected suspend fun send(msg: MESSAGE) {
        channel.send(msg)
    }

    protected fun model() = model

    protected abstract suspend fun receive(msg: MESSAGE)

}