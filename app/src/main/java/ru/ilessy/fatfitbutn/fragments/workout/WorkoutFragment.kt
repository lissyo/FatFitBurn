package ru.ilessy.fatfitbutn.fragments.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ilessy.fatfitbutn.databinding.WorkoutFragmentBinding

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
        workoutViewModel.setIntent(WorkoutIntent.GetWorkouts)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}