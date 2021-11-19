package com.example.myplayer.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.myplayer.R

@BindingAdapter(value = ["bindMoviesImage"])
fun bindMoviesImage(view: ImageView, episodeId: String?) {
    episodeId?.let {
        val url = it

        Glide.with(view.context)
            .load(url)
            .dontAnimate()
            .error(R.drawable.ic_launcher_foreground)
            .into(view)
    }
}

@BindingAdapter(value = ["bindInfoImage"])
fun bindInfoImage(view: ImageView, episodeId: String?) {
    episodeId?.let {
        val url = it

        Glide.with(view.context)
            .load(url)
            .dontAnimate()
            .error(R.drawable.ic_launcher_foreground)
            .into(view)
    }
}

@BindingAdapter(value = ["bindDialogContent"])
fun bindDialogContent(view: TextView, coin: Int) {
    coin?.let {
        if (coin >= 1) {
            view.text = "是否花费1金币购买此内容？"
        } else {
            view.text = "金币"
        }
    }
}