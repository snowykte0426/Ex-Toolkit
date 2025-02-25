package org.snowykte0426.exToolkit.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.snowykte0426.exToolkit.utils.JsonUtils

object ReloadCommand : BaseCommand {
        private val logger: Logger = LogManager.getLogger("Ex-Toolkit")

    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        listOf("extool", "et").forEach { commandName ->
            dispatcher.register(
                CommandManager.literal(commandName)
                    .then(CommandManager.literal("reload").executes { context ->
                        reloadConfig(context.source)
                        1
                    })
            )
        }
    }

    private fun reloadConfig(source: ServerCommandSource) {
        try {
            JsonUtils.loadPatchNotes()
            logger.info("[Ex-Toolkit] 설정이 성공적으로 리로드되었습니다.")
            source.sendFeedback({ Text.literal("[Ex-Toolkit] 설정이 리로드되었습니다!").formatted(Formatting.GREEN) }, false)
        } catch (e: Exception) {
            logger.error("[Ex-Toolkit] 설정 리로드 중 오류 발생: ${e.message}")
            source.sendFeedback({ Text.literal("[Ex-Toolkit] 설정 리로드 중 오류가 발생했습니다.").formatted(Formatting.RED) }, false)
        }
    }
}