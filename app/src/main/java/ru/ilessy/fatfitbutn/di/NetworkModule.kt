package ru.ilessy.fatfitbutn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ilessy.domain.repository.NetworkRepository
import ru.ilessy.network.ApiClient
import ru.ilessy.network.di.VideoWorkoutEndPoint
import ru.ilessy.network.di.WorkoutEndpoint
import ru.ilessy.network.repository.NetworkRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkRepository(
        apiClient: ApiClient, @WorkoutEndpoint workoutEndpoint: String,
        @VideoWorkoutEndPoint videoWorkoutEndPoint: String
    ): NetworkRepository {
        return NetworkRepositoryImpl(
            apiClient = apiClient,
            workoutEndpoint = workoutEndpoint,
            videoWorkoutEndPoint = videoWorkoutEndPoint
        )
    }
}