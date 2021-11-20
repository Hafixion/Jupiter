package com.neptune.jupiter.api.controllers

import com.neptune.jupiter.api.Jupiter
import com.neptune.jupiter.api.extensions.command.unregister
import com.neptune.jupiter.api.extensions.event.KListener
import com.neptune.jupiter.api.extensions.event.event
import com.neptune.jupiter.api.provideKotlinBukkitAPI
import org.bukkit.command.Command
import org.bukkit.event.server.PluginDisableEvent

internal fun provideCommandController() = provideKotlinBukkitAPI().commandController

internal class CommandController(
        override val plugin: Jupiter
) : KListener<Jupiter>, KBAPIController {

    val commands = hashMapOf<String, MutableList<Command>>()

    override fun onEnable() {
        event<PluginDisableEvent> {
            commands.remove(plugin.name)?.forEach {
                it.unregister()
            }
        }
    }
}