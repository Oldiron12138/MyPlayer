package com.example.myplayer.widget

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.myplayer.R


class LoadingDialog(private val activity: FragmentActivity) :
    AlertDialog(activity, R.style.Theme_AppCompat_Dialog) {
    lateinit var animView: LoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)

        //
        setCanceledOnTouchOutside(false)

        //
        initView()
    }

    private fun initView() {
        animView = findViewById(R.id.anim_loading)!!
    }
    public fun dismissDialog(){
        animView.stop()
        this.dismiss()
    }
}