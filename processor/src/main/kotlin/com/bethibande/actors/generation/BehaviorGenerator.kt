package com.bethibande.actors.generation

import com.bethibande.actors.AbstractActor
import com.bethibande.actors.behavior.Behavior
import com.bethibande.actors.struct.ActorStateField
import com.bethibande.actors.struct.ActorStateType
import com.bethibande.actors.system.ActorContext
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class BehaviorGenerator {

    fun generateBehaviorStub(
        type: ActorStateType,
        fields: List<ActorStateField>,
        environment: SymbolProcessorEnvironment
    ) {
        type.actorType = ClassName(type.packageName, type.simpleName.replace("State", "Actor"))
        generateCloseBehavior(type, environment)
        fields.forEach { field -> generateBehavior(field, environment) }
    }

    private fun generateCloseBehavior(type: ActorStateType, environment: SymbolProcessorEnvironment) {
        val name = type.simpleName.replace("State", "BehaviorClose")
        val className = ClassName("${type.packageName}.behavior", name)

        val funSpec = FunSpec.builder("accept")
            .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
            .addParameter("command", type.closeCommand!!)
            .addParameter("state", type.typeName)
            .addParameter(
                "actor",
                AbstractActor::class.asTypeName().parameterizedBy(TypeVariableName("*"), TypeVariableName("*"))
            )
            .addStatement("actor.close()")
            .build()

        val behaviorType = Behavior::class.asTypeName().parameterizedBy(
            type.closeCommand!!,
            type.typeName,
        )

        val typeSpec = TypeSpec.classBuilder(className)
            .addFunction(funSpec)
            .addSuperinterface(behaviorType)
            .addKdoc("Auto-generated behavior used to close actors of type [${type.fullyQualifiedName}]")
            .build()

        val fileSpec = FileSpec.builder(className)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(environment.codeGenerator, Dependencies(true))
        type.closeBehavior = className
    }

    private fun generateBehavior(field: ActorStateField, environment: SymbolProcessorEnvironment) {
        generateGetBehavior(field, environment)
        if (field.isMutable) generateSetBehavior(field, environment)
    }

    private fun generateSetBehavior(field: ActorStateField, environment: SymbolProcessorEnvironment) {
        val fieldNameUpper = field.name.replaceFirstChar { it.uppercaseChar() }
        val name = "${field.parent.simpleName.replace("State", "Behavior")}Set$fieldNameUpper"
        val className = ClassName("${field.parent.packageName}.behavior", name)

        val stateType = field.parent.type.toTypeName()
        val superType = Behavior::class.asTypeName().parameterizedBy(
            field.setCommand!!,
            stateType,
        )

        val funSpec = FunSpec.builder("accept")
            .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
            .addParameter("command", field.setCommand!!)
            .addParameter("state", stateType)
            .addParameter(
                "actor",
                AbstractActor::class.asTypeName().parameterizedBy(TypeVariableName("*"), TypeVariableName("*"))
            )
            .addStatement("state.%N = command.%N", field.name, field.name)
            .addKdoc("Auto-generated function, handles [${field.setCommand!!.canonicalName}]")
            .build()

        val typeSpec = TypeSpec.classBuilder(className)
            .addSuperinterface(superType)
            .addFunction(funSpec)
            .addKdoc("Auto-generated behavior, for [${field.parent.fullyQualifiedName}]")
            .build()

        val fileSpec = FileSpec.builder(className)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(environment.codeGenerator, Dependencies(true))
        field.setBehavior = className
    }

    private fun generateGetBehavior(field: ActorStateField, environment: SymbolProcessorEnvironment) {
        val fieldNameUpper = field.name.replaceFirstChar { it.uppercaseChar() }
        val name = "${field.parent.simpleName.replace("State", "Behavior")}Get$fieldNameUpper"
        val className = ClassName("${field.parent.packageName}.behavior", name)

        val stateType = field.parent.type.toTypeName()
        val superType = Behavior::class.asTypeName().parameterizedBy(
            field.getCommand!!,
            stateType
        )

        val funSpec = FunSpec.builder("accept")
            .addModifiers(KModifier.OVERRIDE, KModifier.SUSPEND)
            .addParameter("command", field.getCommand!!)
            .addParameter("state", stateType)
            .addParameter(
                "actor",
                AbstractActor::class.asTypeName().parameterizedBy(TypeVariableName("*"), TypeVariableName("*"))
            )
            .addStatement("command.deferred.complete(state.%N)", field.name)
            .addKdoc("Auto-generated function, handles [${field.getCommand!!.canonicalName}]")
            .build()

        val typeSpec = TypeSpec.classBuilder(className)
            .addSuperinterface(superType)
            .addFunction(funSpec)
            .addKdoc("Auto-generated behavior, for [${field.parent.fullyQualifiedName}]")
            .build()

        val fileSpec = FileSpec.builder(className)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(environment.codeGenerator, Dependencies(true))
        field.getBehavior = className
    }

}