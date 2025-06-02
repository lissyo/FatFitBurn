package ru.ilessy.domain.usecases

import ru.ilessy.domain.models.Workout
import ru.ilessy.domain.repository.NetworkRepository

class GetWorkoutsUseCase(private val networkRepository: NetworkRepository) {

    suspend operator fun invoke(): List<Workout> {
        return networkRepository.loadWorkouts()
    }

}