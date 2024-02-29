package com.bethibande.actors.struct

import com.bethibande.actors.cache.CommonObjectPool
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

data class Environment(
    val commonObjectPool: CommonObjectPool,
    val resolver: Resolver,
    val symbolProcessorEnvironment: SymbolProcessorEnvironment,
)