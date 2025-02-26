import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.minecraft.command.CommandSource.suggestMatching
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.*
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.snowykte0426.exToolkit.commands.BaseCommand
import org.snowykte0426.exToolkit.utils.ItemUtils

object GetItemCommand : BaseCommand {
    private val logger: Logger = LogManager.getLogger("Ex-Toolkit")

    private val suggestedItems = setOf(
        "diamond_sword", "netherite_sword",
        "diamond_axe", "netherite_axe",
        "bow", "crossbow",
        "mace",
        "iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots",
        "iron_pickaxe", "iron_shovel", "iron_hoe", "iron_sword",
        "iron_axe",
        "golden_helmet", "golden_chestplate", "golden_leggings", "golden_boots",
        "golden_pickaxe", "golden_shovel", "golden_hoe", "golden_sword", "golden_axe",
        "stone_sword", "stone_axe", "stone_pickaxe", "stone_shovel", "stone_hoe",
        "chainmail_helmet", "chainmail_chestplate", "chainmail_leggings", "chainmail_boots",
        "wooden_sword", "wooden_axe", "wooden_pickaxe", "wooden_shovel", "wooden_hoe",
        "leather_helmet", "leather_chestplate", "leather_leggings", "leather_boots",
        "trident",
        "diamond_helmet", "netherite_helmet",
        "diamond_chestplate", "netherite_chestplate",
        "diamond_leggings", "netherite_leggings",
        "diamond_boots", "netherite_boots",
        "diamond_pickaxe", "netherite_pickaxe",
        "diamond_shovel", "netherite_shovel",
        "diamond_hoe", "netherite_hoe",
        "fishing_rod", "shears"
    )

    private val itemSuggestionProvider = SuggestionProvider<ServerCommandSource> { context, builder ->
        suggestMatching(suggestedItems, builder)
    }

    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        listOf("extool", "et").forEach { commandName ->
            dispatcher.register(
                CommandManager.literal(commandName)
                    .then(
                        CommandManager.literal("get")
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
                                            logger.info("[Ex-Toolkit] 유저 ${player.name.string}이 유효하지 않은 일반 아이템 ${itemName}을 요청했습니다.")
                                            return@executes 0
                                        }
                                        itemStack = applyMaxEnchantments(itemStack, context.source.server)

                                        player.giveItemStack(itemStack)
                                        context.source.sendFeedback(
                                            {
                                                Text.literal("[Ex-Toolkit] ${itemName.uppercase()} 아이템을 지급했습니다!")
                                                    .formatted(Formatting.GREEN)
                                            },
                                            false
                                        )
                                        logger.info("[Ex-Toolkit] 유저 ${player.name.string}이 $itemName 아이템을 요청했습니다.")
                                        1
                                    }
                            )
                    )
            )
        }
    }


    private fun applyMaxEnchantments(itemStack: ItemStack, server: MinecraftServer): ItemStack {
        val item = itemStack.item

        val commonEnchantments = mapOf(
            Enchantments.UNBREAKING to 3,
            Enchantments.MENDING to 1
        )

        val enchantments = when (item) {
            is SwordItem -> commonEnchantments + mapOf(
                Enchantments.SHARPNESS to 5,
                Enchantments.SMITE to 5,
                Enchantments.BANE_OF_ARTHROPODS to 5,
                Enchantments.FIRE_ASPECT to 2,
                Enchantments.LOOTING to 3,
                Enchantments.SWEEPING_EDGE to 3
            )

            is AxeItem -> commonEnchantments + mapOf(
                Enchantments.SHARPNESS to 5,
                Enchantments.EFFICIENCY to 5
            )

            is BowItem -> commonEnchantments + mapOf(
                Enchantments.POWER to 5,
                Enchantments.FLAME to 1,
                Enchantments.INFINITY to 1
            )

            is CrossbowItem -> commonEnchantments + mapOf(
                Enchantments.QUICK_CHARGE to 3,
                Enchantments.PIERCING to 4,
                Enchantments.MULTISHOT to 1
            )

            is TridentItem -> commonEnchantments + mapOf(
                Enchantments.IMPALING to 5,
                Enchantments.LOYALTY to 3,
                Enchantments.CHANNELING to 1
            )

            is MaceItem ->
                commonEnchantments + mapOf(
                    Enchantments.DENSITY to 5,
                    Enchantments.BREACH to 4,
                    Enchantments.WIND_BURST to 3
                )

            is ArmorItem -> {
                val slot = when {
                    item.name.string.contains("helmet", true) || item.name.string.contains(
                        "cap",
                        true
                    ) -> "helmet"

                    item.name.string.contains("chestplate", true) || item.name.string.contains(
                        "tunic",
                        true
                    ) -> "chestplate"

                    item.name.string.contains("leggings", true) || item.name.string.contains(
                        "trousers",
                        true
                    ) -> "leggings"

                    item.name.string.contains("boots", true) -> "boots"
                    else -> null
                }

                val protectionEnchantment = Enchantments.PROTECTION to 4
                slot?.let {
                    commonEnchantments + when (it) {
                        "helmet" -> mapOf(
                            protectionEnchantment,
                            Enchantments.RESPIRATION to 3,
                            Enchantments.AQUA_AFFINITY to 1
                        )

                        "leggings" -> mapOf(
                            protectionEnchantment,
                            Enchantments.SWIFT_SNEAK to 3
                        )

                        "boots" -> mapOf(
                            protectionEnchantment,
                            Enchantments.FEATHER_FALLING to 4,
                            Enchantments.DEPTH_STRIDER to 3,
                            Enchantments.SOUL_SPEED to 3
                        )

                        "chestplate" -> mapOf(
                            protectionEnchantment
                        )

                        else -> emptyMap()
                    }
                } ?: emptyMap()
            }

            is FishingRodItem -> commonEnchantments + mapOf(
                Enchantments.LUCK_OF_THE_SEA to 3,
                Enchantments.LURE to 3
            )

            is ShovelItem, is HoeItem, is PickaxeItem -> commonEnchantments + mapOf(
                Enchantments.EFFICIENCY to 5,
                Enchantments.FORTUNE to 3
            )

            is ShearsItem -> commonEnchantments + mapOf(
                Enchantments.EFFICIENCY to 5
            )

            else -> emptyMap()
        }

        enchantments.forEach { (enchantment, level) ->
            val enchantmentEntry = server.registryManager.getOptionalEntry(enchantment).orElse(null)
            if (enchantmentEntry != null) {
                itemStack.addEnchantment(enchantmentEntry, level)
            }
        }

        return itemStack
    }
}