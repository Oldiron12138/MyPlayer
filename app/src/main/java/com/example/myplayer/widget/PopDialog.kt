package com.example.myplayer.widget

import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.myplayer.R

class PopDialog(private val activity: FragmentActivity) :
    AlertDialog(activity, R.style.Theme_AppCompat_Dialog) {

    private lateinit var cancelText: TextView
    private lateinit var exitText: TextView

    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pop)

        //
        setCanceledOnTouchOutside(false)

        //
        initView()
    }

    private fun initView() {
        cancelText = findViewById(R.id.exit_dialog_cancel)!!
        cancelText.setOnClickListener {
            this.dismiss()
        }
    }
}