package com.bethibande.actors

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

data class ProcessingEnvironment(
    val resolver: Resolver,
    val symbolEnvironment: SymbolProcessorEnvironment
)
