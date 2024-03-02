package com.bethibande.actors.generation

import com.bethibande.actors.collectors.ActorStateCollector
import com.bethibande.actors.collectors.ActorStateFieldCollector
import com.bethibande.actors.struct.ActorStateField
import com.bethibande.actors.struct.ActorStateType
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

class ActorGenerator {

    fun generate(resolver: Resolver, environment: SymbolProcessorEnvironment) {
        val types = ActorStateCollector().collect(resolver)
        val fields = types.associateWith { type -> ActorStateFieldCollector().collectFields(type) }

        fields.forEach { (type, fields) -> generateActor(type, fields, environment) }
    }

    private fun generateActor(type: ActorStateType, fields: List<ActorStateField>, environment: SymbolProcessorEnvironment) {
        CommandGenerator().generateCommands(type, fields, environment)
        BehaviorGenerator().generateBehaviorStub(type, fields, environment)
        ActorBaseGenerator().generateBase(type, fields, environment)
        APIGenerator().generate(type, fields, environment)
    }

}