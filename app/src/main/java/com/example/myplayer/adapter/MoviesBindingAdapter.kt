package com.example.myplayer.adapter

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.myplayer.R
import androidx.core.app.ActivityCompat.startPostponedEnterTransition
import com.example.myplayer.MainActivity
import com.squareup.picasso.Callback

import com.squareup.picasso.Picasso




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

@BindingAdapter(value = ["bindInfoImagePP"])
fun bindInfoImagePP(view: ImageView, episodeId: String?) {
    episodeId?.let {
        val url = it

        Picasso.with(view.context)
            .load(url)
            .centerCrop()
            .noFade()
            .fit()
            .into(view, object : Callback {
                override fun onSuccess() {
                    startPostponedEnterTransition(MainActivity.mainActivity)
                }

                override fun onError() {}
            })
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

@BindingAdapter(value = ["lockContent"])
fun lockContent(view: TextView, lock: Boolean) {
    lock?.let {
        if (lock) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter(value = ["showContent", "phoneDetail"], requireAll = true)
fun showContent(view: TextView, lock: Boolean, phone: String) {
    lock?.let {
        if (lock) {
            view.visibility = View.GONE
            view.text = phone
        } else {
            view.visibility = View.VISIBLE
            view.text = phone
        }
    }
}

@BindingAdapter(value = ["bindChatImage", "imageUrl"], requireAll = true)
fun bindChatImage(view: ImageView, bindChatImage: Boolean, imageUrl: String) {
    bindChatImage?.let {
        if (bindChatImage) {
            view.visibility = View.VISIBLE
            imageUrl?.let {
                val url = it

                Glide.with(view.context)
                    .load(url)
                    .dontAnimate()
                    .error(R.drawable.ic_launcher_foreground)
                    .into(view)
            }
        } else {
            view.visibility = View.GONE
        }
    }
}


@BindingAdapter(value = ["isPhoto"])
fun isPhoto(view: androidx.recyclerview.widget.RecyclerView, isP: Boolean) {
    isP?.let {
        if (isP) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter(value = ["isVideo"])
fun isVideo(view: FrameLayout, isP: Boolean) {
    isP?.let {
        if (isP) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter(value = ["isPlayButtonShow"])
fun isPlayButtonShow(view: ImageView, isP: Boolean) {
    isP?.let {
        if (isP) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter(value = ["bindUntilDays"])
fun bindUntilDays(view: TextView, time: Long?) {
    time?.let {
        var current_time: Long = System.currentTimeMillis() / 1000
        android.util.Log.d("zwj" ,"time$current_time")
        if (time < current_time) {
            view.text = "0小时"
        } else {
            if ((time - current_time)/60/60 < 1 ) {
                view.text = "<"+((time - current_time)/60/60+1).toString()+"小时"
            } else {
                view.text = ((time - current_time)/60/60).toString()+"小时"
            }

        }
    }
}