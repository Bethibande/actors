package com.bethibande.actors

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KSPProvider: SymbolProcessorProvider {

    private var processor: SymbolProcessor? = null

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return processor ?: KSPProcessor(environment).apply { processor = this }
    }
}