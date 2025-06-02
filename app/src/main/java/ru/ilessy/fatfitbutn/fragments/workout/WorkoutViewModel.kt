package ru.ilessy.fatfitbutn.fragments.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ilessy.domain.models.VideoWorkout
import ru.ilessy.domain.models.Workout
import ru.ilessy.domain.usecases.GetVideoUseCase
import ru.ilessy.domain.usecases.GetWorkoutsUseCase
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val getVideoUseCase: GetVideoUseCase
) :
    ViewModel() {

    private val _workoutsLiveData: MutableLiveData<List<Workout>> = MutableLiveData<List<Workout>>()
    val workoutsLiveData: LiveData<List<Workout>> = _workoutsLiveData

    private val _videoLiveData: MutableLiveData<VideoWorkout> = MutableLiveData<VideoWorkout>()
    val videoLiveData: LiveData<VideoWorkout> = _videoLiveData

    fun setIntent(workoutIntent: WorkoutIntent) {
        when (workoutIntent) {
            is WorkoutIntent.GetWorkouts -> {
                viewModelScope.launch {
                    _workoutsLiveData.value = getWorkoutsUseCase.invoke()
                }
            }

            is WorkoutIntent.GetVideoWorkout -> {
                viewModelScope.launch {
                    _videoLiveData.value = getVideoUseCase.invoke(id = workoutIntent.workoutId)
                }
            }
        }
    }

}

sealed interface WorkoutIntent {
    data object GetWorkouts : WorkoutIntent
    data class GetVideoWorkout(val workoutId: Long) : WorkoutIntent
}