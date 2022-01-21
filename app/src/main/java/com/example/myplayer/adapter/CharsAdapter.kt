package com.example.myplayer.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayer.R
import com.example.myplayer.data.reponse.CirclePhotoResponse
import com.example.myplayer.data.reponse.CityResponse
import com.example.myplayer.data.reponse.InfoResponse
import com.example.myplayer.databinding.ListItemCharBinding
import com.example.myplayer.databinding.ListItemCityBinding
import com.example.myplayer.databinding.ListItemInfoBinding

class CharsAdapter(
    val context: Context
) : RecyclerView.Adapter<CharsAdapter.CharsViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener
    //
    private val assets: MutableList<String> = mutableListOf()

    //
    fun updateListItem(datas: List<String>) {
        val diffCallback = CharsDiffCallback(assets, datas)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        assets.clear()
        assets.addAll(datas)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharsViewHolder {
        return CharsViewHolder(
            ListItemCharBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CharsViewHolder, position: Int) {
        val asset = assets[position]
        holder.bind(asset, position)
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    interface OnItemClickListener {
        fun onItemClick(zimu: String)
    }

    inner class CharsViewHolder(
        val binding: ListItemCharBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(item: String, position: Int) {
            binding.apply {
                chasr = item
                executePendingBindings()
                binding.setClickListener {view ->
                    binding.chasr?.let {
                        itemClickListener.onItemClick(item)
                    }
                }
            }
        }
    }
}

//fun test(s: String): Boolean {
//    val c = s[0]
//    val i = c.toInt()
//    return i >= 65 && i <= 90 || i >= 97 && i <= 122
//}

private fun showDetailPage(
    view: View, asset: CityResponse, index: Int
) {
    val bundle = Bundle().apply {
        putInt("index", index)
    }
    view.findNavController().navigate(R.id.action_navigation_dashboard_to_info_detail, bundle)
}

private class CharsDiffCallback(
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