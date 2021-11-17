package com.example.myplayer.ui

import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.MainActivity
import com.example.myplayer.R
import com.example.myplayer.adapter.InfoAdapter
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.databinding.FragmentInfoBinding
import com.example.myplayer.databinding.FragmentLoginBinding
import com.example.myplayer.viewmodels.InfoViewModel
import com.example.myplayer.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private lateinit var moviesBinding: FragmentLoginBinding

    private var loginJob: Job? = null

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moviesBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return moviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getString("number") != null){
            moviesBinding.textView4.setText(arguments?.getString("number"))
        }
        initListener()

    }

    private fun initListener() {
        moviesBinding.setClickListenerLogin {
            if (moviesBinding.textView4.text.isEmpty() || moviesBinding.textView6.text.isEmpty()) {
                Toast.makeText(this.requireContext(), "请输入正确的账号与密码", Toast.LENGTH_SHORT)
            } else {
                loginNew(moviesBinding.textView4.text.toString(), moviesBinding.textView6.text.toString())
            }
        }
        moviesBinding.setClickListenerReg {
            requireView().findNavController().navigate(R.id.action_login_fragment_to_reg_fragment)
        }
    }

    private fun login(num: String, pwd: String) {
        loginJob?.cancel()
        loginJob = lifecycleScope.launch {
            loginViewModel.login(num, pwd)
                ?.observe(viewLifecycleOwner) {
                    if (it.resultData) {
                        val intent = Intent()
                        val sharedPref =
                            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                        sharedPref.edit().putString("USERNAME", num).apply()
                        sharedPref.edit().putString("PASSWORD", pwd).apply()
                        intent.setClass(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish();
                    } else {
                        Toast.makeText(requireContext(), "账号或密码错误", Toast.LENGTH_LONG)
                        moviesBinding.textView6.text.clear()
                    }
                }
        }
    }

    private fun loginNew(num: String, pwd: String) {
        loginJob?.cancel()
        loginJob = lifecycleScope.launch {
            loginViewModel.user(num, pwd)
                .observe(viewLifecycleOwner) {
                    if (it != null && it.num != null) {
                        val intent = Intent()
                        val sharedPref =
                            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                        sharedPref.edit().putString("USERNAME", num).apply()
                        sharedPref.edit().putString("PASSWORD", pwd).apply()
                        sharedPref.edit().putInt("COIN", it.coin).apply()
//                        intent.setClass(requireContext(), MainActivity::class.java)
//                        startActivity(intent)
//                        requireActivity().finish();
                        requireView().findNavController().navigate(R.id.action_login_fragment_to_splash_fragment2)
                    } else {
                        Toast.makeText(requireContext(), "账号或密码错误", Toast.LENGTH_LONG).show()
                        moviesBinding.textView6.text.clear()
                    }
                }
        }
    }
}