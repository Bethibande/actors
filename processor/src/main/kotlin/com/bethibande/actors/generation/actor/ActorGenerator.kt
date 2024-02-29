package com.bethibande.actors.generation.actor

import com.bethibande.actors.generation.Generator
import com.bethibande.actors.generation.data.HandleGenerator
import com.bethibande.actors.struct.Environment
import com.bethibande.actors.struct.ModelHandle
import com.google.devtools.ksp.symbol.KSClassDeclaration

class ActorGenerator: Generator {

    override fun generate(env: Environment) {
        val models = env.commonObjectPool.get<List<ModelHandle>>(HandleGenerator.KEY_MODEL_HANDLES)
    }



}