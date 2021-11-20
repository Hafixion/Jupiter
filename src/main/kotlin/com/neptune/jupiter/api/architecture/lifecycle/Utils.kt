package com.neptune.jupiter.api.architecture.lifecycle

inline fun <reified T : PluginLifecycleListener> com.neptune.jupiter.api.architecture.Moon.getOrInsertGenericLifecycle(
        priority: Int,
        factory: () -> T
): T {
    return lifecycleListeners
            .map { it.listener }
            .filterIsInstance<T>()
            .firstOrNull()
            ?: factory().also { registerKotlinPluginLifecycle(priority, it) }
}