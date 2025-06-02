package ru.ilessy.fatfitbutn.fragments.video.core

import android.content.Context
import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import ru.ilessy.domain.player.Media

@UnstableApi
class ExoMedia(private val mediaUri: String, private val context: Context) : Media() {

    var mediaSource: MediaSource? = null
        private set

    init {
        buildMedia()
    }

    @UnstableApi
    override fun buildMedia() {
        val uri = Uri.parse(mediaUri)
        @C.ContentType val type: Int = Util.inferContentType(uri)
        val mediaItem = MediaItem.fromUri(uri)
        val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
        when (type) {
            C.CONTENT_TYPE_DASH -> {
                mediaSource =
                    DashMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
            }

            C.CONTENT_TYPE_SS -> {
                mediaSource =
                    SsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
            }

            C.CONTENT_TYPE_HLS -> {
                mediaSource =
                    HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
            }

            C.CONTENT_TYPE_RTSP -> {
                mediaSource =
                    RtspMediaSource.Factory().createMediaSource(mediaItem)
            }

            C.CONTENT_TYPE_OTHER -> {
                mediaSource =
                    ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
            }
        }
    }

}