package ru.ilessy.fatfitbutn.fragments.workout.adapters

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
import ru.ilessy.domain.models.Workout
import ru.ilessy.fatfitbutn.databinding.WorkoutHolderViewBinding

class WorkoutAdapter(private val workoutsList: List<Workout>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    private val _workoutState: MutableSharedFlow<WorkoutState> = MutableSharedFlow<WorkoutState>()
    val workoutState: SharedFlow<WorkoutState> = _workoutState

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding =
            WorkoutHolderViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutViewHolder(workoutView = binding)
    }

    override fun getItemCount(): Int {
        return workoutsList.size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(workoutsList[position])
    }

    inner class WorkoutViewHolder(private val workoutView: WorkoutHolderViewBinding) :
        RecyclerView.ViewHolder(workoutView.root) {

        init {
            workoutView.root.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    _workoutState.emit(WorkoutState.OpenWorkout(workoutId = workoutsList[absoluteAdapterPosition].id))
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