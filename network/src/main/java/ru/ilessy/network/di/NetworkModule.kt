package ru.ilessy.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ilessy.network.ApiClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkoutEndpoint
annotation class VideoWorkoutEndPoint

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @WorkoutEndpoint
    fun provideWorkoutEndpoint(): String = "ref.test.kolsa.ru/get_workouts"

    @Provides
    @VideoWorkoutEndPoint
    fun provideVideoWorkoutEndpoint(): String = "ref.test.kolsa.ru/get_video"

    @Provides
    @Singleton
    fun provideApiClient(): ApiClient {
        return ApiClient()
    }

}