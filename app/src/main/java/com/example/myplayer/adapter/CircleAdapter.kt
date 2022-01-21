package com.example.myplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayer.data.reponse.CircleResponse
import com.example.myplayer.databinding.ListItemCircleBinding
import com.example.myplayer.data.reponse.CirclePhotoResponse


class CircleAdapter(private val context: Context) :
    RecyclerView.Adapter<CircleAdapter.CircleViewHolder>() {

    private var circleDates: List<CircleResponse> = listOf()
    private var isItemClick: Boolean = false
    private lateinit var itemClickListener : OnItemClickListener
    private lateinit var videoClickListener : OnVideoClickListener

    interface IMsgViewType {
        companion object {
            const val IMVT_COM_MSG = 0 // 收到对方的消息
            const val IMVT_TO_MSG = 1 // 自己发送出去的消息
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircleAdapter.CircleViewHolder {
        return CircleViewHolder(
            ListItemCircleBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    fun setData(datas: List<CircleResponse>) {
        circleDates = datas
        //isItemClick = false
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: OnItemClickListener) {
        isItemClick = true
        itemClickListener = listener
        notifyDataSetChanged()
    }

    fun setVideoClickListener(listener: OnVideoClickListener) {
        isItemClick = true
        videoClickListener = listener
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CircleAdapter.CircleViewHolder, position: Int) {
        holder.bind(circleDates[position])

        //
        val childAdapter = CircleChildAdapter(context)
        if (circleDates[position].photo?.size?:0  == 4 || circleDates[position].photo?.size?:0 ==2) {
            val childLayoutManager = GridLayoutManager(context, 2)
            childLayoutManager.orientation = LinearLayoutManager.VERTICAL
            holder.binding.photoList.layoutManager = childLayoutManager
        } else {
            val childLayoutManager = GridLayoutManager(context, 3)
            childLayoutManager.orientation = LinearLayoutManager.VERTICAL
            holder.binding.photoList.layoutManager = childLayoutManager
        }
        holder.binding.photoList.adapter = childAdapter

        //
        circleDates[position].photo?.let { assets ->
            childAdapter.updateChild(assets)
//
                if (isItemClick) {
                    childAdapter.setItemClickListener(object :
                        CircleChildAdapter.OnItemClickListener {
                        override fun onItemClick(photo : MutableList<CirclePhotoResponse>, position: Int) {
                            itemClickListener?.onItemClick(photo,position)
                        }
                    })
                }
        }

//        childAdapter.setItemClickListener(object : CircleChildAdapter.OnItemClickListener {
//            override fun onItemClick() {
//                unLockDialog(requireActivity(),asset)
//            }
//        })
    }

    override fun getItemCount(): Int {
        return circleDates.size
    }

    inner class CircleViewHolder(
        val binding: ListItemCircleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {

        }
        fun bind(item: CircleResponse) {
            binding.apply {
                circles = item
                binding.playback.setOnClickListener{
                    item.video?.let { it1 -> videoClickListener.onVideoClick(it1) }
                }
                executePendingBindings()
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(photo:MutableList<CirclePhotoResponse>, position: Int)
    }

    interface OnVideoClickListener {
        fun onVideoClick(video:String)
    }


    private class CircleDiffCallback(
        val oldList: List<CircleResponse>,
        val newList: List<CircleResponse>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].content == newList[newItemPosition].content    }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}