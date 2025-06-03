package ru.ilessy.fatfitbutn.fragments.workout.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.ilessy.domain.enums.WorkoutType
import ru.ilessy.domain.models.Workout
import ru.ilessy.fatfitbutn.databinding.WorkoutHolderViewBinding

class WorkoutAdapter(
    private val workoutsList: List<Workout>,
    private var workoutType: WorkoutType? = null,
    private var workoutFilter: String? = null
) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    private val _workoutState: MutableSharedFlow<WorkoutState> = MutableSharedFlow()
    val workoutState: SharedFlow<WorkoutState> = _workoutState

    private var workoutViewList: List<Workout> = workoutsList.filter { workout ->
        (workoutType == null || workoutType == WorkoutType.UNDEFINED || workout.workoutType == workoutType) &&
                (workoutFilter.isNullOrEmpty() ||
                        workout.title.contains(workoutFilter ?: "", ignoreCase = true))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding =
            WorkoutHolderViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutViewHolder(workoutView = binding)
    }

    override fun getItemCount(): Int {
        return workoutViewList.size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(workoutViewList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(workoutType: WorkoutType?, workoutFilter: String?) {
        if (this.workoutType != workoutType || this.workoutFilter != workoutFilter) {
            this.workoutType = workoutType
            this.workoutFilter = workoutFilter
            updateWorkoutViewList(workoutType = workoutType, workoutFilter = workoutFilter)
            notifyDataSetChanged()
        }
    }

    private fun updateWorkoutViewList(workoutType: WorkoutType?, workoutFilter: String? = null) {
        workoutViewList = workoutsList.filter { workout ->
            (workoutType == null || workoutType == WorkoutType.UNDEFINED || workout.workoutType == workoutType) &&
                    (workoutFilter.isNullOrEmpty() ||
                            workout.title.contains(workoutFilter, ignoreCase = true))
        }
    }

    inner class WorkoutViewHolder(private val workoutView: WorkoutHolderViewBinding) :
        RecyclerView.ViewHolder(workoutView.root) {

        init {
            workoutView.root.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    _workoutState.emit(WorkoutState.OpenWorkout(workoutId = workoutViewList[absoluteAdapterPosition].id))
                }
            }
        }

        fun bind(workout: Workout) {
            workoutView.workoutTitle.text = workout.title
            workout.description?.let { description ->
                if (description.isNotEmpty()) {
                    workoutView.workoutDescription.text = workout.description
                    workoutView.workoutDescription.visibility = VISIBLE
                } else {
                    workoutView.workoutDescription.visibility = GONE
                }
            } ?: run {
                workoutView.workoutDescription.visibility = GONE
            }
            workoutView.workoutType.text = workout.workoutType.toString()
            workoutView.workoutDuration.text = workout.duration
        }

    }

}

sealed interface WorkoutState {
    data class OpenWorkout(val workoutId: Long) : WorkoutState
}