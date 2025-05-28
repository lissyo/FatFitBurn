package ru.ilessy.domain.models

import ru.ilessy.domain.enums.WorkoutType

data class Workout(
    private val id: Long,
    private val title: String,
    private val description: String? = null,
    private val workoutType: WorkoutType,
    private val duration: String
)