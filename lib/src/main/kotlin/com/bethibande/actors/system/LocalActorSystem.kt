package com.bethibande.actors.system

import com.bethibande.actors.AbstractActor
import com.bethibande.actors.behavior.Behavior
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin

class LocalActorSystem<A, T: AbstractActor<C, S>, C, S>(
    private val actorFactory: (ActorContext<C, S>) -> T,
    private val apiFactory: (T) -> A,
    private val defaultBehavior: Behavior<C, S>,
): ActorSystem<A, C, S> {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private fun newId(): ActorId = ActorId.ActorIdUUID(UUID.randomUUID())

    override suspend fun new(state: S, id: ActorId?, behavior: Behavior<C, S>?): A {
        val context = ActorContext(
            id ?: newId(),
            this,
            scope,
            behavior ?: defaultBehavior,
            state,
        )

        val actor = actorFactory.invoke(context)
        actor.launch()
        val api = apiFactory.invoke(actor)

        return api
    }

    override suspend fun close() {
        job.cancelAndJoin()
    }

    override suspend fun isOpen(): Boolean = job.isActive && !job.isCancelled
}