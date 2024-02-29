package com.bethibande.example

import java.time.LocalDateTime

@ActorModel
data class ClassModel(
    var name: String,
    var teacher: String,
    var time: LocalDateTime,
)
