package com.example.myplayer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myplayer.databinding.ActivitySplashBinding
import com.example.myplayer.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myplayer.MainApplication.Companion.context

private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.INTERNET)

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private var splashJob: Job? = null

    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!hasPermissions(this)) {
            initCity()
        } else {
            // If permissions have already been granted, proceed
            initCity()
        }
    }

    private fun initCity() {
        splashJob?.cancel()
        splashJob = lifecycleScope.launch {
            //splashViewModel.user(baseContext)
            binding.process = 30
            initInfo()
        }
    }

    private fun initInfo() {
        splashJob?.cancel()
        splashJob = lifecycleScope.launch {
            if(splashViewModel.infos(baseContext)) {
                binding.process = 60
                initMovies()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                // Take the user to the success fragment when permission is granted
                Toast.makeText(context, "Permission request granted", Toast.LENGTH_LONG).show()
                initCity()
            } else {
                Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initMovies() {
        splashJob?.cancel()
        splashJob = lifecycleScope.launch {
            if(splashViewModel.movies(baseContext)){
            binding.process = 100
            val intent = Intent()
            intent.setClass(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()}
        }
    }

    companion object {

        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

}