package org.snowykte0426.exToolkit.commands

import net.minecraft.server.command.ServerCommandSource
import com.mojang.brigadier.CommandDispatcher

interface BaseCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>)
}