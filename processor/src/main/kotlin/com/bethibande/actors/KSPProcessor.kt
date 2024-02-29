package com.bethibande.actors

import com.bethibande.actors.cache.CommonObjectPool
import com.bethibande.actors.generation.actor.ActorGenerator
import com.bethibande.actors.generation.data.HandleGenerator
import com.bethibande.actors.struct.Environment
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

class KSPProcessor(private val environment: SymbolProcessorEnvironment): SymbolProcessor {

    private val objectPool = CommonObjectPool()
    private val generators = listOf(
        HandleGenerator(),
        ActorGenerator()
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val env = Environment(objectPool, resolver, environment)

        generators.forEach { generator -> generator.generate(env) }

        return emptyList()
    }
}