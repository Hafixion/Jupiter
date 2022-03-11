package com.neptune.jupiter.api

import com.neptune.jupiter.api.controllers.*
import com.neptune.jupiter.api.controllers.BungeeCordController
import com.neptune.jupiter.api.controllers.CommandController
import com.neptune.jupiter.api.controllers.MenuController
import com.neptune.jupiter.api.controllers.PlayerController
import com.neptune.jupiter.api.controllers.ProviderController
import com.neptune.jupiter.api.extensions.plugin.registerEvents
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

internal fun provideKotlinBukkitAPI(): Jupiter {
    return Bukkit.getServer().pluginManager.getPlugin("JupiterApi") as Jupiter?
            ?: throw IllegalAccessException("The plugin JupiterApi is not loaded yet")
}

private const val BSTATS_PLUGIN_ID = 6356

class Jupiter : JavaPlugin() {

    internal val commandController = CommandController(this)
    internal val menuController = MenuController(this)
    internal val playerController = PlayerController(this)
    internal val providerController = ProviderController(this)
    internal val bungeeCordController = BungeeCordController(this)

    private val controllers = listOf(
            commandController, menuController, playerController,
            providerController, bungeeCordController
    )

    override fun onEnable() {
        for (controller in controllers) {
            controller.onEnable()

            if(controller is Listener)
                registerEvents(controller)
        }
    }
}