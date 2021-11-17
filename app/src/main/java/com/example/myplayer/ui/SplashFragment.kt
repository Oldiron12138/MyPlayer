package com.example.myplayer.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myplayer.MainActivity
import com.example.myplayer.R
import com.example.myplayer.databinding.ActivitySplashBinding
import com.example.myplayer.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment: Fragment() {
    private lateinit var splashBinding: ActivitySplashBinding

    private var splashJob: Job? = null

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        splashBinding = ActivitySplashBinding.inflate(inflater, container, false)
        return splashBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restoreLogin()
    }

    private fun restoreLogin() {
        val sharedPref =
            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        val num: String = sharedPref.getString("USERNAME","").toString()
        val pwd: String = sharedPref.getString("PASSWORD","").toString()
        if (TextUtils.isEmpty(num) || TextUtils.isEmpty(pwd)) {
            this.findNavController().navigate(R.id.action_splash_fragment_to_login_fragment)
        } else {
            loginNew(num, pwd)
        }
    }

    private fun initCity() {
        splashJob?.cancel()
        splashJob = lifecycleScope.launch {
            //splashViewModel.user(baseContext)
            splashBinding.process = 30
            initInfo()
        }
    }

    private fun initInfo() {
        splashJob?.cancel()
        splashJob = lifecycleScope.launch {
            if(splashViewModel.infos(requireContext())) {
                splashBinding.process = 60
                initMovies()
            }
        }
    }

    private fun initMovies() {
        splashJob?.cancel()
        splashJob = lifecycleScope.launch {
            if(splashViewModel.movies(requireContext())){
                splashBinding.process = 100
                val intent = Intent()
                intent.setClass(requireContext(),MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()}
        }
    }

    private fun loginNew(num: String, pwd: String) {
        splashJob?.cancel()
        splashJob = lifecycleScope.launch {
            splashViewModel.user(num, pwd)
                .observe(viewLifecycleOwner) {
                    if (it != null && it.num != null) {
                        val intent = Intent()
                        val sharedPref =
                            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                        sharedPref.edit().putString("USERNAME", it.num).apply()
                        sharedPref.edit().putInt("COIN", it.coin).apply()
//                        intent.setClass(requireContext(), MainActivity::class.java)
//                        startActivity(intent)
//                        requireActivity().finish();
                        initCity()
                    } else {
                        requireView().findNavController().navigate(R.id.action_splash_fragment_to_login_fragment)
                    }
                }
        }
    }
}