package com.bethibande.actors.context

import com.bethibande.actors.Actor
import com.bethibande.actors.registry.ActorId
import com.bethibande.actors.registry.ActorType
import com.bethibande.actors.system.OldActorSystem

fun interface ContextFactory<T: Actor<M>, M> {

    companion object {
        @JvmStatic
        fun <T: Actor<M>, M> localContextFactory(): ContextFactory<T, M> {
            return ContextFactory { type, id, system -> LocalActorContext(type, id, system) }
        }
    }

    fun newContext(type: ActorType, id: ActorId, system: OldActorSystem<T, M>): ActorContext<T, M>

}