package com.bethibande.actors.struct

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

data class FieldHandle(
    val model: ModelHandle,
    val declaration: KSPropertyDeclaration,
    val name: String,
    val readOnly: Boolean,
)