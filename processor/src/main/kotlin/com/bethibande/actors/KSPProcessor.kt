package com.bethibande.actors

import com.bethibande.actors.cache.ObjectStore
import com.bethibande.actors.pipeline.PipelineBuilder
import com.bethibande.actors.struct.ModelHandle
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

class KSPProcessor(private val environment: SymbolProcessorEnvironment): SymbolProcessor {

    val cache = ObjectStore()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val pipelineGenerateActors = PipelineBuilder.ofStep<Sequence<KSClassDeclaration>, ModelHandle> {
            TODO("Not yet implemented")
        }.build()

        val root = PipelineBuilder.ofStep<Resolver, Sequence<KSClassDeclaration>> {
            resolver.getSymbolsWithAnnotation("com.bethibande.actors.ActorModel")
                .filterIsInstance<KSClassDeclaration>()
                .filter { it -> it.qualifiedName != null }
        }.withPipeline(pipelineGenerateActors).build()

        root.process(resolver)

        return emptyList()
    }
}