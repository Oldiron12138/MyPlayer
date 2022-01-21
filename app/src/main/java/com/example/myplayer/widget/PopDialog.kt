package com.example.myplayer.widget

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.myplayer.R
import com.example.myplayer.data.db.MoviesEntity

class PopDialog(private val activity: FragmentActivity) :
    AlertDialog(activity, R.style.Theme_AppCompat_Dialog) {

    private lateinit var cancelText: TextView
    private lateinit var contentText: TextView
    private lateinit var exitText: TextView
    private var content: String = ""
    private var listener: OnDialogButtonClickListener? = null

    private var currentPosition = 0

    constructor(activity: FragmentActivity, string: String) : this(activity) {
        content = string
    }

    constructor(activity: FragmentActivity, mListener: OnDialogButtonClickListener) : this(activity) {
        this.listener = mListener
    }

    interface OnDialogButtonClickListener {
        fun onDialogButtonClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pop)
        this.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
        this.window?.setWindowAnimations(R.style.DialogOutAndInStyle)
        //
        setCanceledOnTouchOutside(true)

        //
        initView()
    }

    private fun initView() {
        contentText = findViewById(R.id.content)!!
        cancelText = findViewById(R.id.exit_dialog_cancel)!!
        if(content != "") {
            contentText.text = content
        }
        cancelText.setOnClickListener {
            listener?.onDialogButtonClick()
            this.dismiss()
        }
    }
}