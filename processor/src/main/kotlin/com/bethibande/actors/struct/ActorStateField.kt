package com.bethibande.actors.struct

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName

data class ActorStateField(
    val declaration: KSPropertyDeclaration,
    val type: KSType,
    val name: String,
    val getterName: String,
    val setterName: String,
    val isMutable: Boolean,
    val parent: ActorStateType,
    var getCommand: ClassName? = null,
    var setCommand: ClassName? = null,
)