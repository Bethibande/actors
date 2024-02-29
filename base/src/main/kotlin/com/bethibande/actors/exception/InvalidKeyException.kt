package com.bethibande.actors.exception

import com.bethibande.actors.ActorKey

class InvalidKeyException(key: ActorKey<*>): RuntimeException("Invalid key $key")