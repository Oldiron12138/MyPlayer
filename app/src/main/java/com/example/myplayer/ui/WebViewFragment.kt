package com.example.myplayer.ui

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myplayer.databinding.FragmentWebBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class WebFragment :Fragment() {
    var mTimer = Timer()
    private lateinit var binding : FragmentWebBinding
    private lateinit var url: String
    private var time: Long = 0
    private var moviesJob: Job? = null
    var currentTime = MutableLiveData<Long>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentTime.postValue(System.currentTimeMillis()/1000)
        binding = FragmentWebBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getString("url") != null){
            url = arguments?.getString("url")!!
        }
        if (arguments?.getLong("util_time") != null){
            time = arguments?.getLong("util_time")!!
        }
        loginNew()
        subscribeUi()
    }

    private fun loginNew() {
        moviesJob?.cancel()
        moviesJob = lifecycleScope.launch {
            currentTime
                .observe(viewLifecycleOwner) {
                    android.util.Log.d("zwj" ,"update")
                    if(currentTime.value!! > time) {
                        findNavController().popBackStack()
                    }
                }
        }
    }

    private fun subscribeUi() {

        mTimer.schedule(object :TimerTask() {
            override fun run() {
                currentTime.postValue(System.currentTimeMillis()/1000)
            }
        }, 1000*60, 1000*60)
        binding.back.setOnClickListener{
            it.findNavController().popBackStack()
        }
        val webView: WebView = binding.webview
        webView.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                var stringBuilder = StringBuilder()
                stringBuilder.append("Url)")
                stringBuilder.append((request!!.url.toString()))
                if (view != null) {
                    view.loadUrl(request!!.url.toString())
                }
                return true
            }
        }
        var webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.loadsImagesAutomatically = true
        webSettings.defaultTextEncodingName = "utf-8"
        webView.loadUrl(url)
        //webView.loadUrl("https://www.baidu.com")
    }
}