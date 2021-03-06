package com.neptune.jupiter.api.dsl.command.arguments

import com.neptune.jupiter.api.dsl.command.*
import com.neptune.jupiter.api.extensions.server.onlinePlayers
import com.neptune.jupiter.api.extensions.text.color
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.*
import org.bukkit.entity.Player
import java.util.*

// PLAYER

val PLAYER_MISSING_PARAMETER = "Missing player parameter!".color(ChatColor.RED)
val PLAYER_NOT_ONLINE = "The player specified is not online.".color(ChatColor.RED)

/**
 * returns a [Player] or null if the player is not online.
 */
fun Executor<*>.playerOrNull(
        index: Int,
        argMissing: BaseComponent = PLAYER_MISSING_PARAMETER
): Player? = string(index, argMissing).let { Bukkit.getPlayerExact(it) }

fun Executor<*>.player(
        index: Int,
        argMissing: BaseComponent = PLAYER_MISSING_PARAMETER,
        notOnline: BaseComponent = PLAYER_NOT_ONLINE
): Player = playerOrNull(index, argMissing) ?: fail(notOnline)

fun TabCompleter.player(
        index: Int
): List<String> = argumentCompleteBuilder(index) { arg ->
    onlinePlayers.mapNotNull {
        if(it.name.startsWith(arg, true)) it.name else null
    }
}

// OFFLINE PLAYER

fun Executor<*>.offlinePlayer(
        index: Int,
        argMissing: BaseComponent = PLAYER_MISSING_PARAMETER
): OfflinePlayer = string(index, argMissing).let {
    runCatching { UUID.fromString(it) }.getOrNull()?.let { Bukkit.getOfflinePlayer(it) }
            ?: Bukkit.getOfflinePlayer(it)
}

// GAMEMODE

val GAMEMODE_MISSING_PARAMETER = "Missing GameMode argument.".color(ChatColor.RED)
val GAMEMODE_NOT_FOUND = "The gamemode specified not found.".color(ChatColor.RED)

/**
 * returns a [GameMode] or null if was not found.
 */
fun Executor<*>.gameModeOrNull(
        index: Int,
        argMissing: BaseComponent = GAMEMODE_MISSING_PARAMETER
): GameMode? = string(index, argMissing).run {
    toIntOrNull()?.let { GameMode.getByValue(it) } ?: runCatching { GameMode.valueOf(this.uppercase(Locale.getDefault())) }.getOrNull()
}

fun Executor<*>.gameMode(
        index: Int,
        argMissing: BaseComponent = GAMEMODE_MISSING_PARAMETER,
        notFound: BaseComponent = GAMEMODE_NOT_FOUND
): GameMode = gameModeOrNull(index, argMissing) ?: fail(notFound)

fun TabCompleter.gameMode(
        index: Int
): List<String> = argumentCompleteBuilder(index) { arg ->
    GameMode.values().mapNotNull {
        if(it.name.startsWith(arg, true)) it.name.lowercase(Locale.getDefault()) else null
    }
}