package org.snowykte0426.exToolkit

import GetItemCommand
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import org.apache.logging.log4j.LogManager.getLogger
import org.snowykte0426.exToolkit.commands.*
import org.snowykte0426.exToolkit.screens.StartArt

class ExToolkit : ModInitializer {
    private val logger = getLogger("Ex-Toolkit")

    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            logger.info("\u001B[31m" + "[Ex-Toolkit] Registering commands...")
            logger.info("\u001B[36m" + "[Ex-Toolkit] Version: 1.4.1")
            logger.info("\u001B[32m" + StartArt.art)
            listOf(
                ReloadCommand,
                HelpCommand,
                GreetCommand,
                PatchNoteCommand,
                GetItemCommand,
                GetSuperCommand,
                HealCommand,
                RepairCommand
            ).forEach {
                it.register(dispatcher)
            }
            logger.info("\u001B[36m" + "[Ex-Toolkit] Commands registered!")
        }
    }
}