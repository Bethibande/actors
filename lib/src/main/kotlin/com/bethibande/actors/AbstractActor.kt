package com.bethibande.actors

import com.bethibande.actors.system.ActorContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

abstract class AbstractActor<C, S>(private val context: ActorContext<C, S>) {

    private val state = context.initialState
    private val channel = Channel<C>()

    private var job: Job? = null

    suspend fun send(command: C) {
        channel.send(command)
    }

    fun launch(): Job {
        if (job != null) throw IllegalStateException("Actor has already been launched.")
        job = context.scope.launch { run() }
        return job!!
    }

    fun isRunning(): Boolean = job != null && job!!.isActive
    fun isClosed(): Boolean = job != null && (job!!.isCancelled || job!!.isCompleted)

    fun close(reason: Throwable? = null) {
        channel.close(reason)
    }

    fun actorId() = context.id
    fun system() = context.system

    suspend fun run() {
        for (command in channel) {
            context.behavior.accept(command, state, this)
        }
    }

}
