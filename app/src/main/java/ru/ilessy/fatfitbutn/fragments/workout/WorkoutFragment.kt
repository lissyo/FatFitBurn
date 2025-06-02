package ru.ilessy.fatfitbutn.fragments.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.ilessy.fatfitbutn.databinding.WorkoutFragmentBinding
import ru.ilessy.fatfitbutn.fragments.workout.adapters.WorkoutAdapter

@AndroidEntryPoint
class WorkoutFragment : Fragment() {

    private var _binding: WorkoutFragmentBinding? = null
    private val binding get() = _binding!!

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
        binding.workoutsRv.layoutManager = LinearLayoutManager(activity)
        workoutViewModel.setIntent(WorkoutIntent.GetWorkouts)
    }

    private fun observeWorkoutsLiveData() {
        workoutViewModel.workoutsLiveData.observe(viewLifecycleOwner) { workoutsList ->
            if (workoutsList != null) {
                val workoutAdapter = WorkoutAdapter(workoutsList)
                binding.workoutsRv.adapter = workoutAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}