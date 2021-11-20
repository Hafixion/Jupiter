package com.neptune.jupiter.api.controllers

import com.neptune.jupiter.api.Jupiter
import com.neptune.jupiter.api.collections.onlinePlayerMapOf
import com.neptune.jupiter.api.extensions.event.KListener
import com.neptune.jupiter.api.extensions.event.displaced
import com.neptune.jupiter.api.extensions.event.event
import com.neptune.jupiter.api.extensions.scheduler.scheduler
import com.neptune.jupiter.api.provideKotlinBukkitAPI
import com.neptune.jupiter.api.utils.player.ChatInput
import com.neptune.jupiter.api.utils.player.PlayerCallback
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.server.PluginDisableEvent

internal fun providePlayerController() = provideKotlinBukkitAPI().playerController

internal class PlayerController(
        override val plugin: Jupiter
) : KListener<Jupiter>, KBAPIController {

    internal val inputCallbacks by lazy { plugin.onlinePlayerMapOf<ChatInput>() }
    internal val functionsMove by lazy { plugin.onlinePlayerMapOf<PlayerCallback<Boolean>>() }
    internal val functionsQuit by lazy { plugin.onlinePlayerMapOf<PlayerCallback<Unit>>() }

    override fun onEnable() {
        event<AsyncPlayerChatEvent>(ignoreCancelled = true) {
            if (message.isNotBlank()) {
                val input = inputCallbacks.remove(player)
                if (input != null) {
                    if (input.sync) scheduler { input.callback(player, message) }.runTask(plugin)
                    else input.callback(player, message)
                    isCancelled = true
                }
            }
        }
        event<PlayerMoveEvent>(ignoreCancelled = true) {
            if (displaced) {
                if (functionsMove[player]?.run { callback.invoke(player) } == true) {
                    isCancelled = true
                }
            }
        }
        event<PluginDisableEvent> {
            inputCallbacks.entries.filter { it.value.plugin == plugin }.forEach {
                inputCallbacks.remove(it.key)
            }
            functionsMove.entries.filter { it.value.plugin == plugin }.forEach {
                functionsMove.remove(it.key)
            }
            functionsQuit.entries.filter { it.value.plugin == plugin }.forEach {
                functionsQuit.remove(it.key)
            }
        }
    }
}