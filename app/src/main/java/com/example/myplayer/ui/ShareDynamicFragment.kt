package com.example.myplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.myplayer.databinding.FragmentDynamicBinding
import com.example.myplayer.databinding.FragmentScanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareDynamicFragment: DialogFragment() {
    private lateinit var scanBinding: FragmentDynamicBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        scanBinding = FragmentDynamicBinding.inflate(inflater, container, false)
        return scanBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        const val SHARE_TAG = "SHARE_TAG"
    }

}