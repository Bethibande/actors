package com.bethibande.actors.service

import com.bethibande.actors.events.EventFlow
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.Job

abstract class AbstractShardProvider: ShardProvider {

    private enum class EventType {
        ADD,
        REMOVE,
    }

    private data class ShardEvent(
        val shard: ShardInstance,
        val type: EventType
    )

    protected val shards = mutableMapOf<String, ShardInstance>()
    private val events = EventFlow<ShardEvent>()
    private var job = AtomicReference<Job>()

    fun start(job: Job?) {
        val fJob = job ?: Job()

        val set = this.job.compareAndSet(null, fJob)
        if (!set) throw IllegalStateException("Already started.")

        events.start(fJob)
    }

    // TODO: synchronization bad
    @Synchronized
    fun stop() {
        val expected = job.get()

        val job = job.compareAndExchange(expected, null)
        if (job != expected) throw IllegalStateException("Already stopped.")

        job.cancel()
    }

    override fun onShardRemoved(fn: (ShardInstance) -> Unit) {

    }

    override fun onShardRemoved(shard: ShardInstance, fn: (ShardInstance) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun shards(): List<ShardInstance> {
        TODO("Not yet implemented")
    }
}