package com.bethibande.actors.behavior

import java.util.TreeMap
import kotlin.reflect.KClass

/**
 * A behavior tree representing a configurable list of behaviors.
 * Each configured behavior accepts a given type of command.
 * When the BehaviorTree accepts a command,
 * it routes the command to the behavior that is configured to handle the command.
 *
 * The BehaviorTree is backed by a TreeMap.
 * It cannot route commands based on superclasses of the command.
 *
 * @param C root command interface used by the actor
 * @param S state class used for the actor
 */
class BehaviorTree<C: Any, S>: Behavior<C, S> {

    private val behaviors = TreeMap<KClass<out C>, Behavior<C, S>>()

    @Suppress("UNCHECKED_CAST")
    fun <T: C> add(commandType: KClass<T>, behavior: Behavior<T, S>) {
        behaviors[commandType] = behavior as Behavior<C, S>
    }

    override fun accept(command: C, state: S) {
        val type = command::class
        val behavior = behaviors[type]
        behavior?.accept(command, state)
    }
}