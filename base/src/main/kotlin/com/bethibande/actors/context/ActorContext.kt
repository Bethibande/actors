package com.bethibande.actors.context

import com.bethibande.actors.Actor
import com.bethibande.actors.registry.ActorId
import com.bethibande.actors.registry.ActorType
import com.bethibande.actors.system.OldActorSystem

interface ActorContext<T: Actor<M>, M> {

    fun type(): ActorType
    fun id(): ActorId
    fun system(): OldActorSystem<T, M>

}