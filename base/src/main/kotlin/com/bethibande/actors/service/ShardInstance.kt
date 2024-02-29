package com.bethibande.actors.service

import com.bethibande.actors.Actor
import com.bethibande.actors.ActorKey
import com.bethibande.actors.exception.InvalidKeyException
import java.net.SocketAddress
import kotlin.jvm.Throws

interface ShardInstance {

    fun id(): String
    fun address(): SocketAddress

    fun alive(): Boolean

    /**
     * Creates a proxy of an actor running on another shard
     * @throws InvalidKeyException if the given key doesn't belong to the shard
     */
    @Throws(InvalidKeyException::class)
    fun <T: Actor<*>> fetchActor(key: ActorKey<T>): T

}