package com.bethibande.actors.generation

import com.bethibande.actors.struct.ActorStateField
import com.bethibande.actors.struct.ActorStateType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import kotlinx.coroutines.CompletableDeferred

class APIGenerator {

    fun generate(
        type: ActorStateType,
        fields: List<ActorStateField>,
        environment: SymbolProcessorEnvironment
    ) {
        val name = type.simpleName.replace("State", "")
        val className = ClassName(type.packageName, name)

        val constructorSpec = FunSpec.constructorBuilder()
            .addParameter("actor", type.actorType!!)
            .build()

        val actorPropertySpec = PropertySpec.builder("actor", type.actorType!!)
            .addModifiers(KModifier.PRIVATE)
            .initializer("actor")
            .build()

        val typeSpec = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.OPEN)
            .primaryConstructor(constructorSpec)
            .addProperty(actorPropertySpec)
            .apply { addFunctions(fields, this) }
            .addKdoc("Auto generated class, api used to interact with [${type.fullyQualifiedName}]")
            .build()

        val fileSpec = FileSpec.builder(className)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(environment.codeGenerator, Dependencies(true))
    }

    private fun addFunctions(fields: List<ActorStateField>, typeSpec: TypeSpec.Builder) {
        fields.forEach { field ->
            addGetter(field, typeSpec)
            if (field.isMutable) addSetter(field, typeSpec)
        }
    }

    private fun addSetter(field: ActorStateField, typeSpec: TypeSpec.Builder) {
        val fieldType = field.type.toTypeName()
        val fieldName = field.name

        val funSpec = FunSpec.builder(field.setterName)
            .addModifiers(KModifier.SUSPEND)
            .addParameter(fieldName, fieldType)
            .addStatement("actor.send(%T($fieldName))", field.setCommand!!)
            .addKdoc("sets the ${field.name} field of [${field.parent.fullyQualifiedName}] to the given value.\n")
            .addKdoc("@param ${field.name} the new value to set")
            .build()

        typeSpec.addFunction(funSpec)
    }

    private fun addGetter(field: ActorStateField, typeSpec: TypeSpec.Builder) {
        val fieldType = field.type.toTypeName()
        val deferredType = CompletableDeferred::class.asTypeName().parameterizedBy(fieldType)

        val funSpec = FunSpec.builder(field.getterName)
            .addModifiers(KModifier.SUSPEND)
            .addStatement("val deferred = %T()", deferredType)
            .addStatement("actor.send(%T(deferred))", field.getCommand!!)
            .addStatement("return deferred.await()")
            .returns(fieldType)
            .addKdoc("Retrieves the ${field.name} field of [${field.parent.fullyQualifiedName}]\n")
            .addKdoc("@return [${field.parent.fullyQualifiedName}.${field.name}]")
            .build()

        typeSpec.addFunction(funSpec)
    }

}