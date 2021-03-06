package com.example.myplayer

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.myplayer.MainActivity
import com.example.myplayer.MainApplication
import com.example.myplayer.databinding.ActivityLoginBinding
import com.example.myplayer.databinding.ActivitySplashBinding
import com.example.myplayer.util.FileUtils
import com.example.myplayer.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var splashJob: Job? = null

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val bitmap: Bitmap = BitmapFactory.decodeResource(this?.resources,R.mipmap.screenback)
        val bitmapB: Bitmap? = FileUtils.toBlur(bitmap,2)

        val bd = BitmapDrawable(bitmapB)
        binding.loginBack.background = bd
        setContentView(binding.root)

    }
}