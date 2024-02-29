package com.bethibande.actors.system

import com.bethibande.actors.Actor
import com.bethibande.actors.context.ActorContext
import com.bethibande.actors.context.ContextFactory
import com.bethibande.actors.messages.MessageHandler
import com.bethibande.actors.messages.MessageHandlerFactory
import com.bethibande.actors.registry.ActorId
import com.bethibande.actors.registry.ActorRegistry
import com.bethibande.actors.registry.ActorType
import com.bethibande.actors.registry.LocalActorRegistry
import kotlin.reflect.KClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class OldActorSystem<T: Actor<M>, M>(
    private val id: String,
    private val type: KClass<T>,
    private val idFactory: IdFactory,
    private val registry: ActorRegistry,
    private val contextFactory: ContextFactory<T, M>,
    private val actorFactory: (ActorContext<T, M>) -> T,
    private val messageHandlerFactory: MessageHandlerFactory<T, M>,
) {

    private val actorType = ActorType.ofKClass(type)

    private val root = Job()
    private val scope = CoroutineScope(root + Dispatchers.Default)

    companion object {
        fun <T: Actor<M>, M> localSystem(
            id: String,
            type: KClass<T>,
            actorFactory: (ActorContext<T, M>) -> T
        ): OldActorSystem<T, M> {
            return OldActorSystem(
                id,
                type,
                IdFactory.localLongIdFactory(),
                LocalActorRegistry(),
                ContextFactory.localContextFactory(),
                actorFactory,
                MessageHandlerFactory.localMessageHandlerFactory()
            )
        }
    }

    fun id() = id
    fun type() = type
    fun actorType() = actorType

    open suspend fun new(): T {
        return new(idFactory.createId(actorType), messageHandlerFactory)
    }

    open suspend fun new(id: ActorId, messageHandlerFactory: MessageHandler<M>): T {
        val context = contextFactory.newContext(
            actorType,
            id,
            this,
        )

        val actor = actorFactory.invoke(context)
        actor.withMessageHandler()
        registry.register(actor)

        return actor
    }

    open suspend fun close(actor: Actor<*>) {
        if (!actor.isClosed()) actor.close()
        registry.unregister(actor)
    }

}