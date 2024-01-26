package com.bethibande.actors.pipeline

import java.util.function.Function

class PipelineBuilder<I: Any, CO: Any> internal constructor(
    private val steps: List<PipelineStep<Any, Any>>,
) {

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun <I: Any, O: Any> ofStep(step: PipelineStep<I, O>) = PipelineBuilder<I, O>(listOf(step as PipelineStep<Any, Any>))

    }

    @Suppress("UNCHECKED_CAST")
    private fun <O: Any> withStep0(step: PipelineStep<CO, O>): PipelineBuilder<I, O> = PipelineBuilder(
        steps.run { toMutableList() }.apply { add(step as PipelineStep<Any, Any>) }
    )

    fun <O: Any> withPipeline(pipeline: Pipeline<CO, O>) = withStep0 { input -> pipeline.process(input) }

    fun <O: Any> withStep(function: Function<CO, O>) = withStep0 { input -> function.apply(input) }

    fun <O : Any> withStep(step: PipelineStep<CO, O>) = withStep0 { input -> step.process(input) }

    fun build(): Pipeline<I, CO> = Pipeline(steps)

}