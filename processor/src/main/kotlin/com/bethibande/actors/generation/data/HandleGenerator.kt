package com.bethibande.actors.generation.data

import com.bethibande.actors.annotations.ActorModel
import com.bethibande.actors.generation.Generator
import com.bethibande.actors.struct.Environment
import com.bethibande.actors.struct.FieldHandle
import com.bethibande.actors.struct.ModelHandle
import com.bethibande.actors.utils.getSymbolsWithAnnotation
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.Modifier

class HandleGenerator: Generator {

    companion object {

        const val KEY_MODEL_TYPES   = "MODELS_DECLARATIONS"
        const val KEY_MODEL_HANDLES = "MODELS_HANDLES"

    }

    override fun generate(env: Environment) {
        val elements = env.resolver.getSymbolsWithAnnotation(ActorModel::class)
            .filterIsInstance<KSClassDeclaration>()
            .filter { declaration -> declaration.parentDeclaration !is KSClassDeclaration } // No inner classes supported
            .filter { declaration -> declaration.classKind == ClassKind.CLASS }
            .filter { declaration -> declaration.modifiers.contains(Modifier.DATA) }
            .filter { declaration -> declaration.qualifiedName != null }

        val fieldHandles = elements.map { element -> element to element.getDeclaredProperties() }
            .map { (element, properties) -> processFields(element, properties) }
            .associate { pair -> pair }

        val modelHandles = elements.map { element -> ModelHandle(
            element.qualifiedName!!,
            element,
            fieldHandles[element]!!
        )}

        env.commonObjectPool.put(KEY_MODEL_TYPES, elements)
        env.commonObjectPool.put(KEY_MODEL_HANDLES, modelHandles)
    }

    private fun processFields(
        element: KSClassDeclaration,
        properties: Sequence<KSPropertyDeclaration>
    ): Pair<KSClassDeclaration, List<FieldHandle>> {
        val handles = properties.map { property -> FieldHandle(
            property,
            property.simpleName.toString(),
            property.modifiers.contains(Modifier.FINAL)
        )}.toList()

        return element to handles
    }

}