package com.bethibande.actors

import com.bethibande.actors.service.ShardInstance
import kotlin.reflect.KClass

data class ActorKey<T: Actor<*>>(
    val type: KClass<T>,
    val id: String,
    val shard: ShardInstance?,
)
