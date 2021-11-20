package com.neptune.jupiter.api.architecture.lifecycle.extensions.impl

import com.neptune.jupiter.api.architecture.lifecycle.LifecycleListener
import com.neptune.jupiter.api.architecture.lifecycle.getOrInsertGenericLifecycle
import com.neptune.jupiter.api.collections.onlinePlayerMapOf
import com.neptune.jupiter.api.extensions.skedule.BukkitDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.bukkit.entity.Player

internal fun com.neptune.jupiter.api.architecture.Moon.getOrInsertCoroutineLifecycle(): CoroutineLifecycle {
    return getOrInsertGenericLifecycle(Int.MIN_VALUE) {
        CoroutineLifecycle(this)
    }
}


internal class CoroutineLifecycle(
        override val plugin: com.neptune.jupiter.api.architecture.Moon
) : LifecycleListener<com.neptune.jupiter.api.architecture.Moon> {

    inner class PlayerCoroutineScope(
            val job: Job,
            val coroutineScope: CoroutineScope
    ) {
        fun cancelJobs() = job.cancel()
    }

    private val job = SupervisorJob()
    val pluginCoroutineScope = CoroutineScope(BukkitDispatchers.SYNC + job)

    private val playersCoroutineScope by lazy {
        onlinePlayerMapOf<PlayerCoroutineScope>()
    }

    override fun onPluginEnable() {}

    override fun onPluginDisable() {
        job.cancel()
        for (scopes in playersCoroutineScope.values) {
            scopes.cancelJobs()
        }
    }

    fun getPlayerCoroutineScope(player: Player): CoroutineScope {
        return playersCoroutineScope[player]?.coroutineScope
                ?: newPlayerCoroutineScope().also {
                    playersCoroutineScope.put(player, it) { playerCoroutineScope ->
                        playerCoroutineScope.cancelJobs()
                    }
                }.coroutineScope
    }

    private fun newPlayerCoroutineScope(): PlayerCoroutineScope {
        val job = SupervisorJob()
        return PlayerCoroutineScope(
                job,
                CoroutineScope(BukkitDispatchers.SYNC + job)
        )
    }
}