package com.example.myplayer.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
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
import android.graphics.drawable.BitmapDrawable
import com.example.myplayer.MainActivity
import com.example.myplayer.util.FileUtils


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

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bitmap: Bitmap = BitmapFactory.decodeResource(MainActivity.context?.resources,R.mipmap.screenback)

        val valueAnimator = ValueAnimator.ofInt(200, 255)
        valueAnimator.duration = 8000
        valueAnimator.addUpdateListener { animation ->
            val curValue = animation.animatedValue as Int

            var bitmapB: Bitmap? = FileUtils.toBlur(bitmap,2)
            val bd = BitmapDrawable(bitmapB)
            bd.alpha = curValue
            moviesBinding.background.background = bd
        }
        valueAnimator.repeatMode = 2
        valueAnimator.repeatCount = 10000
        valueAnimator.start()

        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getString("number") != null) {
            moviesBinding.textView4.setText(arguments?.getString("number"))
        }
        initListener()
        refreshInfo()

    }

    fun bitMapScale(bitmap: Bitmap, scale: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postScale(scale, scale) //长和宽放大缩小的比例
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
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
        val bundle = Bundle().apply {
            putString("token", token)
            putString("num", num)
        }
        requireView().findNavController().navigate(R.id.action_navigation_notifications_to_chat_fragment, bundle)

    }
    override fun onDestroyView() {
        loginJob?.cancel()

        super.onDestroyView()
    }
}