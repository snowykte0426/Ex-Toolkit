package org.snowykte0426.exToolkit.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object HelpCommand : BaseCommand {
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        listOf("extool", "et").forEach { commandName ->
            dispatcher.register(
                CommandManager.literal(commandName)
                    .then(CommandManager.literal("help").executes { context ->
                        context.source.sendFeedback(
                            { Text.literal("[Ex-Toolkit] 명령어 도움말:").formatted(Formatting.YELLOW) }, false
                        )
                        context.source.sendFeedback(
                            { Text.literal("   /$commandName reload - 플러그인 설정을 리로드합니다.").formatted(Formatting.GOLD) },
                            false
                        )
                        context.source.sendFeedback(
                            { Text.literal("   /$commandName patchnote - 패치노트를 표시합니다.").formatted(Formatting.GOLD) },
                            false
                        )
                        context.source.sendFeedback(
                            { Text.literal("   /$commandName help - 도움말을 표시합니다.").formatted(Formatting.GOLD) }, false
                        )
                        1
                    })
            )
        }
    }
}