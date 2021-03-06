package com.neptune.jupiter.api.extensions.player

import com.neptune.jupiter.api.extensions.text.msg
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

fun PlayerInventory.clearArmor() {
    armorContents = arrayOf<ItemStack?>(null, null, null, null)
}

fun PlayerInventory.clearAll() {
    clear()
    clearArmor()
}

val Player.hasItemInHand get() = itemInHand != null && itemInHand.type != Material.AIR

fun Player.playSound(sound: Sound, volume: Float, pitch: Float) = playSound(location, sound, volume, pitch)
fun Player.playNote(instrument: Instrument, note: Note) = playNote(location, instrument, note)
fun <T> Player.playEffect(effect: Effect, data: T? = null) = playEffect(player.location, effect, data)

fun CommandSender.msg(message: List<String>) = message.forEach { msg(it) }

fun Player.resetWalkSpeed() {
    player.walkSpeed = 0.2f
}

fun Player.resetFlySpeed() {
    player.flySpeed = 0.1f
}