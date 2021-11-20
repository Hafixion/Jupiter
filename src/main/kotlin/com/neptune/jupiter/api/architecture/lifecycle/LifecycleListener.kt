package com.neptune.jupiter.api.architecture.lifecycle

import com.neptune.jupiter.api.architecture.Moon
import com.neptune.jupiter.api.extensions.plugin.WithPlugin

/**
 * Class that listen to Lifecycle from a [Moon]
 */
interface LifecycleListener<T : com.neptune.jupiter.api.architecture.Moon> : PluginLifecycleListener, WithPlugin<T> {

    override fun invoke(event: LifecycleEvent) {
        when(event) {
            LifecycleEvent.LOAD -> onPluginLoad()
            LifecycleEvent.ENABLE -> onPluginEnable()
            LifecycleEvent.DISABLE -> onPluginDisable()
            LifecycleEvent.CONFIG_RELOAD -> onConfigReload()
        }
    }

    /**
     * Called when the Plugin loads (before the World)
     */
    fun onPluginLoad() {}

    /**
     * Called when the Plugin enables and is ready to register events, commands and etc...
     */
    fun onPluginEnable() {}

    /**
     * Called when the Plugin disable like: Server Stop,
     * Reload Server or Plugins such Plugman disable the plugin.
     */
    fun onPluginDisable() {}

    /**
     * Called when some part of your code calls [Moon.reloadConfig]
     */
    fun onConfigReload() {}
}