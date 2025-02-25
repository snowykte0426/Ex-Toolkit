package org.snowykte0426.exToolkit.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object ReloadCommand : BaseCommand {
    private val logger: Logger = LogManager.getLogger("Ex-Toolkit")

    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        listOf("extool", "et").forEach { commandName ->
            dispatcher.register(
                CommandManager.literal(commandName)
                    .then(CommandManager.literal("reload").executes { context ->
                        logger.warn("[Ex-Toolkit] 설정이 Reload 되었습니다.")
                        context.source.sendFeedback(
                            { Text.literal("[Ex-Toolkit] 설정이 리로드되었습니다!").formatted(Formatting.GREEN) }, false
                        )
                        1
                    })
            )
        }
    }
}