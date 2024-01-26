package com.bethibande.actors.struct

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

data class Environment(
    val resolver: Resolver,
    val symbolProcessorEnvironment: SymbolProcessorEnvironment,
)