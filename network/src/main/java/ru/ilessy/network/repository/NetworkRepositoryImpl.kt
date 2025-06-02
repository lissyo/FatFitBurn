package ru.ilessy.network.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import ru.ilessy.domain.enums.WorkoutType
import ru.ilessy.domain.models.VideoWorkout
import ru.ilessy.domain.models.Workout
import ru.ilessy.domain.repository.NetworkRepository
import ru.ilessy.domain.repository.ApiResult
import ru.ilessy.network.ApiClient
import ru.ilessy.network.di.VideoWorkoutEndPoint
import ru.ilessy.network.di.WorkoutEndpoint
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.Continuation

class NetworkRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    @WorkoutEndpoint private val workoutEndpoint: String,
    @VideoWorkoutEndPoint private val videoWorkoutEndPoint: String
) : NetworkRepository {

    override suspend fun loadWorkouts(): ApiResult<List<Workout>> {
        return makeApiRequest(workoutEndpoint) { parseWorkouts(it) }
    }

    override suspend fun loadVideoFromId(id: Long): ApiResult<VideoWorkout> {
        return makeApiRequest("$videoWorkoutEndPoint?id=$id") { parseVideoWorkout(it) }
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
        val gson = Gson()
        val videoType = object : TypeToken<VideoWorkout>() {}.type
        return gson.fromJson(json, videoType)
    }

    private data class WorkoutJson(
        val id: Long,
        val title: String,
        val description: String?,
        val type: Int,
        val duration: String
    )

    private suspend inline fun <reified T> makeApiRequest(
        url: String,
        crossinline parser: (String) -> T
    ): ApiResult<T> {
        return suspendCancellableCoroutine { continuation ->
            val call = apiClient.invoke(
                Request.Builder().url(url).build(),
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        continuation.completeWithError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.handleResponse(continuation, parser)
                    }
                }
            )

            continuation.invokeOnCancellation {
                if (!call.isCanceled()) {
                    call.cancel()
                }
            }
        }
    }

    private fun <T> Continuation<ApiResult<T>>.completeWithError(throwable: Throwable) {
        resumeWith(Result.success(ApiResult.Error(throwable)))
    }

    private inline fun <reified T> Response.handleResponse(
        continuation: Continuation<ApiResult<T>>,
        crossinline parser: (String) -> T
    ) {
        try {
            if (!isSuccessful) {
                continuation.completeWithError(IOException("HTTP $code"))
                return
            }

            val body = body?.string()
            if (body == null) {
                continuation.completeWithError(IOException("Empty response body"))
                return
            }

            val result = parser(body)
            continuation.resumeWith(Result.success(ApiResult.Success(result)))
        } catch (e: Exception) {
            continuation.completeWithError(e)
        }
    }
}