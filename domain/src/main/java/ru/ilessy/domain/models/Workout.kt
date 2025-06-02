package ru.ilessy.domain.models

import ru.ilessy.domain.enums.WorkoutType

data class Workout(
    val id: Long,
    val title: String,
    val description: String? = null,
    val workoutType: WorkoutType,
    val duration: String
)