package com.bethibande.actors.utils

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import kotlin.reflect.KClass

fun Resolver.getSymbolsWithAnnotation(annotation: KClass<out Annotation>): List<KSAnnotated> {
    return getSymbolsWithAnnotation(annotation.qualifiedName!!).toList()
}