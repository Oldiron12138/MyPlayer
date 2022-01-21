package com.example.myplayer.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.DefaultLoadControl

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

import com.google.android.exoplayer2.DefaultRenderersFactory
import android.os.Environment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.myplayer.databinding.FragmentMoviesBinding
import com.example.myplayer.databinding.FragmentPlayerBinding
import com.example.myplayer.manager.ExoPlayerManager
import com.google.android.exoplayer2.MediaItem

import com.google.android.exoplayer2.SimpleExoPlayer
import java.io.File
import android.view.MotionEvent

import android.view.View.OnTouchListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myplayer.R
import com.example.myplayer.viewmodels.InfoViewModel
import kotlinx.coroutines.Job


class PlayerFragment :Fragment(),ExoPlayerManager.OnBackKeyPress{
    private lateinit var playerBinding: FragmentPlayerBinding

    lateinit var exoPlayerManager: ExoPlayerManager
    lateinit var player: SimpleExoPlayer
    lateinit var url: String
    var mPosY: Float = 0f
    var mPosX: Float = 0f
    var mCurPosX: Float = 0f
    var mCurPosY: Float = 0f
    private var fromWhere = ""
    private var homeJob: Job? = null
    private var onBackFromCircle: OnBackFromCircle? = null
    public fun setOnBackClick(listener: OnBackFromCircle?) {
        onBackFromCircle = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        playerBinding = FragmentPlayerBinding.inflate(inflater, container, false)
        return playerBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getString("url") != null){
            url = arguments?.getString("url")!!
            fromWhere = arguments?.getString("fromWhere")!!
        }
        //
        initExoPlayer()
    }

    override fun onResume() {
        super.onResume()
        playerBinding.root.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Navigation.findNavController(requireView()).popBackStack()
                true
            } else false
        }
        playerBinding.myPlayer.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Navigation.findNavController(requireView()).popBackStack()
                true
            } else false
        }
        playerBinding.playerBack.setOnClickListener {
            if ("circle" == fromWhere) {
                onBackFromCircle?.backFromCircle()
            } else {
                Navigation.findNavController(requireView()).popBackStack()
            }
            exoPlayerManager.stopPlayer()
        }
    }

//    private fun setGestureListener() {
//        requireView().setOnTouchListener(OnTouchListener { v, event -> // TODO Auto-generated method stub
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    mPosX = event.x
//                    mPosY = event.y
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    mCurPosX = event.x
//                    mCurPosY = event.y
//                }
//                MotionEvent.ACTION_UP -> if (mCurPosY - mPosY > 0
//                    && Math.abs(mCurPosY - mPosY) > 25
//                ) {
//                    //向下滑動
//                } else if (mCurPosY - mPosY < 0
//                    && Math.abs(mCurPosY - mPosY) > 25
//                ) {
//                    //向上滑动
//                    collapse()
//                }
//            }
//            true
//        })
//    }

    private fun initExoPlayer() {
        exoPlayerManager = ExoPlayerManager()
        exoPlayerManager.create(requireContext(), playerBinding.myPlayer)
        exoPlayerManager.initializePlayer(url)
        exoPlayerManager.initBackListener(this)
    }

    interface OnBackFromCircle {
        fun backFromCircle()
    }

    override fun onBackKeyPress() {
        if ("circle" == fromWhere) {
            onBackFromCircle?.backFromCircle()
        } else {
            this.findNavController().navigate(R.id.action_player_fragment_to_movies_fragment)
        }
        exoPlayerManager.stopPlayer()
    }
//    override fun onDestroyView() {
//        if (fromWhere != "Movies")
//        requireActivity().onBackPressed()
//       // this.findNavController().navigate(R.id.action_player_fragment_to_movies_fragment)
//        // Cancels this job with an optional cancellation.
//        homeJob?.cancel()
//
//        // Release player.
//        exoPlayerManager.releasePlayer()
//
//        super.onDestroyView()
//    }

    companion object {
        const val PLAYER_TAG = "PLAYER_TAG"

    }
}