package com.bethibande.actors.system

import com.bethibande.actors.behavior.Behavior
import kotlinx.coroutines.CoroutineScope

data class ActorContext<C, S>(
    val id: ActorId,
    val system: ActorSystem<*, C, S>,
    val scope: CoroutineScope,
    val behavior: Behavior<C, S>,
    val initialState: S,
)
