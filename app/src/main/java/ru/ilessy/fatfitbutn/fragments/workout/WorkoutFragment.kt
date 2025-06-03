package ru.ilessy.fatfitbutn.fragments.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.ilessy.fatfitbutn.activities.WorkoutIntent
import ru.ilessy.fatfitbutn.activities.MainViewModel
import ru.ilessy.fatfitbutn.databinding.WorkoutFragmentBinding
import ru.ilessy.fatfitbutn.fragments.workout.adapters.WorkoutAdapter
import ru.ilessy.fatfitbutn.fragments.workout.adapters.WorkoutState

@AndroidEntryPoint
class WorkoutFragment : Fragment() {

    private var _binding: WorkoutFragmentBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private val workoutViewModel: WorkoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WorkoutFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeWorkoutsLiveData()
        observeWorkoutType()
        binding.workoutsRv.layoutManager = LinearLayoutManager(activity)
        binding.controlLayout.filter.setOnClickListener {
            workoutViewModel.changeWorkoutType()
        }
        mainViewModel.setIntent(workoutIntent = WorkoutIntent.GetWorkouts)
    }

    private fun observeWorkoutsLiveData() {
        mainViewModel.workoutsLiveData.observe(viewLifecycleOwner) { workoutsList ->
            if (workoutsList != null) {
                val workoutAdapter = WorkoutAdapter(
                    workoutsList = workoutsList,
                    workoutType = workoutViewModel.workoutType.value
                )
                lifecycleScope.launch {
                    workoutAdapter.workoutState.collect { workoutState ->
                        when (workoutState) {
                            is WorkoutState.OpenWorkout -> {
                                mainViewModel.setIntent(
                                    workoutIntent = WorkoutIntent.GetVideoWorkout(
                                        workoutId = workoutState.workoutId
                                    )
                                )
                            }
                        }
                    }
                }
                binding.workoutsRv.adapter = workoutAdapter
            }
        }
    }

    private fun observeWorkoutType() {
        workoutViewModel.workoutType.observe(viewLifecycleOwner) { workoutType ->
            if (binding.workoutsRv.adapter != null) {
                (binding.workoutsRv.adapter as WorkoutAdapter).filter(workoutType = workoutType)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}