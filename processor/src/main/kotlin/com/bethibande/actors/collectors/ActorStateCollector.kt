package com.bethibande.actors.collectors

import com.bethibande.actors.annotations.ActorState
import com.bethibande.actors.struct.ActorStateType
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ksp.toTypeName

class ActorStateCollector {

    fun collect(resolver: Resolver): List<ActorStateType> {
        return resolver.getSymbolsWithAnnotation(ActorState::class.java.canonicalName)
            .filterIsInstance<KSClassDeclaration>()
            //.filter { it.validate() }
            //.filter { !it.isExpect }
            //.filter { !it.isCompanionObject }
            //.filter { it.parentDeclaration is KSFile } // inner classes are not supported
            .map { toActorStateType(it) }
            .toList()
    }

    private fun toActorStateType(declaration: KSClassDeclaration): ActorStateType {
        val simpleName = declaration.simpleName.asString()
        val packageName = declaration.packageName.asString()
        val fullyQualifiedName = "$packageName.$simpleName"

        val type = declaration.asType(emptyList())

        return ActorStateType(
            declaration,
            type,
            type.toTypeName(),
            packageName,
            simpleName,
            fullyQualifiedName
        )
    }

}