package org.snowykte0426.exToolkit.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.minecraft.command.CommandSource.suggestMatching
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.snowykte0426.exToolkit.utils.ItemUtils

object GetSuperCommand : BaseCommand {
    private val logger: Logger = LogManager.getLogger("Ex-Toolkit")

    private val suggestedItems = Registries.ITEM.ids.map { it.path }.toSet()

    private val itemSuggestionProvider = SuggestionProvider<ServerCommandSource> { context, builder ->
        suggestMatching(suggestedItems, builder)
    }

    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        listOf("extool", "et").forEach { commandName ->
            dispatcher.register(
                CommandManager.literal(commandName)
                    .then(
                        CommandManager.literal("get-super")
                            .then(
                                CommandManager.argument("item", StringArgumentType.string())
                                    .suggests(itemSuggestionProvider)
                                    .executes { context ->
                                        val itemName = StringArgumentType.getString(context, "item").lowercase()
                                        val player = context.source.player ?: return@executes 0

                                        var itemStack = ItemUtils.getItem(itemName)

                                        if (itemStack.isEmpty) {
                                            context.source.sendFeedback(
                                                {
                                                    Text.literal("[Ex-Toolkit] 유효하지 않은 아이템입니다!")
                                                        .formatted(Formatting.RED)
                                                },
                                                false
                                            )
                                            logger.info("[Ex-Toolkit] 유저 ${player.name.string}이 유효하지 않은 슈퍼 아이템 ${itemName}을 요청했습니다.")
                                            return@executes 0
                                        }
                                        itemStack = applySuperEnchantments(itemStack, context.source.server)

                                        player.giveItemStack(itemStack)
                                        context.source.sendFeedback(
                                            {
                                                Text.literal("[Ex-Toolkit] ${itemName.uppercase()} 슈퍼 아이템을 지급했습니다!")
                                                    .formatted(Formatting.LIGHT_PURPLE)
                                            },
                                            false
                                        )
                                        logger.info("[Ex-Toolkit] 유저 ${player.name.string}이 슈퍼 아이템 ${itemName}을 요청했습니다.")
                                        1
                                    }
                            )
                    )
            )
        }
    }


    private fun applySuperEnchantments(itemStack: ItemStack, server: MinecraftServer): ItemStack {
        val commonEnchantments = mapOf(
            Enchantments.UNBREAKING to 3,
            Enchantments.MENDING to 1
        )

        val enchantments = commonEnchantments.toMutableMap()

        enchantments.put(Enchantments.SHARPNESS, 255)
        enchantments.put(Enchantments.FIRE_ASPECT, 255)

        enchantments.forEach { (enchantment, level) ->
            val enchantmentEntry = server.registryManager.getOptionalEntry(enchantment).orElse(null)
            if (enchantmentEntry != null) {
                itemStack.addEnchantment(enchantmentEntry, level)
            }
        }

        return itemStack
    }
}