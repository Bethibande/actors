package com.bethibande.actors.collectors

import com.bethibande.actors.struct.ActorStateType
import com.bethibande.actors.struct.ActorStateField
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType

class ActorStateFieldCollector {

    fun collectFields(stateType: ActorStateType): List<ActorStateField> {
        return stateType.declaration.getDeclaredProperties()
            //.filter { !it.isExpect }
            .map { toStateField(it, stateType) }
            .toList()
    }

    private fun capitalize(name: String): String {
        return name.replaceFirstChar { it.uppercaseChar() }
    }

    private fun getterName(fieldName: String, type: KSType): String {
        val capitalizedFieldName = capitalize(fieldName)

        return when (type.declaration.qualifiedName?.asString()) {
            "kotlin.Boolean" -> "is$capitalizedFieldName"
            else -> "get$capitalizedFieldName"
        }
    }

    private fun setterName(fieldName: String, type: KSType): String {
        val capitalizedFieldName = capitalize(fieldName)
        return "set$capitalizedFieldName"
    }

    private fun toStateField(declaration: KSPropertyDeclaration, parent: ActorStateType): ActorStateField {
        val name = declaration.simpleName.asString()
        val type = declaration.type.resolve()

        return ActorStateField(
            declaration,
            type,
            name,
            getterName(name, type),
            setterName(name, type),
            declaration.isMutable,
            parent,
        )
    }

}