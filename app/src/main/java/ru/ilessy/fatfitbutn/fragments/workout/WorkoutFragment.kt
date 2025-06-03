package ru.ilessy.fatfitbutn.fragments.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
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
        observeWorkoutFilter()
        binding.workoutsRv.layoutManager = LinearLayoutManager(activity)
        binding.controlLayout.filter.setOnClickListener {
            workoutViewModel.changeWorkoutType()
        }
        binding.controlLayout.founder.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    workoutViewModel.changeWorkoutFilter(workoutFilter = newText)
                }
                return true
            }

        })
        mainViewModel.setIntent(workoutIntent = WorkoutIntent.GetWorkouts)
    }

    private fun observeWorkoutsLiveData() {
        mainViewModel.workoutsLiveData.observe(viewLifecycleOwner) { workoutsList ->
            if (workoutsList != null) {
                val workoutAdapter = WorkoutAdapter(
                    workoutsList = workoutsList,
                    workoutType = workoutViewModel.workoutType.value,
                    workoutFilter = workoutViewModel.workoutFilter.value
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
                (binding.workoutsRv.adapter as WorkoutAdapter).filter(
                    workoutType = workoutType,
                    workoutFilter = workoutViewModel.workoutFilter.value
                )
            }
        }
    }

    private fun observeWorkoutFilter() {
        workoutViewModel.workoutFilter.observe(viewLifecycleOwner) { workoutFilter ->
            if (binding.workoutsRv.adapter != null) {
                (binding.workoutsRv.adapter as WorkoutAdapter).filter(
                    workoutType = workoutViewModel.workoutType.value,
                    workoutFilter = workoutFilter
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}