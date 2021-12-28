package com.example.myplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplayer.databinding.FragmentCircleBinding
import com.example.myplayer.databinding.FragmentSelectBinding

class SelectVideo: Fragment() {
    private lateinit var selectBinding: FragmentSelectBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        selectBinding = FragmentSelectBinding.inflate(inflater, container, false)
        return selectBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // registerListener()
//        subscribeUi()
//        getInfoDetailData()
    }

    companion object{
        const val SELECT_TAG = "SELECT_TAG"
    }
}