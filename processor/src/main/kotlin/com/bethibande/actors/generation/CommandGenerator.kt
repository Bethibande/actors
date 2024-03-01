package com.bethibande.actors.generation

import com.bethibande.actors.struct.ActorStateField
import com.bethibande.actors.struct.ActorStateType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import kotlinx.coroutines.CompletableDeferred

class CommandGenerator {

    fun generateCommands(type: ActorStateType, fields: List<ActorStateField>, environment: SymbolProcessorEnvironment) {
        val commandInterfaceName = generateCommandInterface(type, environment)
        type.commandInterface = commandInterfaceName

        fields.forEach { field -> generateCommands(field, commandInterfaceName, environment) }
    }

    private fun generateCommands(
        field: ActorStateField,
        commandInterface: ClassName,
        environment: SymbolProcessorEnvironment
    ) {
        val getter = generateGetCommand(field, commandInterface, environment)
        field.getCommand = getter

        if (field.isMutable) {
            val setter = generateSetCommand(field, commandInterface, environment)
            field.setCommand = setter
        }
    }

    private fun generateSetCommand(
        field: ActorStateField,
        commandInterface: ClassName,
        environment: SymbolProcessorEnvironment
    ): ClassName {
        val name = "${commandInterface.simpleName}Set${field.name.replaceFirstChar { it.uppercaseChar() }}"
        val className = ClassName(commandInterface.packageName, name)

        val typeSpec = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addSuperinterface(commandInterface)
            .primaryConstructor(FunSpec.constructorBuilder()
                .addParameter(field.name, field.type.toTypeName())
                .build())
            .addProperty(PropertySpec.builder(field.name, field.type.toTypeName())
                .initializer(field.name)
                .build())
            .addKdoc("Auto generated command interface used to assign a value to ${field.name} of the [${field.parent.fullyQualifiedName}] class")
            .build()

        val fileSpec = FileSpec.builder(className)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(environment.codeGenerator, Dependencies(true))
        return className
    }

    private fun generateGetCommand(
        field: ActorStateField,
        commandInterface: ClassName,
        environment: SymbolProcessorEnvironment
    ): ClassName {
        val name = "${commandInterface.simpleName}Get${field.name.replaceFirstChar { it.uppercaseChar() }}"
        val className = ClassName(commandInterface.packageName, name)

        val deferredType = CompletableDeferred::class.asClassName().parameterizedBy(field.type.toTypeName())
        val parameterSpec = ParameterSpec.builder("deferred", deferredType).build()
        val propertySpec = PropertySpec.builder("deferred", deferredType)
            .initializer("deferred")
            .build()

        val typeSpec = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addSuperinterface(commandInterface)
            .primaryConstructor(FunSpec.constructorBuilder()
                .addParameter(parameterSpec)
                .build())
            .addProperty(propertySpec)
            .addKdoc("Auto generated command interface used to retrieve the ${field.name} field of the [${field.parent.fullyQualifiedName}] class")
            .build()

        val fileSpec = FileSpec.builder(className)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(environment.codeGenerator, Dependencies(true))
        return className
    }

    private fun generateCommandInterface(type: ActorStateType, environment: SymbolProcessorEnvironment): ClassName {
        val name = type.simpleName.replace("State", "Command")
        val className = ClassName("${type.packageName}.commands", name)

        val typeSpec = TypeSpec.interfaceBuilder(className)
            .addKdoc("Auto generated command interface for the actor [${type.fullyQualifiedName}]")
            .build()

        val fileSpec = FileSpec.builder(className.packageName, className.simpleName)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(environment.codeGenerator, Dependencies(true))

        return className
    }

}