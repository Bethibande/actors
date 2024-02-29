package com.bethibande.actors.cache

import com.bethibande.actors.Actor
import java.util.WeakHashMap

class ActorCache {

    private val actors = WeakHashMap<String, Actor<*>>()

}