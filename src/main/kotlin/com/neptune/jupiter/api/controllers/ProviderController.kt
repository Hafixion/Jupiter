package com.neptune.jupiter.api.controllers

import com.neptune.jupiter.api.Jupiter
import com.neptune.jupiter.api.extensions.event.KListener
import com.neptune.jupiter.api.extensions.event.event
import com.neptune.jupiter.api.provideKotlinBukkitAPI
import com.neptune.jupiter.api.utils.KClassComparator
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.reflect.KClass

internal fun provideProviderController() = provideKotlinBukkitAPI().providerController

internal class ProviderController(
        override val plugin: Jupiter
) : KListener<Jupiter>, KBAPIController {

    private val providerTree = TreeMap<String, TreeMap<KClass<*>, Any>>()

    fun register(plugin: Plugin, any: Any): Boolean {
        return providerTree.getOrPut(plugin.name, { TreeMap(KClassComparator) })
                .putIfAbsent(any::class, any) == null
    }

    fun unregister(plugin: Plugin, any: Any): Boolean {
        return providerTree.get(plugin.name)?.remove(any::class) == true
    }

    fun <T : Any> find(plugin: Plugin, kclass: KClass<T>): T {
        return providerTree.get(plugin.name)?.get(kclass) as T
    }

    override fun onEnable() {
        event<PluginDisableEvent> {
            providerTree.remove(plugin.name)
        }
    }
}