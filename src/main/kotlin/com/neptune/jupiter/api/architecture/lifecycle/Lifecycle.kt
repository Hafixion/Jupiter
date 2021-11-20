package com.neptune.jupiter.api.architecture.lifecycle

/**
 * Holds a lifecycle listener class and its priority
 */
data class Lifecycle(
        val priority: Int,
        val listener: PluginLifecycleListener
) : Comparable<Lifecycle> {

    override fun compareTo(
            other: Lifecycle
    ): Int = other.priority.compareTo(priority)
}