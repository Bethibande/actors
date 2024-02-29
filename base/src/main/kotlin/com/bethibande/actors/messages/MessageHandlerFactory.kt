package com.bethibande.actors.messages

import com.bethibande.actors.Actor

fun interface MessageHandlerFactory<T: Actor<M>, M> {

    companion object {
        @JvmStatic
        fun <T: Actor<M>, M> localMessageHandlerFactory(): MessageHandlerFactory<T, M> {
            return MessageHandlerFactory { actor ->
                MessageHandler.localMessageSender { message -> actor.receive(message) }
            }
        }
    }

    fun newMessageHandlerForActor(actor: T): MessageHandler<M>

}