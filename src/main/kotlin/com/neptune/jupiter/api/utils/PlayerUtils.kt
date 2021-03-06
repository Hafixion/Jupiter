package com.neptune.jupiter.api.utils.player

import com.neptune.jupiter.api.controllers.providePlayerController
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

typealias ChatInputCallBack = Player.(String) -> Unit
typealias PlayerCallbackFunction<R> = Player.() -> R
typealias PlayerQuitFunction = PlayerCallbackFunction<Unit>
typealias PlayerMoveFunction = PlayerCallbackFunction<Boolean>

fun Player.chatInput(
        plugin: Plugin,
        sync: Boolean = false,
        whenQuitWithoutInput: PlayerQuitFunction = {},
        callback: ChatInputCallBack
) {
    providePlayerController().inputCallbacks.put(
            player,
            ChatInput(plugin, sync, callback, whenQuitWithoutInput)
    ) { it.playerQuit(this) }
}

// null if player disconnect
suspend fun Player.chatInput(
        plugin: Plugin
): String? = suspendCoroutine { c ->
    chatInput(plugin, true, { c.resume(null) }) { c.resume(it) }
}

fun Player.whenQuit(
        plugin: Plugin,
        callback: PlayerQuitFunction
) {
    providePlayerController().functionsQuit.put(this, PlayerCallback(plugin, callback)) {
        it.callback.invoke(player)
    }
}

fun Player.whenMove(
        plugin: Plugin,
        callback: PlayerMoveFunction
) {
    providePlayerController().functionsMove.put(this, PlayerCallback(plugin, callback))
}

class ChatInput(
        val plugin: Plugin,
        val sync: Boolean,
        val callback: ChatInputCallBack,
        val playerQuit: PlayerQuitFunction
)
class PlayerCallback<R>(
        val plugin: Plugin,
        val callback: PlayerCallbackFunction<R>
)