package com.neptune.jupiter.api.utils

import com.neptune.jupiter.api.controllers.provideBungeeCordController
import com.neptune.jupiter.api.extensions.bungeecord.sendBungeeCord
import com.google.common.io.ByteStreams
import org.bukkit.entity.Player

typealias ResponseCallback = (message: ByteArray) -> Unit

class BungeeCordRequest(
        val player: Player,
        val subChannel: String,
        val request: ByteArray? = null,
        val responseCallback: ResponseCallback? = null
) {
    fun send() {
        val out = ByteStreams.newDataOutput()
        out.writeUTF(subChannel)
        if (request != null) out.write(request)

        player.sendBungeeCord(out.toByteArray())

        if (responseCallback != null) provideBungeeCordController().addToQueue(this)
    }
}