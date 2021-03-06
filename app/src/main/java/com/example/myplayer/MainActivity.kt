package com.example.myplayer

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myplayer.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import android.view.MotionEvent
import android.view.View

import android.view.WindowManager

import android.os.Build
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import java.io.File
import com.amap.api.location.AMapLocationClientOption
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import android.app.job.JobInfo

import android.content.ComponentName

import android.app.job.JobScheduler
import com.example.myplayer.util.JobServicesTest


const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var permissions = Manifest.permission.READ_EXTERNAL_STORAGE
    var permissionArray = arrayOf(permissions,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.CAMERA,Manifest.permission.CAPTURE_AUDIO_OUTPUT)


    var  mLocationClient: AMapLocationClient? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val checkPermission = this.let { ActivityCompat.checkSelfPermission(it, permissions) }
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissionArray, 0)
        }
        mainActivity = this
        context = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRootViewFitsSystemWindows(this, true)
        fullScreen(this)
        navView = binding.navView

        val navController = findNavController(R.id.main_nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movies_fragment, R.id.navigation_dashboard, R.id.navigation_lives, R.id.navigation_notifications
            )
        )
        this.supportActionBar?.hide()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.player_fragment || destination.id == R.id.info_detail || destination.id == R.id.web_fragment || destination.id == R.id.release_fragment) {
                runOnUiThread { navView!!.visibility = View.GONE }
            } else {
                runOnUiThread { navView!!.visibility = View.VISIBLE }
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView!!.setupWithNavController(navController)
       // mLocationClient!!.start()
        mResources = resources
        binding.navView.background.alpha = 200
    }

    override fun onResume() {
        super.onResume()
        // Add the FLAG_KEEP_SCREEN_ON flag for keeping on screen.
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        window.addFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        hideNavigationBar()
            setUpLocal()
       // launchApp()

    }

    private fun launchApp(): Boolean {
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancelAll()
        val builder = JobInfo.Builder(
            1024, ComponentName(
                packageName,
                JobServicesTest::class.java.getName()
            )
        )
        builder.setMinimumLatency(10000)
        builder.setOverrideDeadline(10000)
        builder.setPersisted(true)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        val schedule = jobScheduler.schedule(builder.build())
        return if (schedule <= 0) {
            false
        } else true
    }

    private fun setUpLocal() {
//        AMapLocationClient.updatePrivacyShow(context,true,true)
//        AMapLocationClient.updatePrivacyAgree(context,true)
        try {
            mLocationClient = AMapLocationClient(getApplicationContext())
            if (mLocationClient == null ) {
            }
        } catch (e: Exception) {
        }
        //??????LocationClient???
        mLocationClient!!.setLocationListener(object: AMapLocationListener {
            override fun onLocationChanged(amapLocation: AMapLocation?) {
                android.util.Log.d("zwj" ,"local test $amapLocation")
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        android.util.Log.d("zwj" ,"local test $amapLocation")
                        val sharedPref =
                            mainActivity.getSharedPreferences("CITY_CACHE", Context.MODE_PRIVATE)
                        sharedPref.edit().putString("CITY", cutString(amapLocation.city)).apply()
                    }else {
                        android.util.Log.d("zwj" ,"local test error")
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }

        })
        var mLocationOption: AMapLocationClientOption? = null
        mLocationOption = AMapLocationClientOption()
        mLocationOption.isOnceLocation = true
        mLocationOption.isOnceLocationLatest = true
        mLocationClient!!.setLocationOption(mLocationOption)
        mLocationClient!!.startLocation()

    }

    private fun cutString(city:String): String {
        var str: String = city
        var strs: List<String> = str.split("???")
        return strs[0].toString()
    }

    private fun hideNavigationBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
    }

//    val decorView = window.decorView
//    val option =
//        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//    decorView.systemUiVisibility = option
//    window.statusBarColor = Color.TRANSPARENT

    fun setRootViewFitsSystemWindows(activity: Activity, fitSystemWindows: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val winContent = activity.findViewById<View>(android.R.id.content) as ViewGroup
            if (winContent.childCount > 0) {
                val rootView = winContent.getChildAt(0) as ViewGroup
                if (rootView != null) {
                    rootView.fitsSystemWindows = fitSystemWindows
                }
            }
        }
    }

    private val onTouchListeners = ArrayList<MyOnTouchListener>(
        10
    )

    private fun fullScreen(activity: Activity) {

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        for (listener in onTouchListeners) {
            listener.onTouch(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun registerMyOnTouchListener(myOnTouchListener: MyOnTouchListener) {
        onTouchListeners.add(myOnTouchListener)
    }

    fun unregisterMyOnTouchListener(myOnTouchListener: MyOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener)
    }

    interface MyOnTouchListener {
        fun onTouch(ev: MotionEvent?): Boolean
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "??????????????????", Toast.LENGTH_LONG).show()
                }
        }
    }

    companion object {
        lateinit var mResources: Resources
        lateinit var mainActivity: MainActivity
        var context: MainActivity? = null
        var navView: BottomNavigationView? = null
        fun getContext(): Context {
            return context!!
        }
        fun getNav(): BottomNavigationView {
            return navView!!
        }

        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }

        fun showNavigationBar() {
            context?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

}