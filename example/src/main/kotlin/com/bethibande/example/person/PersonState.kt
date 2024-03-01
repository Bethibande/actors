package com.bethibande.example.person

import com.bethibande.actors.annotations.ActorState

@ActorState
data class PersonState(
    var name: String,
    var age: Int,
)
