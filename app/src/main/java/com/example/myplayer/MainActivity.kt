package com.example.myplayer

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
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.location.BDLocation

import com.baidu.location.BDAbstractLocationListener
import android.view.WindowManager

import android.os.Build
import android.app.Activity
import android.view.ViewGroup

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var mLocationClient: LocationClient? = null
    val myListener = MyLocationListener()
    public lateinit var currentCity: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity = this
        mLocationClient = LocationClient(getApplicationContext())
        //声明LocationClient类
        mLocationClient!!.registerLocationListener(myListener)

        val option = LocationClientOption()
        option.setIsNeedAddress(true)
        option.setOpenGps(true)
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy)
        option.setCoorType("gcj02");
        option.setNeedNewVersionRgc(true)
        mLocationClient!!.setLocOption(option)
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
            if (destination.id == R.id.player_fragment || destination.id == R.id.scan_fragment) {
                runOnUiThread { navView!!.setVisibility(View.GONE) }
            } else {
                runOnUiThread { navView!!.setVisibility(View.VISIBLE) }
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView!!.setupWithNavController(navController)
        mLocationClient!!.start()
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
    }

    private fun hideNavigationBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
    }

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

    class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            val addr = location.addrStr //获取详细地址信息
            val country = location.country //获取国家
            val province = location.province //获取省份
            val city = location.city //获取城市
            val district = location.district //获取区县
            val street = location.street //获取街道信息
            val adcode = location.adCode //获取adcode
            val town = location.town //获取乡镇信息
            //val ccc:String = city.toString()
            android.util.Log.d("zwj" ,"city ${location.locType}" )
            //this.currentCity
            val sharedPref =
                mainActivity.getSharedPreferences("CITY_CACHE", Context.MODE_PRIVATE)
            sharedPref.edit().putString("CITY", "乌鲁木齐").apply()
        }
    }

    companion object {
        lateinit var mainActivity: MainActivity
        var context: MainActivity? = null
        var navView: BottomNavigationView? = null
        fun getContext(): Context {
            return context!!
        }
        fun getNav(): BottomNavigationView {
            return navView!!
        }
    }

}