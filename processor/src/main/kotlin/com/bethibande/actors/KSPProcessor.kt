package com.bethibande.actors

import com.bethibande.actors.utils.getAllClasses
import com.bethibande.actors.utils.getAllImplementationsOf
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

class KSPProcessor(private val environment: SymbolProcessorEnvironment): SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val env = ProcessingEnvironment(resolver, environment)

        val interfaceActor = resolver.getClassDeclarationByName("com.bethibande.actors.Actor")!!
        val classes = resolver.getAllClasses()
        val actors = classes.getAllImplementationsOf(interfaceActor)



        return emptyList()
    }
}