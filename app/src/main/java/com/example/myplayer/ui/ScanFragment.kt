package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplayer.databinding.FragmentScanBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.viewpager2.widget.ViewPager2

import android.R
import com.example.myplayer.MainActivity
import com.example.myplayer.adapter.ViewPagerAdapter


@AndroidEntryPoint
class ScanFragment(fragmentClick: OnFragmentClick): Fragment() {
    private lateinit var scanBinding: FragmentScanBinding

    private val onFragmentClick: OnFragmentClick = fragmentClick

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MainActivity.getNav().visibility = View.GONE
        scanBinding = FragmentScanBinding.inflate(inflater, container, false)
        return scanBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url: List<String>? = arguments?.getStringArrayList("url")
        val position: Int? = arguments?.getInt("position")
//        scanBinding.url = url
//        scanBinding.fragment.setOnClickListener(View.OnClickListener {
//            android.util.Log.d("zwj" ,"fragmenrClick")
//            onFragmentClick?.fragmentClick()
//        })
        val viewPager2: ViewPager2 = scanBinding.vp2
        val adapter = ViewPagerAdapter(url,requireContext(),onFragmentClick)
        viewPager2.adapter = adapter
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        if (position != null) {
            viewPager2.setCurrentItem(position,false)
        }
    }

    interface OnFragmentClick {
        fun fragmentClick()
    }
    override fun onDestroyView() {
        MainActivity.getNav().visibility = View.VISIBLE
        super.onDestroyView()
    }


    companion object {
        const val SCAN_TAG = "SCAN_TAG"
    }

}