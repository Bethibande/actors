package com.bethibande.actors.context

import com.bethibande.actors.Actor
import com.bethibande.actors.registry.ActorId
import com.bethibande.actors.registry.ActorType
import com.bethibande.actors.system.OldActorSystem

data class LocalActorContext<T: Actor<M>, M>(
    val type: ActorType,
    val id: ActorId,
    val system: OldActorSystem<T, M>,
): ActorContext<T, M> {

    override fun type(): ActorType = type

    override fun id(): ActorId = id

    override fun system(): OldActorSystem<T, M> = system
}
