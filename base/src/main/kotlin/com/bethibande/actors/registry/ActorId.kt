package com.bethibande.actors.registry

import com.bethibande.actors.Actor
import java.util.UUID

/**
 * Represents an identifier for an [Actor].
 */
interface ActorId {

    data class ActorIdLong(val value: Long): ActorId
    data class ActorIdUUID(val value: UUID): ActorId

}