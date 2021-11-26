package com.example.myplayer.widget

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import com.example.myplayer.MainApplication
import com.example.myplayer.R
import com.example.myplayer.widget.ExitDialog.OnDialogButtonClickListener
import android.view.View
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.ui.DetailFragment


class ExitDialog(private val activity: FragmentActivity) :
    AlertDialog(activity, R.style.Theme_AppCompat_Dialog) {

    private lateinit var centerText: TextView
    private lateinit var cancelText: TextView
    private lateinit var exitText: TextView
    private var content: String? = null
    private var listener: OnDialogButtonClickListener? = null
    private var listener2: OnDialogButtonClickListenerForInfo? = null
    private var asset:MoviesEntity? = null
    private var info:InfoEntity? = null

    constructor(activity: FragmentActivity, string: String, mListener: OnDialogButtonClickListener, mAsset: MoviesEntity) : this(activity) {
        content = string
        this.listener = mListener
        asset = mAsset
    }

    constructor(activity: FragmentActivity, string: String, mListener: OnDialogButtonClickListenerForInfo, mInfo: InfoEntity) : this(activity) {
        content = string
        this.listener2 = mListener
        info = mInfo
    }

    interface OnDialogButtonClickListener {
        fun onDialogButtonClick(isPositive: Boolean, asset: MoviesEntity)
    }

    interface OnDialogButtonClickListenerForInfo {
        fun onDialogButtonClickForInfo(isPositive: Boolean, info: InfoEntity)
    }

    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_exit)

        //
        setCanceledOnTouchOutside(false)

        //
        initView()
    }

    private fun initView() {
        centerText = findViewById(R.id.main_content)!!
        cancelText = findViewById(R.id.exit_dialog_cancel)!!
        exitText = findViewById(R.id.exit_dialog_exit)!!
        if (listener != null || listener2 != null) {
            centerText.text = content
            android.util.Log.d("zwj" ,"onclick111")
            cancelText.setOnClickListener{ it ->
                if (asset == null) {
                    when (it.id) {
                        R.id.exit_dialog_cancel ->
                            info?.let { listener2!!.onDialogButtonClickForInfo(true, it) }
                    }
                } else {
                    when (it.id) {
                        R.id.exit_dialog_cancel ->
                            asset?.let { listener!!.onDialogButtonClick(true, it) }
                    }
                }
                dismiss()
            }
            exitText.setOnClickListener{ it ->
                if (asset == null) {
                    when (it.id) {
                        R.id.exit_dialog_exit ->
                            info?.let { listener2!!.onDialogButtonClickForInfo(false, it) }
                    }
                } else {
                    when (it.id) {
                        R.id.exit_dialog_exit ->
                            asset?.let { listener!!.onDialogButtonClick(false, it) }
                    }
                }
                dismiss()
            }
        } else {
            android.util.Log.d("zwj" ,"onclick222")
            cancelText.setOnClickListener { it ->
                var sharedPref =
                    activity.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                sharedPref.edit().clear().commit()
                MainApplication.exitApplication()
            }
            exitText.setOnClickListener {
                this.dismiss()
            }
        }

    }
}