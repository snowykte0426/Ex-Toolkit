package org.snowykte0426.exToolkit

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import org.snowykte0426.exToolkit.commands.*

class ExToolkit : ModInitializer {
    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            listOf(ReloadCommand, HelpCommand, GreetCommand, PatchNoteCommand).forEach {
                it.register(dispatcher)
            }
        }
    }
}