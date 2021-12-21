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
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.databinding.ListCateItemBinding

class CategoryAdapter(
    val context: Context
) : RecyclerView.Adapter<CategoryAdapter.CateViewHolder>() {

    //
    var cateMap:MutableMap<String, String> = sortedMapOf()

    private val assets: MutableList<String> = mutableListOf()
    private val assetsKey: MutableList<String> = mutableListOf()
    private var itemClickListener: OnItemClickListener? = null
    //

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CateViewHolder {


        return CateViewHolder(
            ListCateItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CateViewHolder, position: Int) {
        val asset = assets[position]
        holder.bind(asset, position)
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(asset: MoviesEntity)
    }

    fun updateListItem() {
        assets.add("热门")
        assets.add("国产")
        assets.add("欧美")
        assets.add("日韩")
        assets.add("动漫")
        assets.add("主播")
        assetsKey.add("remen")
        assetsKey.add("guochan")
        assetsKey.add("oumei")
        assetsKey.add("rihan")
        assetsKey.add("dongman")
        assetsKey.add("zhubo")


    }

    inner class CateViewHolder(
        val binding: ListCateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.city?.let {

                }
            }
        }

        fun bind(item: String, position: Int) {
            binding.apply {
                city = item
                executePendingBindings()
            }
        }
    }
}
private class CateDiffCallback(
    val oldList: List<String>,
    val newList: List<String>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
