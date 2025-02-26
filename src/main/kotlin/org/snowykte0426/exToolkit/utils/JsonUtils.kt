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
                ),
                PatchNoteEntry(
                    version = "1.1.1",
                    notes = listOf(
                        "/extool reload 명령 실행 시 실제로 리로드 되지 않던 버그 수정"
                    )
                ),
                PatchNoteEntry(
                    version = "1.2.0",
                    notes = listOf(
                        "/extool get 명령어 추가"
                    )
                ),
                PatchNoteEntry(
                    version = "1.2.1",
                    notes = listOf(
                        "아이템 지급 시 최대 레벨 인첸트 적용"
                    )
                ),
                PatchNoteEntry(
                    version = "1.2.2",
                    notes = listOf(
                        "서버 실행 시 모드 초기화 로깅 추가",
                        "/extool get 명령어 아이템 추천 기능 추가"
                    )
                ),
                PatchNoteEntry(
                    version = "1.2.3",
                    notes = listOf(
                        "추천 아이템 코드 오류 수정",
                        "삼지창 마법부여 충돌 수정"
                    )
                ),
                PatchNoteEntry(
                    version = "1.2.4",
                    notes = listOf(
                        "거북 등딱지 추천 아이템 목록에서 제거"
                    )
                ),
                PatchNoteEntry(
                    version = "1.2.5",
                    notes = listOf(
                        "/extool reload 명령어 실행 시 패치노트 파일 갱신 기능 추가",
                        "모드 실행 시 아스키 아트 출력 기능 추가"
                    )
                ),
                PatchNoteEntry(
                    version = "1.2.6",
                    notes = listOf(
                        "/extool help 추가 설명 등록"
                    )
                ),
                PatchNoteEntry(
                    version = "1.2.7",
                    notes = listOf(
                        "/extool get에서 철퇴 및 갑옷 류 누락 문제 수정",
                        "추천 아이템 목록 업데이트"
                    )
                ),
                PatchNoteEntry(
                    version = "1.2.8",
                    notes = listOf(
                        "일부 아이템 마법부여 추가"
                    )
                ),
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