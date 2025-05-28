package ru.ilessy.fatfitbutn.fragments.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ilessy.domain.usecases.GetWorkoutsUseCase
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(private val getWorkoutsUseCase: GetWorkoutsUseCase) :
    ViewModel() {

    fun setIntent(workoutIntent: WorkoutIntent) {
        when (workoutIntent) {
            WorkoutIntent.GetWorkouts -> {
                viewModelScope.launch {
                    getWorkoutsUseCase.invoke()
                }
            }
        }
    }

}

sealed interface WorkoutIntent {
    data object GetWorkouts : WorkoutIntent
}