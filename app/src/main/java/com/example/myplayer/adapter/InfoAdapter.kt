package com.example.myplayer.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayer.R
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.reponse.MoviesResponse
import com.example.myplayer.databinding.ListItemInfoBinding
import androidx.core.app.ActivityOptionsCompat
import com.example.myplayer.data.reponse.CirclePhotoResponse


class InfoAdapter(
    val context: Context
) : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    //
    private val assets: MutableList<InfoEntity> = mutableListOf()

    private var current: String = "乌鲁木齐"
    //
    fun updateListItem(datas: List<InfoEntity>, current: String) {
        val diffCallback = InfoDiffCallback(assets, datas)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.current = current
        assets.clear()
        assets.addAll(datas)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        return InfoViewHolder(
            ListItemInfoBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val asset = assets[position]
        holder.bind(asset, position,current)
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    inner class InfoViewHolder(
        val binding: ListItemInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(item: InfoEntity, position: Int, current: String) {

            binding.apply {
                info = item
                executePendingBindings()
                binding.setClickListener {
                    binding.info?.let {
                            asset ->
                        showDetailPage(it, asset, position, current)
                    }
                }
            }
        }
    }
}

private fun showDetailPage(
    view: View, asset: InfoEntity, index: Int, current: String
) {
    val bundle = Bundle().apply {
        putInt("index", index)
        putString("current", current)
    }
    view.findNavController().navigate(R.id.action_navigation_dashboard_to_info_detail, bundle)
}

private class InfoDiffCallback(
    val oldList: List<InfoEntity>,
    val newList: List<InfoEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}