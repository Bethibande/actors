package com.bethibande.actors.generation

import com.bethibande.actors.AbstractActor
import com.bethibande.actors.behavior.BehaviorMap
import com.bethibande.actors.struct.ActorStateField
import com.bethibande.actors.struct.ActorStateType
import com.bethibande.actors.system.ActorContext
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class ActorBaseGenerator {

    fun generateBase(type: ActorStateType, fields: List<ActorStateField>, environment: SymbolProcessorEnvironment) {
        val name = type.simpleName.replace("State", "Actor")
        val className = ClassName(type.packageName, name)
        type.actorType = className

        val treeType = BehaviorMap::class.asTypeName()
            .parameterizedBy(type.commandInterface!!, type.typeName)

        val behaviorTreePropertySpec = buildBehaviorTree(treeType, fields)

        val companionSpec = TypeSpec.companionObjectBuilder()
            .addProperty(behaviorTreePropertySpec)
            .build()

        val contextType = ActorContext::class.asTypeName()
            .parameterizedBy(type.commandInterface!!, type.typeName)

        val constructorSpec = FunSpec.constructorBuilder()
            .addParameter("context", contextType)
            .build()

        val typeSpec = TypeSpec.classBuilder(className)
            .superclass(AbstractActor::class.asTypeName().parameterizedBy(type.commandInterface!!, type.typeName))
            .addSuperclassConstructorParameter("context")
            .primaryConstructor(constructorSpec)
            .addType(companionSpec)
            .build()

        val fileSpec = FileSpec.builder(className)
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(environment.codeGenerator, Dependencies(true))
    }

    private fun buildBehaviorTree(treeType: TypeName, fields: List<ActorStateField>): PropertySpec {
        val initializer = CodeBlock.builder()
            .addStatement("%T()", treeType)
            .apply { fields.forEach { field ->
                addStatement(".with(%T::class.java, %T())", field.getCommand!!, field.getBehavior!!)
                if (field.isMutable) addStatement(".with(%T::class.java, %T())", field.setCommand!!, field.setBehavior!!)
            } }
            .build()

        return PropertySpec.builder("BehaviorMap", treeType)
            .initializer(initializer)
            .build()
    }

}