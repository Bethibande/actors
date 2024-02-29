package com.bethibande.actors.events

import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class EventFlow<E>() {

    private val flow = MutableSharedFlow<E>()
    private val listeners = CopyOnWriteArrayList<Consumer<E>>()

    fun start(job: Job?): Job {
        val context = job?.plus(Dispatchers.Default) ?: Dispatchers.Default
        val scope = CoroutineScope(context)

        return scope.launch {
            flow.collect { event ->
                listeners.forEach { it.accept(event) }
            }
        }
    }

    suspend fun post(event: E) = flow.emit(event)

    fun subscribe(listener: Consumer<E>): Registration {
        listeners.add(listener)
        return Registration { listeners.remove(listener) }
    }

}