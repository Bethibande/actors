package com.bethibande.actors

import com.bethibande.actors.generation.ActorGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

class KSPProcessor(private val environment: SymbolProcessorEnvironment): SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        ActorGenerator().generate(resolver, environment)
        return emptyList()
    }
}