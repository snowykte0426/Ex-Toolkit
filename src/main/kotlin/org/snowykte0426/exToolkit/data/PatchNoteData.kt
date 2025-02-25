package org.snowykte0426.exToolkit.data

data class PatchNoteEntry(
    val version: String,
    val notes: List<String>
)

data class PatchNoteData(
    val title: String,
    val patchNote: List<PatchNoteEntry>
)