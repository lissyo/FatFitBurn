package ru.ilessy.fatfitbutn.fragments.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ilessy.fatfitbutn.activities.MainViewModel
import ru.ilessy.fatfitbutn.databinding.VideoFragmentBinding

@AndroidEntryPoint
class VideoWorkoutFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: VideoFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VideoFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeVideoLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeVideoLiveData() {
        mainViewModel.videoLiveData.observe(viewLifecycleOwner) { videoWorkout ->

        }
    }
}