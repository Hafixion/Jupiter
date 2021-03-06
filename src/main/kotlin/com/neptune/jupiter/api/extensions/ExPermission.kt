package com.neptune.jupiter.api.extensions.permission

import org.bukkit.permissions.Permissible

fun Permissible.anyPermission(vararg permissions: String): Boolean
        = permissions.any { hasPermission(it) }

fun Permissible.allPermission(vararg permissions: String): Boolean
        = permissions.all { hasPermission(it) }

fun Permissible.hasPermissionOrStar(permission: String): Boolean
        = hasPermission(permission) || hasPermission(permission.replaceAfterLast('.', "*"))