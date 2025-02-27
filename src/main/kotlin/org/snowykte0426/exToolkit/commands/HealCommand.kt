package org.snowykte0426.exToolkit.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.apache.logging.log4j.LogManager

object HealCommand : BaseCommand {
    private val logger = LogManager.getLogger("Ex-Toolkit")

    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        listOf("extool", "et").forEach { commandName ->
            dispatcher.register(
                CommandManager.literal(commandName)
                    .then(
                        CommandManager.literal("heal")
                            .executes { context ->
                                val player = context.source.player ?: return@executes 0
                                healPlayer(player)
                                context.source.sendFeedback(
                                    { Text.literal("[Ex-Toolkit] 당신의 체력을 회복했습니다.").formatted(Formatting.AQUA) },
                                    false
                                )
                                1
                            }
                            .then(
                                CommandManager.argument("target", StringArgumentType.string())
                                    .suggests(playerNameSuggestion())
                                    .executes { context ->
                                        val playerName = StringArgumentType.getString(context, "target")
                                        val server = context.source.server
                                        val target = server.playerManager.getPlayer(playerName)

                                        if (target != null) {
                                            healPlayer(target)
                                            context.source.sendFeedback(
                                                {
                                                    Text.literal("[Ex-Toolkit] $playerName 님의 체력을 회복했습니다.")
                                                        .formatted(Formatting.AQUA)
                                                },
                                                false
                                            )
                                            1
                                        } else {
                                            context.source.sendError(Text.literal("플레이어 $playerName 를 찾을 수 없습니다."))
                                            0
                                        }
                                    }
                            )
                    )
            )
        }
    }

    private fun healPlayer(player: ServerPlayerEntity) {
        player.health = player.maxHealth
        player.hungerManager.foodLevel = 20
        player.hungerManager.saturationLevel = 5.0f

        val negativeEffects = setOf(
            StatusEffects.POISON,
            StatusEffects.WITHER,
            StatusEffects.SLOWNESS,
            StatusEffects.WEAKNESS,
            StatusEffects.BLINDNESS,
            StatusEffects.MINING_FATIGUE,
            StatusEffects.NAUSEA,
            StatusEffects.HUNGER,
            StatusEffects.UNLUCK,
            StatusEffects.BAD_OMEN,
            StatusEffects.RAID_OMEN,
            StatusEffects.TRIAL_OMEN,
            StatusEffects.DARKNESS,
            StatusEffects.WIND_CHARGED,
            StatusEffects.WEAVING,
            StatusEffects.OOZING,
            StatusEffects.INFESTED
        )

        val effectsToRemove = player.statusEffects.filter { it.effectType in negativeEffects }
        effectsToRemove.forEach { player.removeStatusEffect(it.effectType) }

        logger.info("[Ex-Toolkit] 유저 ${player.name.string}이 체력을 회복하고 부정적인 상태 효과가 제거되었습니다.")
    }

    private fun playerNameSuggestion(): SuggestionProvider<ServerCommandSource> {
        return SuggestionProvider { context, builder ->
            val server = context.source.server
            server.playerManager.playerList.forEach { player ->
                builder.suggest(player.gameProfile.name)
            }
            builder.buildFuture()
        }
    }
}