package com.example.myplayer.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.myplayer.databinding.FragmentRegBinding
import com.example.myplayer.viewmodels.InfoViewModel
import com.example.myplayer.viewmodels.LoginViewModel
import com.example.myplayer.viewmodels.RegViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegFragment: Fragment() {
    private lateinit var moviesBinding: FragmentRegBinding

    private var loginJob: Job? = null

    private val regViewModel: RegViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moviesBinding = FragmentRegBinding.inflate(inflater, container, false)
        return moviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener();

    }

    private fun initListener() {
        moviesBinding.setClickListener {
            if (moviesBinding.textView4.text.isEmpty() || moviesBinding.textView6.text.isEmpty() || moviesBinding.pwdConfirm.text.isEmpty()) {
                Toast.makeText(this.requireContext(), "请输入正确的账号与密码", Toast.LENGTH_SHORT).show()
            } else {
                if (moviesBinding.textView6.text.toString().equals(moviesBinding.pwdConfirm.text.toString())) {
                    register(moviesBinding.textView4.text.toString(), moviesBinding.textView6.text.toString())
                } else {
                    Toast.makeText(this.requireContext(), "两次输入的密码不同", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun register(num: String, pwd: String) {
        loginJob?.cancel()
        loginJob = lifecycleScope.launch {
            regViewModel.register(num, pwd)
                ?.observe(viewLifecycleOwner) {
                    if (it.resultData) {
                        val bundle = Bundle().apply {
                            putString("number", moviesBinding.textView4.text.toString())
                        }
                        requireView().findNavController().navigate(R.id.action_reg_fragment_to_login_fragment, bundle)
                    } else {
                        Toast.makeText(requireContext(), "账号或密码错误", Toast.LENGTH_LONG).show()
                        moviesBinding.textView6.text.clear()
                    }
                }
        }
    }
}