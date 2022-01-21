package com.example.myplayer.widget


import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.myplayer.R
import com.example.myplayer.data.db.MoviesEntity
import android.os.Build




class ShareDialog(private val activity: FragmentActivity) :
    AlertDialog(activity, R.style.Theme_AppCompat_Dialog) {

    private lateinit var cancelText: TextView
    private lateinit var photoText: TextView
    private lateinit var videoText: TextView
    private var content: String = ""
    private var listener: OnDialogButtonClickListener? = null

    private var videoListener: OnVideoClick? = null
    private var photoListener: OnPhotoClick? = null

    private var currentPosition = 0

    constructor(activity: FragmentActivity, string: String) : this(activity) {
        content = string
    }

    constructor(activity: FragmentActivity, mListener: OnDialogButtonClickListener) : this(activity) {
        this.listener = mListener
    }

    constructor(activity: FragmentActivity, mVideoListener: OnVideoClick, mPhotoListener: OnPhotoClick) : this(activity) {
        this.videoListener = mVideoListener
        this.photoListener = mPhotoListener
    }

    interface OnDialogButtonClickListener {
        fun onDialogButtonClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
        this.window?.setWindowAnimations(R.style.DialogOutAndInStyle)

        setContentView(R.layout.dialog_share)

        //
        setCanceledOnTouchOutside(true)

        //
        initView()
    }

    private fun initView() {
        cancelText = findViewById(R.id.cancel)!!
        cancelText.setOnClickListener{
            this.dismiss()
        }

        videoText = findViewById(R.id.video)!!
        videoText.setOnClickListener{
            videoListener?.onVideoClick()
            this.dismiss()
        }
        photoText = findViewById(R.id.photo)!!
        photoText.setOnClickListener{
            photoListener?.onPhotoClick()
            this.dismiss()
        }
    }

    interface OnVideoClick {
        fun onVideoClick()
    }

    interface OnPhotoClick {
        fun onPhotoClick()
    }
}