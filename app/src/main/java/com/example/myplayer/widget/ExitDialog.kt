package com.example.myplayer.widget

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import com.example.myplayer.MainApplication
import com.example.myplayer.R

class ExitDialog(private val activity: FragmentActivity) :
    AlertDialog(activity, R.style.Theme_AppCompat_Dialog) {

    private lateinit var cancelText: TextView
    private lateinit var exitText: TextView

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
        cancelText = findViewById(R.id.exit_dialog_cancel)!!
        exitText = findViewById(R.id.exit_dialog_exit)!!
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