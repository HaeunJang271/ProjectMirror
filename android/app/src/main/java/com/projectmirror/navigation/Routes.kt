package com.projectmirror.navigation

object Routes {
    const val TITLE = "title"
    const val NARRATIVE = "{chapterId}/scene/{sceneId}"
    const val CHAPTER_ID = "chapterId"
    const val SCENE_ID = "sceneId"

    fun narrative(chapterId: String, sceneId: String) = "$chapterId/scene/$sceneId"

    @Deprecated("Use narrative(prologue, sceneId)")
    fun prologue(sceneId: String) = narrative("prologue", sceneId)
}
