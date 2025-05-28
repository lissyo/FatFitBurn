package ru.ilessy.domain.usecases

import ru.ilessy.domain.models.VideoWorkout
import ru.ilessy.domain.repository.NetworkRepository

class GetVideoUseCase(private val networkRepository: NetworkRepository) {

    suspend operator fun invoke(id: Long): VideoWorkout {
        return networkRepository.loadVideoFromId(id = id)
    }

}