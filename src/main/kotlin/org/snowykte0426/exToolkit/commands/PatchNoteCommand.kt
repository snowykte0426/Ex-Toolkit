package org.snowykte0426.exToolkit.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.snowykte0426.exToolkit.data.PatchNoteData
import org.snowykte0426.exToolkit.utils.JsonUtils

object PatchNoteCommand : BaseCommand {
    private const val LINES_PER_PAGE = 3

    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        listOf("extool", "et").forEach { commandName ->
            dispatcher.register(
                CommandManager.literal(commandName)
                    .then(
                        CommandManager.literal("patchnote")
                            .then(
                                CommandManager.argument("page", IntegerArgumentType.integer(1))
                                    .executes { context ->
                                        val page = IntegerArgumentType.getInteger(context, "page")
                                        sendPatchNotes(context.source, page)
                                        1
                                    }
                            )
                            .executes { context ->
                                sendPatchNotes(context.source, 1)
                                1
                            }
                    )
            )
        }
    }

    private fun sendPatchNotes(source: ServerCommandSource, page: Int) {
        val patchNotes: PatchNoteData = JsonUtils.loadPatchNotes() ?: run {
            source.sendFeedback({ Text.literal("[Ex-Toolkit] 패치노트 파일을 찾을 수 없습니다.").formatted(Formatting.RED) }, false)
            return
        }

        source.sendFeedback({ Text.literal("===== ${patchNotes.title} =====").formatted(Formatting.GOLD) }, false)

        val startIndex = (page - 1) * LINES_PER_PAGE
        val endIndex = minOf(startIndex + LINES_PER_PAGE, patchNotes.patchNote.size)
        for (i in startIndex until endIndex) {
            val entry = patchNotes.patchNote[i]
            source.sendFeedback({ Text.literal("버전 ${entry.version}:").formatted(Formatting.AQUA) }, false)
            entry.notes.forEach { note ->
                source.sendFeedback({ Text.literal(" - $note").formatted(Formatting.YELLOW) }, false)
            }
        }
    }
}