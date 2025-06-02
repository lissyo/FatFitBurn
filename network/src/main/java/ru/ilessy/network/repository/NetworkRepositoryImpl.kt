package ru.ilessy.network.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import ru.ilessy.domain.enums.WorkoutType
import ru.ilessy.domain.models.VideoWorkout
import ru.ilessy.domain.models.Workout
import ru.ilessy.domain.repository.NetworkRepository
import ru.ilessy.network.ApiClient
import ru.ilessy.network.di.VideoWorkoutEndPoint
import ru.ilessy.network.di.WorkoutEndpoint
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class NetworkRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    @WorkoutEndpoint private val workoutEndpoint: String,
    @VideoWorkoutEndPoint private val videoWorkoutEndPoint: String
) : NetworkRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun loadWorkouts(): List<Workout> {
        return suspendCancellableCoroutine { continuation ->
            apiClient.invoke(
                Request.Builder().url(workoutEndpoint).build(),
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        continuation.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val workouts = parseWorkouts(response.body?.string())
                            continuation.resume(workouts) {}
                        } else {
                            continuation.resumeWithException(IOException("HTTP ${response.code}"))
                        }
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun loadVideoFromId(id: Long): VideoWorkout {
        return suspendCancellableCoroutine { continuation ->
            apiClient.invoke(
                Request.Builder().url("$videoWorkoutEndPoint/$id").build(),
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        continuation.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val videoWorkout = parseVideoWorkout(response.body?.string())
                            continuation.resume(videoWorkout) {}
                        } else {
                            continuation.resumeWithException(IOException("HTTP ${response.code}"))
                        }
                    }
                }
            )
        }
    }

    private fun parseWorkouts(json: String?): List<Workout> {
        val gson = Gson()
        val listType = object : TypeToken<List<WorkoutJson>>() {}.type
        val workoutsJson = gson.fromJson<List<WorkoutJson>>(json, listType)

        return workoutsJson.map { jsonItem ->
            Workout(
                id = jsonItem.id,
                title = jsonItem.title,
                description = jsonItem.description ?: "",
                workoutType = WorkoutType.fromInt(jsonItem.type),
                duration = jsonItem.duration
            )
        }
    }

    private fun parseVideoWorkout(json: String?): VideoWorkout {
        TODO("Реализовать парсинг JSON в VideoWorkout")
    }

    private data class WorkoutJson(
        val id: Long,
        val title: String,
        val description: String?,
        val type: Int,
        val duration: String
    )
}