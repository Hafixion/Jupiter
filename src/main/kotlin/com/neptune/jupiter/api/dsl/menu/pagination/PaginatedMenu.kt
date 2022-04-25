package com.neptune.jupiter.api.dsl.menu.pagination

import com.neptune.jupiter.api.architecture.Moon
import com.neptune.jupiter.api.dsl.menu.menu
import com.neptune.jupiter.api.dsl.menu.slot
import com.neptune.jupiter.api.dsl.menu.slot.*
import com.neptune.jupiter.api.extensions.item.displayName
import com.neptune.jupiter.api.extensions.item.item
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

private val next_page = item(Material.ARROW).displayName("§eNext Page")
private val previous_page = item(Material.ARROW).displayName("§ePrevious Page")

class PaginatedMenu(
    val title: String,
    val plugin: Moon,
    val items: List<ItemStack>,
    val lines: Int,
    val onClick: MenuPlayerSlotInteractEvent? = null,
    val onRender: MenuPlayerSlotRenderEvent? = null,
    val onUpdate: MenuPlayerSlotUpdateEvent? = null,
    val onMoveTo: MenuPlayerSlotMoveToEvent? = null
) {
    init {
        if (lines <= 1) {
            throw IllegalArgumentException("Lines must be greater than 1")
        }
    }

    fun openPageToPlayer(page: Int, vararg player: Player) {
        menu(title, lines, plugin) {
            val menuItems = itemsForPage(page)

            if (page > 0) slot((lines - 1) * 9 + 1, previous_page).onClick {
                close()
                openPageToPlayer(page - 1, *player)
            }

            if (items.size > (page + 1) * (lines - 1) * 9 + (lines - 1) * 9) slot((lines) * 9, next_page).onClick {
                close()
                openPageToPlayer(page + 1, *player)
            }

            menuItems.forEachIndexed { index, item ->
                slot(index, item).apply {
                    if (onClick != null) onClick(onClick)
                    if (onRender != null) onRender(onRender)
                    if (onUpdate != null) onUpdate(onUpdate)
                    if (onMoveTo != null) this.onMoveToSlot(onMoveTo)
                }
            }
        }.openToPlayer(*player)
    }

    fun openToPlayer(vararg player: Player) = openPageToPlayer(0, *player)

    fun itemsForPage(page: Int): List<ItemStack> {
        val startingIndex = pageAndIndexToIndex((lines - 1) * 9, page, 0)
        val endingIndex = pageAndIndexToIndex((lines - 1) * 9, page, (lines - 1) * 9 + 1)

        return items.subList(startingIndex, endingIndex)
    }

}

fun pageAndIndexToIndex(pageSize: Int, page: Int, index: Int) = page * pageSize + index