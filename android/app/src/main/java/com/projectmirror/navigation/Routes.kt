package com.projectmirror.navigation

object Routes {
    const val TITLE = "title"
    const val PROLOGUE = "prologue/{sceneId}"
    const val PROLOGUE_SCENE = "sceneId"

    fun prologue(sceneId: String) = "prologue/$sceneId"
}
