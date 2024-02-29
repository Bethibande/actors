package com.bethibande.actors.registry

import com.bethibande.actors.Actor
import kotlin.reflect.KClass

/**
 * Represents a type of [Actor].
 */
interface ActorType {

    companion object {
        @JvmStatic
        fun ofKClass(clazz: KClass<out Actor<*>>): ActorType {
            return FullyQualifiedActorType(clazz.java.canonicalName)
        }
    }

    data class FullyQualifiedActorType(val fullyQualifiedName: String): ActorType

}