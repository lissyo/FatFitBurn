package ru.ilessy.domain.repository

import ru.ilessy.domain.models.VideoWorkout
import ru.ilessy.domain.models.Workout

interface NetworkRepository {
    suspend fun loadWorkouts(): ApiResult<List<Workout>>
    suspend fun loadVideoFromId(id: Long): ApiResult<VideoWorkout>
}

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()
}