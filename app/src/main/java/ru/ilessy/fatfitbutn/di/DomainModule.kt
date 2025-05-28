package ru.ilessy.fatfitbutn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.ilessy.domain.repository.NetworkRepository
import ru.ilessy.domain.usecases.GetVideoUseCase
import ru.ilessy.domain.usecases.GetWorkoutsUseCase

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetWorkoutsUseCase(networkRepository: NetworkRepository): GetWorkoutsUseCase {
        return GetWorkoutsUseCase(networkRepository = networkRepository)
    }

    @Provides
    fun provideGetVideoUseCase(networkRepository: NetworkRepository): GetVideoUseCase {
        return GetVideoUseCase(networkRepository = networkRepository)
    }

}