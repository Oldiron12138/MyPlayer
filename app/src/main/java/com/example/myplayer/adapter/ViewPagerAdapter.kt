package com.example.myplayer.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.datastore.preferences.core.preferencesOf
import androidx.fragment.app.FragmentActivity
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.databinding.ViewPagerItemBinding
import com.example.myplayer.ui.ScanFragment
import com.example.myplayer.widget.ExitDialog
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.PhotoViewAttacher

class ViewPagerAdapter internal constructor(private val colors: List<String>? , private val context: Context) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    var mListerner: ScanFragment.OnFragmentClick? = null


    constructor(colors: List<String>?, context: Context, mListerner: ScanFragment.OnFragmentClick) :this(colors,context) {
        this.mListerner = mListerner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewPagerItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = position % itemCount
        colors?.get(position)?.let {
            holder.bind(it, mListerner)
        }
    }

    override fun getItemCount(): Int {
        if (colors != null) {
            return colors.size
        } else {
            return 0
        }
    }

    inner class ViewHolder(val binding: ViewPagerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: String , listener: ScanFragment.OnFragmentClick?) {
            binding.url = uri
            if (listener != null) {
            binding.photoView.setOnPhotoTapListener(object : OnPhotoTapListener {
                override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
                    listener.fragmentClick(position)
                }
            })
        }}
    }
}