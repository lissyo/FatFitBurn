package ru.ilessy.fatfitbutn.fragments.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ilessy.domain.enums.WorkoutType

class WorkoutViewModel : ViewModel() {

    private val _workoutType: MutableLiveData<WorkoutType> = MutableLiveData()
    val workoutType: LiveData<WorkoutType> = _workoutType

    private val _workoutFilter: MutableLiveData<String> = MutableLiveData()
    val workoutFilter: LiveData<String> = _workoutFilter

    fun changeWorkoutType() {
        when (_workoutType.value) {
            WorkoutType.WORKOUT -> {
                _workoutType.value = WorkoutType.ONLINE
            }

            WorkoutType.ONLINE -> {
                _workoutType.value = WorkoutType.ONLINE_WORKOUT
            }

            WorkoutType.ONLINE_WORKOUT -> {
                _workoutType.value = WorkoutType.UNDEFINED
            }

            else -> {
                _workoutType.value = WorkoutType.WORKOUT
            }
        }
    }

    fun changeWorkoutFilter(workoutFilter: String) {
        _workoutFilter.value = workoutFilter
    }

}