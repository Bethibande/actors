package com.bethibande.actors.utils

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

fun Resolver.getAllClasses(): List<KSClassDeclaration> {
    return getNewFiles()
        .map { file -> file.declarations }
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.validate() }
        .toList()
}

fun List<KSClassDeclaration>.getAllImplementationsOf(declaration: KSClassDeclaration): List<KSClassDeclaration> {
    val type = declaration.asType(emptyList())
    return filter { it.superTypes.map { superType -> superType.resolve() }.contains(type) }
}