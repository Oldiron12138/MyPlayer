package com.example.myplayer

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Process
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {



    companion object {
        var context: Application? = null
        fun getContext(): Context {
            return context!!
        }
        fun exitApplication() {
            Process.killProcess(Process.myPid())
        }
    }
}