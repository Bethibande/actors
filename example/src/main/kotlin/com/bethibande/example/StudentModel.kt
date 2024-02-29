package com.bethibande.example

@ActorModel
data class StudentModel(
    val id: String,
    var name: String,
    var classes: List<ClassModel>,
)