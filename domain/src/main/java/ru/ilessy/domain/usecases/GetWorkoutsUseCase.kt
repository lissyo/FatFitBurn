package ru.ilessy.domain.usecases

import ru.ilessy.domain.models.Workout
import ru.ilessy.domain.repository.NetworkRepository
import ru.ilessy.domain.repository.ApiResult

class GetWorkoutsUseCase(private val networkRepository: NetworkRepository) {

    suspend operator fun invoke(): ApiResult<List<Workout>> {
        return networkRepository.loadWorkouts()
    }

}