package com.bethibande.actors.struct

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSName

data class ModelHandle(
    val name: KSName,
    val declaration: KSClassDeclaration,
    val fields: List<FieldHandle>
)