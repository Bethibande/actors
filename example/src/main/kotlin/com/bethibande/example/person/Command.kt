package com.bethibande.example.person

import kotlinx.coroutines.CompletableDeferred

sealed interface Command {

    data class CommandSetName(val name: String): Command
    data class CommandSetAge(val age: Int): Command

    data class CommandGetName(val deferred: CompletableDeferred<String?>): Command
    data class CommandGetAge(val deferred: CompletableDeferred<Int?>): Command

}