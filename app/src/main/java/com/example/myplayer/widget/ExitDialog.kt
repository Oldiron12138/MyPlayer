package com.example.myplayer.widget

import android.content.Context
import android.os.Build
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
import android.view.View.OnSystemUiVisibilityChangeListener
import android.view.ViewGroup
import android.view.WindowManager
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
    private var listener3: OnDialogButtonClickListenerForWeb? = null
    private var asset:MoviesEntity? = null
    private var index:Int? = 0
    private var info:InfoEntity? = null

    constructor(activity: FragmentActivity, string: String, mListener: OnDialogButtonClickListener, mAsset: MoviesEntity) : this(activity) {
        content = string
        this.listener = mListener
        asset = mAsset
    }

    constructor(activity: FragmentActivity, string: String, mListener: OnDialogButtonClickListenerForWeb,mIndex: Int) : this(activity) {
        content = string
        this.listener3 = mListener
        index = mIndex
    }

    constructor(activity: FragmentActivity, string: String, mListener: OnDialogButtonClickListenerForInfo, mInfo: InfoEntity,mIndex: Int) : this(activity) {
        content = string
        this.listener2 = mListener
        info = mInfo
        index = mIndex
    }
    interface OnDialogButtonClickListenerForWeb {
        fun onDialogButtonClick(isPositive: Boolean, index:Int)
    }

    interface OnDialogButtonClickListener {
        fun onDialogButtonClick(isPositive: Boolean, asset: MoviesEntity)
    }

    interface OnDialogButtonClickListenerForInfo {
        fun onDialogButtonClickForInfo(isPositive: Boolean, info: InfoEntity, index: Int?)
    }

    private var currentPosition = 0


    override fun show() {
        this.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.show()
        window?.decorView?.let { fullScreenImmersive(it) }
        this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }


    private fun fullScreenImmersive(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            view.systemUiVisibility = uiOptions
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        this.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//                View.SYSTEM_UI_FLAG_FULLSCREEN
        //this.window?.setWindowAnimations(R.style.DialogOutAndInStyle)
        setContentView(R.layout.dialog_exit)


        setCanceledOnTouchOutside(false)

        //
        initView()
        this.setOnDismissListener {
            this.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
           // this.window?.setWindowAnimations(R.style.DialogOutAndInStyle)
        }
    }


    private fun initView() {
        centerText = findViewById(R.id.main_content)!!
        cancelText = findViewById(R.id.exit_dialog_cancel)!!
        exitText = findViewById(R.id.exit_dialog_exit)!!
        if (listener != null || listener2 != null || listener3!=null) {
            centerText.text = content
            cancelText.setOnClickListener{ it ->
                if (asset == null && info!=null) {
                    when (it.id) {
                        R.id.exit_dialog_cancel ->
                            info?.let { listener2!!.onDialogButtonClickForInfo(true, it,index) }
                    }
                } else if(asset != null && info==null){
                    when (it.id) {
                        R.id.exit_dialog_cancel ->
                            asset?.let { listener!!.onDialogButtonClick(true, it) }
                    }
                } else {
                    android.util.Log.d("zwj " ,"adddaddd3")
                    index?.let { it1 -> listener3!!.onDialogButtonClick(true, it1) }
                }
                dismiss()
            }
            exitText.setOnClickListener{ it ->
                if (asset == null && info!=null) {
                    when (it.id) {
                        R.id.exit_dialog_exit ->
                            info?.let { listener2!!.onDialogButtonClickForInfo(false, it,index) }
                    }
                } else if(asset != null && info==null){
                    when (it.id) {
                        R.id.exit_dialog_exit ->
                            asset?.let { listener!!.onDialogButtonClick(false, it) }
                    }
                } else {
                     dismiss()
                }
                dismiss()
            }
        } else {
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