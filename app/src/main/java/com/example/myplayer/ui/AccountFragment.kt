package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.myplayer.R
import com.example.myplayer.databinding.FragmentAccountBinding
import com.example.myplayer.viewmodels.LoginViewModel
import com.example.myplayer.widget.ExitDialog
import com.example.myplayer.widget.PopDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment: Fragment(), PopDialog.OnDialogButtonClickListener {
    private lateinit var moviesBinding: FragmentAccountBinding

    private var loginJob: Job? = null
    private lateinit var num: String
    private lateinit var pwd: String
    private lateinit var token: String

    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moviesBinding = FragmentAccountBinding.inflate(inflater, container, false)
        return moviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getString("number") != null) {
            moviesBinding.textView4.setText(arguments?.getString("number"))
        }
        initListener()
        refreshInfo()

    }

    private fun refreshInfo() {
        val sharedPref =
            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        num = sharedPref.getString("USERNAME","").toString()
        pwd = sharedPref.getString("PASSWORD","").toString()
        loginNew(num, pwd)
    }

    private fun loginNew(num: String, pwd: String) {
        loginJob?.cancel()
        loginJob = lifecycleScope.launch {
            loginViewModel.user(num, pwd)
                .observe(viewLifecycleOwner) {
                    if (it != null && it.num != null) {
                        val sharedPref =
                            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                        sharedPref.edit().putInt("COIN", it.coin).apply()
                        sharedPref.edit().putString("TOKEN", it.token).apply()
                        moviesBinding.number = it.num
                        moviesBinding.mCoin = it.coin.toString()
                        moviesBinding.mName = it.name
                        token = it.token
                    } else {
                    }
                }
        }
    }

    private fun initListener() {
        moviesBinding.setClickListenerCharge {
            popDialog(requireActivity())
        }
        moviesBinding.setClickListenerLogout {
            exitDialog(requireActivity())
        }
    }

    fun popDialog(activity: FragmentActivity) {
        val popDialog = PopDialog(activity, this)
        popDialog.show()
    }

    fun exitDialog(activity: FragmentActivity) {
        val exitDialog = ExitDialog(activity)
        exitDialog.show()
    }

    override fun onDialogButtonClick() {
        android.util.Log.d("zwj" ,"onclick")
        val bundle = Bundle().apply {
            putString("token", token)
            putString("num", num)
        }
        requireView().findNavController().navigate(R.id.action_navigation_notifications_to_chat_fragment, bundle)

    }
}