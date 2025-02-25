package org.snowykte0426.exToolkit.utils

import com.google.gson.Gson
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.snowykte0426.exToolkit.data.PatchNoteData
import org.snowykte0426.exToolkit.data.PatchNoteEntry
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

object JsonUtils {
    private val logger: Logger = LogManager.getLogger("Ex-Toolkit")

    private val gson = Gson()
    private val CONFIG_DIR = Path.of("config/ex-toolkit")
    private val PATCH_NOTES_PATH = CONFIG_DIR.resolve("patchnote.json")

    fun loadPatchNotes(): PatchNoteData {
        if (!Files.exists(PATCH_NOTES_PATH)) {
            createDefaultPatchNote()
        }
        return gson.fromJson(Files.readString(PATCH_NOTES_PATH), PatchNoteData::class.java)
    }

    fun createDefaultPatchNote() {
        val defaultPatchNote = PatchNoteData(
            title = "Ex-Toolkit Patch Note",
            patchNote = listOf(
                PatchNoteEntry(
                    version = "1.0.0",
                    notes = listOf(
                        "새로운 명령어 추가: /extool patchnote",
                        "/et 별칭 추가로 명령어 사용 편리",
                        "설정 리로드 기능 개선"
                    )
                ),
                PatchNoteEntry(
                    version = "1.1.0",
                    notes = listOf(
                        "패치노트 페이지 넘기기 기능 추가",
                        "기타 버그 수정 및 안정성 향상"
                    )
                )
            )
        )

        try {
            Files.createDirectories(CONFIG_DIR)
            Files.writeString(
                PATCH_NOTES_PATH,
                gson.toJson(defaultPatchNote),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
            )
            logger.info("[Ex-Toolkit] 패치노트 파일 생성 완료")
        } catch (e: Exception) {
            logger.error("[Ex-Toolkit] 패치노트 파일 생성 실패", e)
        }
    }
}