package com.bethibande.actors.events

/**
 * Represents the registration of a listener/callback
 */
fun interface Registration {

    /**
     * Unregister the listener
     */
    fun unregister()

}