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
import com.example.myplayer.data.reponse.CityResponse
import com.example.myplayer.data.reponse.InfoResponse
import com.example.myplayer.databinding.ListItemCityBinding
import com.example.myplayer.databinding.ListItemInfoBinding

class CityAdapter(
    val context: Context
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    //
    private val assets: MutableList<String> = mutableListOf()

    //
    fun updateListItem(datas: List<String>) {
        val diffCallback = CityDiffCallback(assets, datas)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        assets.clear()
        assets.addAll(datas)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(
            ListItemCityBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val asset = assets[position]
        holder.bind(asset, position)
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    inner class CityViewHolder(
        val binding: ListItemCityBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(item: String, position: Int) {
            binding.apply {
                city = item
                executePendingBindings()
                binding.setClickListener {view ->
                    binding.city?.let {
                        if (!test(it)) {
                            val bundle = Bundle().apply {
                                putString("city", it)
                            }
                            view.findNavController()
                                .navigate(R.id.action_city_fragment_to_navigation_dashboard, bundle)
                            Navigation.findNavController(view).popBackStack(R.id.moives_list, true)
                        }
                        }
                }
            }
        }
    }
}

fun test(s: String): Boolean {
    val c = s[0]
    val i = c.toInt()
    return i >= 65 && i <= 90 || i >= 97 && i <= 122
}

private fun showDetailPage(
    view: View, asset: CityResponse, index: Int
) {
    val bundle = Bundle().apply {
        putInt("index", index)
    }
    view.findNavController().navigate(R.id.action_navigation_dashboard_to_info_detail, bundle)
}

private class CityDiffCallback(
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