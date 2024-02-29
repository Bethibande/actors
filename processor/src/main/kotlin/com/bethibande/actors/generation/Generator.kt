package com.bethibande.actors.generation

import com.bethibande.actors.struct.Environment

interface Generator {

    fun generate(env: Environment)

}