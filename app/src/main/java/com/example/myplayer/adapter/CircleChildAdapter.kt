package com.example.myplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayer.data.reponse.CirclePhotoResponse
import com.example.myplayer.databinding.ListCirclePhotoBinding
import android.util.DisplayMetrics




class CircleChildAdapter(context: Context) : RecyclerView.Adapter<CircleChildAdapter.CircleChildViewHolder>() {

    private val assets: MutableList<CirclePhotoResponse> = mutableListOf()

    private val context: Context = context
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

        var dm = DisplayMetrics()
        dm = context.resources.displayMetrics

        val density = dm.density // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）

        val densityDPI = dm.densityDpi // 屏幕密度（每寸像素：120/160/240/320）

        val xdpi = dm.xdpi
        val ydpi = dm.ydpi


        val screenWidth = dm.widthPixels // 屏幕宽（像素，如：480px）
        val screenHeight = dm.heightPixels

        android.util.Log.d("zwjSize" ,"width $screenWidth")
        android.util.Log.d("zwjSize" ,"height $screenHeight")
        val parm = holder.itemView.layoutParams
        if (itemCount == 1) {
            parm.width = (screenWidth - px2dip(1,density)) / 2
            parm.height = parm.width
        } else if (itemCount == 2 || itemCount == 4) {
            parm.width = (screenWidth - px2dip(3, density)) / 2
            parm.height = parm.width
        } else {
            parm.width = (screenWidth - px2dip(3, density)) / 3
            parm.height = parm.width
        }

        assets[position].let { asset ->
            holder.bind(asset, itemClickListener)
//            if (isItemClick) {
//                itemClickListener?.onItemClick(asset.photoDetail)
//            }
        }
    }

    fun px2dip(count : Int ,density: Float): Int {
        return ((40 + count*15)  * density + 0.5f).toInt()
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    class CircleChildViewHolder(
        val binding: ListCirclePhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CirclePhotoResponse, itemClickListener: OnItemClickListener?) {
            binding.apply {
                asset = item
                binding.imageView.setOnClickListener {
                    itemClickListener?.onItemClick(item.photoDetail)
                }
                executePendingBindings()
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(photo: String)
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
