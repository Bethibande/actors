package com.bethibande.actors.struct

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

data class FieldHandle(
    val declaration: KSPropertyDeclaration,
    val name: String,
    val readOnly: Boolean,
)