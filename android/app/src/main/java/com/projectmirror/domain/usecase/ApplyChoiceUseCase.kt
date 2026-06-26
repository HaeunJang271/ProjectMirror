package com.projectmirror.domain.usecase

import com.projectmirror.data.repository.GameStateRepository
import com.projectmirror.domain.model.ChoiceOption
import com.projectmirror.domain.model.WorldFlags
import javax.inject.Inject

class ApplyChoiceUseCase @Inject constructor(
    private val gameStateRepository: GameStateRepository,
) {
    suspend operator fun invoke(
        choice: ChoiceOption,
        chapterId: String,
        currentSceneId: String,
    ): WorldFlags = gameStateRepository.applyChoice(
        choice = choice,
        chapterId = chapterId,
        sceneId = currentSceneId,
    )
}
