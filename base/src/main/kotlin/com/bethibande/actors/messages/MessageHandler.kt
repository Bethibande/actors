package com.bethibande.actors.messages

import com.bethibande.actors.Actor

fun interface MessageHandler<M> {

    companion object {
        @JvmStatic
        fun <M> localMessageSender(receiver: suspend (M) -> Unit): MessageHandler<M> {
            return MessageHandler { message, _ -> receiver.invoke(message) }
        }
    }

    suspend fun send(message: M, actor: Actor<M>)

}