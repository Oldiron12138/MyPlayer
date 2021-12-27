package com.example.myplayer

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myplayer.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.location.BDLocation

import com.baidu.location.BDAbstractLocationListener
import android.view.WindowManager

import android.os.Build
import android.view.Window
import android.app.Activity
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import androidx.annotation.RequiresApi
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
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//            val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                val window: Window = window
//                val attributes: WindowManager.LayoutParams = window.getAttributes()
//                attributes.flags = attributes.flags or flagTranslucentNavigation
//                window.setAttributes(attributes)
//                getWindow().statusBarColor = Color.TRANSPARENT
//            } else {
//                val window: Window = window
//                val attributes: WindowManager.LayoutParams = window.getAttributes()
//                attributes.flags =
//                    attributes.flags or (flagTranslucentStatus or flagTranslucentNavigation)
//                window.setAttributes(attributes)
//            }
//        }
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.main_nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movies_fragment, R.id.navigation_dashboard, R.id.navigation_lives, R.id.navigation_notifications
            )
        )
        this.supportActionBar?.hide()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.player_fragment) {
                runOnUiThread { navView.setVisibility(View.GONE) }
            } else {
                runOnUiThread { navView.setVisibility(View.VISIBLE) }
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        mLocationClient!!.start()
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                val window = activity.window
                val decorView = window.decorView
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
//                val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
//                decorView.systemUiVisibility = option
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                setAndroidNativeLightStatusBar(this, true)
                window.statusBarColor = Color.TRANSPARENT
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                val window = activity.window
                val attributes = window.attributes
                val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                val flagTranslucentNavigation =
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                attributes.flags = attributes.flags or flagTranslucentStatus
                //                attributes.flags |= flagTranslucentNavigation;
                window.attributes = attributes
            }
        }
    }

    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
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
        fun getContext(): Context {
            return context!!
        }
    }

}