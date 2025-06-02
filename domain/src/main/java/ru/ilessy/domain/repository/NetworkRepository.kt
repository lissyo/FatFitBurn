package ru.ilessy.domain.repository

import ru.ilessy.domain.models.VideoWorkout
import ru.ilessy.domain.models.Workout

interface NetworkRepository {
    suspend fun loadWorkouts(): List<Workout>
    suspend fun loadVideoFromId(id: Long): VideoWorkout
}