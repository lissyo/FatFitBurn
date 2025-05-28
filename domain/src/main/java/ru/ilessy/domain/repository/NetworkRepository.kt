package ru.ilessy.domain.repository

import ru.ilessy.domain.models.VideoWorkout
import ru.ilessy.domain.models.Workout

interface NetworkRepository {
    fun loadWorkouts(): List<Workout>
    fun loadVideoFromId(id: Long): VideoWorkout
}