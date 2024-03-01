package com.bethibande.actors.generation

import com.bethibande.actors.AbstractActor
import com.bethibande.actors.struct.ActorStateField
import com.bethibande.actors.struct.ActorStateType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class ActorBaseGenerator {

    fun generateBase(type: ActorStateType, fields: List<ActorStateField>, environment: SymbolProcessorEnvironment) {
        val name = type.simpleName.replace("State", "Actor")
        val className = ClassName(type.packageName, name)
        type.actorType = className

        val typeSpec = TypeSpec.classBuilder(className)
            .superclass(AbstractActor::class.asTypeName().parameterizedBy(type.commandInterface!!))
            .build()

        val fileSpec = FileSpec.builder(className)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(environment.codeGenerator, Dependencies(true))
    }

}