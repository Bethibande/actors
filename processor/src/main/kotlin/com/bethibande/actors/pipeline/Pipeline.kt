package com.bethibande.actors.pipeline

class Pipeline<I: Any, O: Any>(
    private val steps: List<PipelineStep<Any, Any>>,
) {

    @Suppress("UNCHECKED_CAST")
    fun process(input: I): O {
        var value: Any = input
        steps.forEach { step -> value = step.process(value) }

        return value as O
    }

}