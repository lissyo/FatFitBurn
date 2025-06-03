package ru.ilessy.fatfitbutn.fragments.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media3.common.util.UnstableApi
import dagger.hilt.android.AndroidEntryPoint
import ru.ilessy.fatfitbutn.activities.MainViewModel
import ru.ilessy.fatfitbutn.databinding.VideoFragmentBinding
import ru.ilessy.fatfitbutn.fragments.video.core.ExoMedia
import ru.ilessy.fatfitbutn.fragments.video.core.ExoMediaPlayer

@OptIn(UnstableApi::class)
@AndroidEntryPoint
class VideoWorkoutFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: VideoFragmentBinding? = null
    private val binding get() = _binding!!

    private var exoMediaPlayer: ExoMediaPlayer? = null

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
        initializationPlayer()
        observeVideoLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializationPlayer() {
        exoMediaPlayer = context?.let {
            ExoMediaPlayer(it)
        }
        binding.playerView.setOnClickListener {
            if (binding.customControls.isGone) {
                binding.customControls.visibility = VISIBLE
            } else {
                binding.customControls.visibility = GONE
            }
        }
    }

    private fun observeVideoLiveData() {
        mainViewModel.videoLiveData.observe(viewLifecycleOwner) { videoWorkout ->
            context?.let { context ->
                exoMediaPlayer?.media = ExoMedia(
                    mediaUri = "https://ref.test.kolsa.ru/${videoWorkout.link}",
                    context = context
                )
                mainViewModel.workoutsLiveData.value?.find { it.id == videoWorkout.id }
                    ?.let { binding.customControls.setWorkoutData(it) }
                playPlayer()
            }
        }
    }

    private fun playPlayer() {
        exoMediaPlayer?.play()
    }

    override fun onStart() {
        super.onStart()
        exoMediaPlayer?.initializationPlayer()
        binding.playerView.player = this.exoMediaPlayer?.exoPlayer
        binding.customControls.setPlayer(player = exoMediaPlayer?.exoPlayer)
    }

    override fun onResume() {
        super.onResume()
        playPlayer()
    }

    override fun onPause() {
        super.onPause()
        exoMediaPlayer?.exoPlayer?.playWhenReady = false
        exoMediaPlayer?.pause()
    }

    override fun onStop() {
        super.onStop()
        exoMediaPlayer?.releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoMediaPlayer = null
    }
}