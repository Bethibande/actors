package com.bethibande.actors

interface ClosableActor<M>: AutoCloseable, Actor<M>