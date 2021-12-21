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
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.externalCacheDir != null) {
                storageRootPath = context.externalCacheDir!!.canonicalPath
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
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