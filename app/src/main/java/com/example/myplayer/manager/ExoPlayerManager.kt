package com.example.myplayer.manager

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.findNavController
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.widget.ExitDialog
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.R
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util
import com.google.common.collect.ImmutableList
import org.json.JSONObject
import java.net.URLEncoder

class ExoPlayerManager {
    var playerView: StyledPlayerView? = null
    private var progressBar: DefaultTimeBar? = null
    private var mCuePoints: Array<Float?>? = null
    private var adPlayMemory: BooleanArray? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var mContext: Context? = null
    private var videoUrl: String?= null
    private var exoSkipBackBut: ImageButton? = null
    private var exoResumeBut: ImageButton? = null
    private var backButton: ImageView? = null
    private var listener: OnBackKeyPress? = null
    fun create(context: Context, playerView: StyledPlayerView) {
        // Adaptive track selection.
        trackSelector = DefaultTrackSelector(context)
        trackSelector?.buildUponParameters()?.setMaxVideoSizeSd()?.let {
            trackSelector?.setParameters(
                it
            )
        }
        trackSelector?.parameters =
            DefaultTrackSelector.ParametersBuilder().setRendererDisabled(C.TRACK_TYPE_VIDEO, true).build()
        mContext = context

        // Create an ExoPlayer.
        exoplayer = SimpleExoPlayer.Builder(context)
            .setTrackSelector(trackSelector!!)
            .build()

        //
        exoplayer.addAnalyticsListener(EventLogger(trackSelector))
        exoplayer.setAudioAttributes(AudioAttributes.DEFAULT, true)

        //
        exoplayer.playWhenReady = true

        //
        playerView.player = exoplayer

        progressBar = playerView.findViewById(R.id.exo_progress)
        exoSkipBackBut = playerView.findViewById(R.id.exo_pause)
        exoResumeBut = playerView.findViewById(R.id.exo_play)
        this.playerView = playerView
    }

    fun initBackListener(mListener: OnBackKeyPress) {
        listener = mListener
    }

    fun initializePlayer(
        url: String
    ) {
        // Create a media item.
        val mediaItem = createMediaItem(url)

        if (exoplayer.isPlaying) {
            exoplayer.stop()
        }

        with(exoplayer) {
            setMediaItem(mediaItem)
            prepare()
            play()
            exoSkipBackBut?.visibility = View.VISIBLE
        }
        exoSkipBackBut?.setOnClickListener {
            setPlayAndPause(false)
            exoSkipBackBut?.visibility = View.GONE
            exoResumeBut?.visibility = View.VISIBLE
        }
        exoResumeBut?.setOnClickListener {
            setPlayAndPause(true)
            exoResumeBut?.visibility = View.GONE
            exoSkipBackBut?.visibility = View.VISIBLE
        }
        backButton?.setOnClickListener {
            listener?.onBackKeyPress()

        }
    }
    fun setPlayAndPause(isPlay: Boolean) {
        exoplayer.playWhenReady = isPlay
    }

    fun getPlayAndPause(): Boolean {
        return exoplayer.playWhenReady
    }
    private fun createMediaItem(url: String): MediaItem {
        val uri = Uri.parse(url)
        val jsonObject = JSONObject()
        val licenseUri = "https://widevine-dash.ezdrm.com/proxy?pX=5FE38E&CustomData=" +
                URLEncoder.encode(jsonObject.toString(), "UTF-8")

        val builder = MediaItem.Builder()
        with(builder) {
            setUri(uri)
            setDrmMultiSession(false)
            setDrmForceDefaultLicenseUri(false)
            setDrmLicenseRequestHeaders(null)
            setDrmSessionForClearTypes(ImmutableList.of(C.TRACK_TYPE_VIDEO, C.TRACK_TYPE_AUDIO))
        }

        return builder.build()
    }

    interface PlayingChangedListener {
        fun contentPosition(contentPosition: Long)
    }


    fun stopPlayer() {
        if (exoplayer.isPlaying) {
            exoplayer.stop()
        }
    }

    fun releasePlayer() {
        exoplayer.release()
    }

    interface OnBackKeyPress {
        fun onBackKeyPress()
    }


    companion object {
        private lateinit var exoplayer: SimpleExoPlayer
        private var playingChangedListener: PlayingChangedListener? = null
    }
}