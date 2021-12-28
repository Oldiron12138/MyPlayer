package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplayer.databinding.FragmentScanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanFragment(fragmentClick: OnFragmentClick): Fragment() {
    private lateinit var scanBinding: FragmentScanBinding

    private val onFragmentClick: OnFragmentClick? = fragmentClick

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scanBinding = FragmentScanBinding.inflate(inflater, container, false)
        return scanBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url: String? = arguments?.getString("url")
        scanBinding.url = url
        scanBinding.fragment.setOnClickListener(View.OnClickListener {
            android.util.Log.d("zwj" ,"fragmenrClick")
            onFragmentClick?.fragmentClick()
        })
    }

    interface OnFragmentClick {
        fun fragmentClick()
    }

    companion object {
        const val SCAN_TAG = "SCAN_TAG"
    }

}