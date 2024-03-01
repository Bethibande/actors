package com.bethibande.actors.struct

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName

data class ActorStateType(
    val declaration: KSClassDeclaration,
    val type: KSType,
    val packageName: String,
    val simpleName: String,
    val fullyQualifiedName: String,
    var commandInterface: ClassName? = null,
    var actorType: ClassName? = null,
)
