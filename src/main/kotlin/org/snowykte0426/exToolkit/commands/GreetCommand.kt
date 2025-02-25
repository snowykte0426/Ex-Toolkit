package org.snowykte0426.exToolkit.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object GreetCommand : BaseCommand {
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        listOf("extool", "et").forEach { commandName ->
            dispatcher.register(
                CommandManager.literal(commandName)
                    .then(
                        CommandManager.literal("greet")
                        .then(
                            CommandManager.argument("name", StringArgumentType.string())
                                .executes { context ->
                                    val name = StringArgumentType.getString(context, "name")
                                    context.source.sendFeedback(
                                        { Text.literal("[Ex-Toolkit] 안녕하세요, $name!").formatted(Formatting.AQUA) },
                                        false
                                    )
                                    1
                                }
                        )
                    )
            )
        }
    }
}