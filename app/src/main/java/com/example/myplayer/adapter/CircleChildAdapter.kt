package com.example.myplayer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayer.data.reponse.CirclePhotoResponse
import com.example.myplayer.databinding.ListCirclePhotoBinding

class CircleChildAdapter : RecyclerView.Adapter<CircleChildAdapter.CircleChildViewHolder>() {

    private val assets: MutableList<CirclePhotoResponse> = mutableListOf()

    private var xPosition = 0

    private var isShowSelectBg = false

    private var itemClickListener: OnItemClickListener? = null

    private var isItemClick = false

    fun updateChild(assetList: List<CirclePhotoResponse>) {
        isItemClick = false

        val diffCallback = CircleChildDiffCallback(assets, assetList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        assets.clear()
        assets.addAll(assetList)

        diffResult.dispatchUpdatesTo(this)
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        isItemClick = true
        itemClickListener = listener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircleChildViewHolder {
        return CircleChildViewHolder(
            ListCirclePhotoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CircleChildViewHolder, position: Int) {
        assets[position].let { asset ->
            holder.bind(asset)
        }
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    class CircleChildViewHolder(
        val binding: ListCirclePhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CirclePhotoResponse) {
            binding.apply {
                asset = item
                executePendingBindings()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(assetId: String)
    }
}

private class CircleChildDiffCallback(
    val oldAssetList: List<CirclePhotoResponse>,
    val newAssetList: List<CirclePhotoResponse>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldAssetList.size
    }

    override fun getNewListSize(): Int {
        return newAssetList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldAssetList[oldItemPosition].photoDetail == newAssetList[newItemPosition].photoDetail
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldAssetList[oldItemPosition] == newAssetList[newItemPosition]
    }
}
