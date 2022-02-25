package com.example.myplayer

import android.app.Application
import android.content.Context
import android.os.Process
import com.netease.nimlib.sdk.NIMClient
import dagger.hilt.android.HiltAndroidApp
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.auth.LoginInfo

import com.netease.nimlib.sdk.uinfo.model.UserInfo

import android.os.Environment
import android.text.TextUtils
import com.netease.nimlib.sdk.util.NIMUtil
import java.io.IOException
import android.R
import com.netease.nimlib.sdk.StatusBarNotificationConfig


@HiltAndroidApp
class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        NIMClient.init(this, loginInfo(), options());


        MainApplication.applicationContext = this
    }

    private fun loginInfo(): LoginInfo? {
        val loginInfo: LoginInfo = LoginInfo("15940850832","feaf81e470b6281ef0063ac82b66182e")
        return loginInfo
    }

    // 设置初始化配置参数，如果返回值为 null，则全部使用默认参数。
    open fun options(): SDKOptions? {
        val options = SDKOptions()
        options.preloadAttach = true
        return options
    }
    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    fun getAppCacheDir(context: Context): String? {
        var storageRootPath: String? = null
        try {
            if (context.externalCacheDir != null) {
                storageRootPath = context.externalCacheDir!!.canonicalPath
            }
        } catch (e: IOException) {
            e.printStackTrace()

        }
        if (TextUtils.isEmpty(storageRootPath)) {
            storageRootPath = Environment.getExternalStorageDirectory()
                .toString() + "/" + MainApplication.context?.getPackageName() //DemoCache.getContext()
        }
        return storageRootPath
    }

    companion object {
        lateinit var applicationContext: Application
        var context: Application? = null
        fun getContext(): Context {
            return context!!
        }
        fun exitApplication() {
            Process.killProcess(Process.myPid())
        }
    }
}