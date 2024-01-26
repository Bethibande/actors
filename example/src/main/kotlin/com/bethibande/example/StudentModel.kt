package com.bethibande.example

import com.bethibande.actors.annotations.ActorModel

@ActorModel
data class StudentModel(
    val id: String,
    var name: String,
    var classes: List<ClassModel>,
)