package ru.ilessy.fatfitbutn.fragments.video.core

import android.content.Context
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.ilessy.domain.player.Media
import ru.ilessy.domain.player.Player

class ExoMediaPlayer(private val context: Context) : Player() {

    var exoPlayer: ExoPlayer? = null
        private set

    private val _playerEventsFlow: MutableSharedFlow<PlayerEvents> = MutableSharedFlow()
    val playerEventsFlow: SharedFlow<PlayerEvents> = _playerEventsFlow

    override fun initializationPlayer() {
        exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer?.addListener(object : androidx.media3.common.Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                processingPlayerError(error = error)
            }

            override fun onPlayerErrorChanged(error: PlaybackException?) {
                super.onPlayerErrorChanged(error)
                error?.let {
                    processingPlayerError(error = it)
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                CoroutineScope(Dispatchers.IO).launch {
                    _playerEventsFlow.emit(
                        PlayerEvents.PlayerPlaybackChange(
                            PlayerPlayback.from(
                                playbackState = playbackState
                            )
                        )
                    )
                }
            }

        })
    }

    private fun processingPlayerError(error: PlaybackException?) {
        if (exoPlayer?.isPlaying == false) {
            CoroutineScope(Dispatchers.IO).launch {
                _playerEventsFlow.emit(
                    PlayerEvents.PlayerErrorEventNotPlaying(
                        error = error
                    )
                )
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _playerEventsFlow.emit(
                    PlayerEvents.PlayerErrorEventPlaying(
                        error = error
                    )
                )
            }
        }
    }

    override fun releasePlayer() {
        pause()
        exoPlayer?.release()
    }

    override var media: Media? = null

    @UnstableApi
    override fun play() {
        exoPlayer?.let { exoPlayer ->
            media?.let { media ->
                if (media is ExoMedia) {
                    media.mediaSource?.let {
                        exoPlayer.setMediaSource(it)
                        exoPlayer.prepare()
                        exoPlayer.playWhenReady = true
                    }
                }
            }
        }
    }

    override fun pause() {
        if (exoPlayer?.isPlaying == true) {
            exoPlayer?.pause()
        }
    }

    override fun stop() {
        if (exoPlayer?.isPlaying == true) {
            exoPlayer?.stop()
        }
    }

}

sealed class PlayerEvents {
    data class PlayerErrorEventPlaying(val error: PlaybackException?) : PlayerEvents()
    data class PlayerErrorEventNotPlaying(val error: PlaybackException?) : PlayerEvents()
    data class PlayerPlaybackChange(val playerPlayback: PlayerPlayback) : PlayerEvents()
}

enum class PlayerPlayback(val playbackState: Int) {
    /**
     * The player is idle, meaning it holds only limited resources. The player must be {@link
     * #prepare() prepared} before it will play the media.
     */
    STATE_IDLE(1),

    /**
     * The player is not able to immediately play the media, but is doing work toward being able to do
     * so. This state typically occurs when the player needs to buffer more data before playback can
     * start.
     */
    STATE_BUFFERING(2),

    /**
     * The player is able to immediately play from its current position. The player will be playing if
     * {@link #getPlayWhenReady()} is true, and paused otherwise.
     */
    STATE_READY(3),

    /** The player has finished playing the media. */
    STATE_ENDED(4);

    companion object {
        fun from(playbackState: Int): PlayerPlayback {
            return entries.first { it.playbackState == playbackState }
        }
    }
}