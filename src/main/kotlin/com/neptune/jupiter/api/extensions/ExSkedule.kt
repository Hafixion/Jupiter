package com.neptune.jupiter.api.extensions.skedule

import com.neptune.jupiter.api.extensions.plugin.WithPlugin
import com.neptune.jupiter.api.utils.getTakeValuesOrNull
import com.neptune.jupiter.api.utils.registerCoroutineContextTakes
import com.neptune.jupiter.api.utils.unregisterCoroutineContextTakes
import com.okkero.skedule.BukkitDispatcher
import com.okkero.skedule.BukkitSchedulerController
import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.schedule
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

// Skedule's maven repository is currently unavailable.

fun WithPlugin<*>.schedule(
        initialContext: SynchronizationContext = SynchronizationContext.SYNC,
        co: suspend BukkitSchedulerController.() -> Unit
) = plugin.schedule(initialContext, co)

val BukkitSchedulerController.contextSync get() = SynchronizationContext.SYNC
val BukkitSchedulerController.contextAsync get() = SynchronizationContext.ASYNC

suspend fun BukkitSchedulerController.switchToSync() = switchContext(contextSync)
suspend fun BukkitSchedulerController.switchToAsync() = switchContext(contextAsync)

/**
 * Returns a PluginDispatcher to be used to provide ASYNC and SYNC Coroutines Dispatchers from Bukkit.
 * by Skedule.
 */
val WithPlugin<*>.BukkitDispatchers get() = plugin.BukkitDispatchers
val Plugin.BukkitDispatchers get() = PluginDispatcher(this as JavaPlugin)

/**
 * Returns a PluginDispatcher to be used to provide ASYNC and SYNC Coroutines Dispatchers from Bukkit.
 * by Skedule.
 *
 * Uses Bukkit's [JavaPlugin.getProvidingPlugin] to safe retrieve the actually JavaPlugin class.
 */
val WithPlugin<*>.BukkitDispatchersSafe get() = plugin.BukkitDispatchersSafe
val Plugin.BukkitDispatchersSafe get() = PluginDispatcher(JavaPlugin.getProvidingPlugin(this::class.java))

@JvmInline
value class PluginDispatcher(val plugin: JavaPlugin) {
    val ASYNC get() = BukkitDispatcher(plugin, true)
    val SYNC get() = BukkitDispatcher(plugin, false)
}

// Take max millisecond in a tick

@OptIn(ExperimentalTime::class)
suspend fun BukkitSchedulerController.takeMaxPerTick(time: Duration) {
    val takeValues = getTakeValuesOrNull(context)

    if(takeValues == null) {
        // registering take max at current millisecond
        registerCoroutineContextTakes(context, time)
    } else {
        // checking if this exceeded the max time of execution
        if(takeValues.wasTimeExceeded()) {
            unregisterCoroutineContextTakes(context)
            waitFor(1)
        }
    }
}
