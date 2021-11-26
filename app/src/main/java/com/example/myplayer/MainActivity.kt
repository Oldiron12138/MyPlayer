package com.example.myplayer

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


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.main_nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movies_fragment, R.id.navigation_dashboard, R.id.navigation_notifications
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

}