package com.projectmirror.navigation

object Routes {
    const val TITLE = "title"
    const val NARRATIVE = "narrative"
    const val JOURNAL = "journal"
    const val PAUSE = "pause"
    const val SETTINGS = "settings"
    const val LOAD = "load"
    const val SAVE = "save"
    const val CHAPTER_ID = "chapterId"
    const val SCENE_ID = "sceneId"

    fun narrative(chapterId: String, sceneId: String) = NARRATIVE

    @Deprecated("Use narrative(prologue, sceneId)")
    fun prologue(sceneId: String) = narrative("prologue", sceneId)
}
