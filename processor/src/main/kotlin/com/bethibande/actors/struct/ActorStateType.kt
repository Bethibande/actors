package com.bethibande.actors.struct

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

data class ActorStateType(
    val declaration: KSClassDeclaration,
    val type: KSType,
    val typeName: TypeName,
    val packageName: String,
    val simpleName: String,
    val fullyQualifiedName: String,
    var commandInterface: ClassName? = null,
    var closeCommand: ClassName? = null,
    var closeBehavior: ClassName? = null,
    var actorType: ClassName? = null,
)
