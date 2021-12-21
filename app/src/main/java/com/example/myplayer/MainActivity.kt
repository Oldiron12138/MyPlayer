package com.example.myplayer

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
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
        option.setNeedNewVersionRgc(true)
        mLocationClient!!.setLocOption(option)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

    private val onTouchListeners = ArrayList<MyOnTouchListener>(
        10
    )

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
            android.util.Log.d("zwj" ,"city $addr" )
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