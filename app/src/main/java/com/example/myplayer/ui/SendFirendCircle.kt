package com.example.myplayer.ui

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplayer.base.TAG
import com.example.myplayer.databinding.FragmentFriendSendBinding
import java.io.FileNotFoundException

class SendFirendCircle(uri: Uri) : Fragment() {
    private lateinit var selectBinding: FragmentFriendSendBinding
    val url: Uri = uri
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        selectBinding = FragmentFriendSendBinding.inflate(inflater, container, false)
        return selectBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // registerListener()
        subscribeUi()
//        getInfoDetailData()
    }

    private fun subscribeUi() {
        val uri: Uri? = url
        val cr: ContentResolver = requireContext().contentResolver
        try {
            val bitmap = BitmapFactory.decodeStream(uri?.let { cr.openInputStream(it) })
            selectBinding.photo.setImageBitmap(bitmap)
        } catch (e: FileNotFoundException) {
            // Log.e("Exception", e.getMessage(), e)
        }

        selectBinding.cancel.setOnClickListener{
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = fragmentManager.findFragmentByTag(SEND_TAG)
            fragment?.let {
                fragmentTransaction.remove(fragment)
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    companion object {
        const val SEND_TAG = "SEND_TAG"
    }
}