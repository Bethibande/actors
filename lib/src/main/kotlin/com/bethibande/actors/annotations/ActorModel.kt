package com.bethibande.actors.annotations

import com.bethibande.actors.STR_NULL

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ActorModel(val name: String = STR_NULL)
