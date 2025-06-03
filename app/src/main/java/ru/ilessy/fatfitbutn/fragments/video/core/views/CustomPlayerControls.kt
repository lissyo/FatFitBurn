package ru.ilessy.fatfitbutn.fragments.video.core.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.TimeBar
import androidx.media3.ui.TimeBar.OnScrubListener
import androidx.media3.ui.TrackSelectionDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.ilessy.domain.models.Workout
import ru.ilessy.fatfitbutn.databinding.CustomPlayerControlsBinding
import ru.ilessy.fatfitbutn.fragments.video.core.utils.TimeFormat
import ru.ilessy.fatfitbutn.fragments.video.core.views.enums.ControlEnum

private const val MEMORY_ID = "MEMORY_ID"
private const val SUPER_STATE = "SUPER_STATE"

@UnstableApi
class CustomPlayerControls @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var _binding: CustomPlayerControlsBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null
    private var updateJob: Job? = null
    private var eventJob: Job? = null

    private var playbackSpeed = 1.0f

    init {
        _binding = CustomPlayerControlsBinding.inflate(LayoutInflater.from(context), this, true)
        updateJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                updateProgress()
                delay(1000)
            }
        }
    }

    fun setPlayer(player: ExoPlayer?) {
        this.player = player
        this.player?.addListener(object : Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_BUFFERING -> {

                    }

                    Player.STATE_ENDED -> {

                    }

                    Player.STATE_IDLE -> {

                    }

                    Player.STATE_READY -> {
                        analyzingSelectionOverrides()
                    }
                }

            }
        })
        updateProgress()
        binding.exoProgress.addListener(object : OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {}

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                player?.seekTo(position)
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                player?.seekTo(position)
            }
        })

        binding.exoPlay.setOnClickListener {
            player?.play()
            binding.exoPlay.visibility = GONE
            binding.exoPause.visibility = VISIBLE
            binding.exoPause.requestFocus()
        }

        binding.exoPause.setOnClickListener {
            player?.pause()
            binding.exoPause.visibility = GONE
            binding.exoPlay.visibility = VISIBLE
            binding.exoPlay.requestFocus()
        }

        binding.workoutSpeedBtn.setOnClickListener {
            playbackSpeed += 0.25f
            if (playbackSpeed > 2.0f) {
                playbackSpeed = 0.25f
            }
            player?.setPlaybackSpeed(playbackSpeed)
        }

        binding.workoutQualityBtn.setOnClickListener {
            showSelectionDialog(type = C.TRACK_TYPE_VIDEO, title = "Select Quality")
        }
    }

    private fun analyzingSelectionOverrides() {
        player?.let { player ->
            val tracks = player.currentTracks
            val trackGroupsVideo = tracks.groups.filter { it.type == C.TRACK_TYPE_VIDEO }
            analyzeSelectionInGroup(
                trackGroups = trackGroupsVideo,
                targetView = binding.workoutQualityBtn
            )
        }
    }

    private fun analyzeSelectionInGroup(trackGroups: List<Tracks.Group>, targetView: View) {
        if (trackGroups.isNotEmpty()) {
            targetView.visibility = VISIBLE
        } else {
            targetView.visibility = INVISIBLE
        }
    }

    private fun showSelectionDialog(type: Int, title: String) {
        player?.let { player ->
            val tracks = player.currentTracks
            val trackGroups = tracks.groups.filter { it.type == type }
            val currentOverrides = mutableMapOf<TrackGroup, TrackSelectionOverride>()
            for (group in trackGroups) {
                val trackGroup = group.mediaTrackGroup
                val override = player.trackSelectionParameters.overrides[trackGroup]
                if (override != null) {
                    currentOverrides[trackGroup] = override
                }
            }
            TrackSelectionDialogBuilder(
                context,
                title,
                trackGroups
            ) { _, overrides ->
                run {
                    applyTrackSelectionOverrides(overrides = overrides, type = type)
                }
            }.apply {
                setOverrides(currentOverrides)
            }.build().show()
        }
    }

    private fun applyTrackSelectionOverrides(
        overrides: Map<TrackGroup, TrackSelectionOverride>,
        type: Int
    ) {
        player?.let { player ->
            val trackSelector = player.trackSelector as? DefaultTrackSelector ?: return
            val parameters = trackSelector.buildUponParameters().apply {
                clearOverridesOfType(type)
            }
            for ((_, override) in overrides) {
                parameters.addOverride(override)
            }
            trackSelector.parameters = parameters.build()
        }
    }

    fun setWorkoutData(workout: Workout) {
        binding.workoutName.text = workout.title
        binding.workoutDescription.text = workout.description
    }

    private fun updateProgress() {
        val player = player ?: return
        val duration = player.duration
        val position = player.currentPosition

        binding.exoProgress.setDuration(duration)
        binding.exoProgress.setPosition(position)

        binding.exoPosition.text = TimeFormat.formatTime(position)
        binding.exoDuration.text = TimeFormat.formatTime(duration)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        updateJob?.cancel()
        eventJob?.cancel()
        _binding = null
    }
}