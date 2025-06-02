package ru.ilessy.fatfitbutn.fragments.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ilessy.domain.models.Workout
import ru.ilessy.domain.usecases.GetWorkoutsUseCase
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(private val getWorkoutsUseCase: GetWorkoutsUseCase) :
    ViewModel() {

    private val _workoutsLiveData: MutableLiveData<List<Workout>> = MutableLiveData<List<Workout>>()
    val workoutsLiveData: LiveData<List<Workout>> = _workoutsLiveData

    fun setIntent(workoutIntent: WorkoutIntent) {
        when (workoutIntent) {
            WorkoutIntent.GetWorkouts -> {
                viewModelScope.launch {
                    _workoutsLiveData.value = getWorkoutsUseCase.invoke()
                }
            }
        }
    }

}

sealed interface WorkoutIntent {
    data object GetWorkouts : WorkoutIntent
}