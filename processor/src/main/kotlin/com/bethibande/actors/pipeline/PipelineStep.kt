package com.bethibande.actors.pipeline

fun interface PipelineStep<I: Any, O: Any> {

    fun process(input: I): O

}