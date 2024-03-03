package com.bethibande.actors.system

import java.util.UUID

interface ActorId {

    data class ActorIdUUID(
        val value: UUID
    ): ActorId

}