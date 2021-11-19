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
import com.aliyuncs.exceptions.ClientException
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.conn.ssl.AllowAllHostnameVerifier
import com.aliyuncs.CommonResponse

import com.aliyuncs.CommonRequest
import com.aliyuncs.DefaultAcsClient

import com.aliyuncs.IAcsClient
import com.aliyuncs.exceptions.ServerException
import com.aliyuncs.http.MethodType

import com.aliyuncs.profile.DefaultProfile




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
            if (moviesBinding.textView4.text.isEmpty()) {
                moviesBinding.error1.visibility = View.VISIBLE
                moviesBinding.numError = "*账号不能为空"
            } else {
                moviesBinding.error1.visibility = View.GONE
            }
            if (moviesBinding.textView4.text.length != 11 && moviesBinding.textView4.text.length > 0) {
                moviesBinding.error1.visibility = View.VISIBLE
                moviesBinding.numError = "*请输入正确的手机号"
            }
            if (moviesBinding.textView6.text.isEmpty() || moviesBinding.pwdConfirm.text.isEmpty()) {
                //Toast.makeText(this.requireContext(), "请输入正确的账号与密码", Toast.LENGTH_SHORT).show()
            } else {
                if (moviesBinding.textView6.text.toString().equals(moviesBinding.pwdConfirm.text.toString())) {
                    //sendSms()
                    register(moviesBinding.textView4.text.toString(), moviesBinding.textView6.text.toString())
                } else {
                    //Toast.makeText(this.requireContext(), "两次输入的密码不同", Toast.LENGTH_SHORT).show()
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

//    fun sendSms() {
//        val profile = DefaultProfile.getProfile(
//            "zhangweijie12138",
//            "<LTAI5tQjersCWei4hrzbZtNM>",
//            "<Sh71NeXn7gFcIu6g6kKtQrwHNYeVJU>"
//        ) //自己账号的AccessKey信息
//        val client: IAcsClient = DefaultAcsClient(profile)
//        val request = CommonRequest()
//        request.sysMethod = MethodType.POST
//        request.sysDomain = "dysmsapi.aliyuncs.com" //短信服务的服务接入地址
//        request.sysVersion = "2017-05-25" //API的版本号
//        request.sysAction = "SendSms" //API的名称
//        request.putQueryParameter("PhoneNumbers", "15940850830") //接收短信的手机号码
//        request.putQueryParameter("SignName", "阿里大于测试专用") //短信签名名称
//        request.putQueryParameter("TemplateCode", "SMS_209335004") //短信模板ID
//        request.putQueryParameter("TemplateParam", "{\"code\":\"1111\"}") //短信模板变量对应的实际值
//        try {
//            val response = client.getCommonResponse(request)
//            android.util.Log.d("zwj " ,"reponse ${response.data}")
//        } catch (e: ServerException) {
//            e.printStackTrace()
//        } catch (e: ClientException) {
//            e.printStackTrace()
//        }
//        }

}