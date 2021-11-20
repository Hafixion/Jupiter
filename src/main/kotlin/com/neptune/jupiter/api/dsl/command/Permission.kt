package com.neptune.jupiter.api.dsl.command

import com.neptune.jupiter.api.extensions.permission.allPermission
import com.neptune.jupiter.api.extensions.permission.anyPermission
import com.neptune.jupiter.api.extensions.permission.hasPermissionOrStar

inline fun <reified T> Executor<*>.permission(
        permission: String, builder: () -> T
): T = permission({ sender.hasPermission(permission) }, builder)

inline fun <reified T> Executor<*>.permissionOrStar(
        permission: String, builder: () -> T
): T = permission({ sender.hasPermissionOrStar(permission) }, builder)

inline fun <reified T> Executor<*>.anyPermission(
        vararg permissions: String, builder: () -> T
): T = permission({ sender.anyPermission(*permissions) }, builder)

inline fun <reified T> Executor<*>.allPermission(
        vararg permissions: String, builder: () -> T
): T = permission({ sender.allPermission(*permissions) }, builder)

inline fun <reified T> Executor<*>.permission(
        permissionChecker: () -> Boolean,
        builder: () -> T
): T = if(permissionChecker()) builder() else fail(command.permissionMessage)