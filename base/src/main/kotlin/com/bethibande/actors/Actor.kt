package com.bethibande.actors

import com.bethibande.actors.context.ActorContext
import com.bethibande.actors.messages.MessageHandler
import com.bethibande.actors.registry.ActorId
import com.bethibande.actors.registry.ActorType
import java.util.concurrent.atomic.AtomicBoolean

abstract class Actor<M>(private val context: ActorContext<*, M>) {

    private val type: ActorType = context.type()
    private val id: ActorId = context.id()

    private val closed = AtomicBoolean(false)
    private var handler: MessageHandler<M>? = null

    fun id() = id
    fun type() = type

    suspend fun send(message: M) {
        if (closed.get() || handler == null) throw IllegalStateException("Actor is not open.")
        handler!!.send(message, this)
    }

    internal abstract suspend fun receive(message: M)

    suspend fun close() {
        if (!closed.compareAndSet(false, true)) throw IllegalStateException("Actor is already closed")
        context.system().close(this)
    }

    fun isClosed() = closed.get()

    fun 
            ithMessageHandler(handler: MessageHandler<M>) = apply { this.handler = handler }

}