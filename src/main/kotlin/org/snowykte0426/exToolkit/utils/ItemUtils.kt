package org.snowykte0426.exToolkit.utils

import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

object ItemUtils {

    fun getItem(itemName: String): ItemStack {
        val item = Registries.ITEM.get(Identifier.of(itemName))
        return if (item != Items.AIR) {
            ItemStack(item)
        } else {
            ItemStack.EMPTY
        }
    }
}