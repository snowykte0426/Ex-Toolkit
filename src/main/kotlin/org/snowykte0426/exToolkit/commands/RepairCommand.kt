package org.snowykte0426.exToolkit.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.item.ItemStack
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.apache.logging.log4j.LogManager.getLogger

object RepairCommand : BaseCommand {
    private val logger = getLogger("Ex-Toolkit")

    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        listOf("extool", "et").forEach { commandName ->
            dispatcher.register(
                CommandManager.literal(commandName)
                    .then(
                        CommandManager.literal("repair")
                            .executes { context ->
                                val player = context.source.player ?: return@executes 0
                                val result = repairItem(player)
                                if (result) {
                                    context.source.sendFeedback(
                                        { Text.literal("[Ex-Toolkit] 아이템이 수리되었습니다.").formatted(Formatting.GREEN) },
                                        false
                                    )
                                    1
                                } else {
                                    context.source.sendError(Text.literal("수리할 아이템이 없습니다."))
                                    0
                                }
                            }
                    )
            )
        }
    }

    private fun repairItem(player: ServerPlayerEntity): Boolean {
        val itemStack: ItemStack = player.mainHandStack
        return if (!itemStack.isEmpty && itemStack.isDamageable) {
            itemStack.damage = 0
            logger.info("[Ex-Toolkit] 유저 ${player.name.string}이 아이템 ${itemStack.item}을 수리했습니다.")
            true
        } else {
            false
        }
    }
}