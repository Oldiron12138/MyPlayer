package com.example.myplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayer.R
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.reponse.PersonChat
import com.example.myplayer.databinding.ChatDialogLeftItemBinding
import com.example.myplayer.databinding.ChatDialogRightItemBinding
import com.example.myplayer.databinding.ListItemInfoBinding


class ChatAdapter(private val context: Context) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var assets: MutableList<PersonChat> = mutableListOf()

    /**
     * 是否是自己发送的消息
     *
     * @author cyf
     */
    interface IMsgViewType {
        companion object {
            const val IMVT_COM_MSG = 0 // 收到对方的消息
            const val IMVT_TO_MSG = 1 // 自己发送出去的消息
        }
    }
    fun updateListItem(datas: MutableList<PersonChat>) {
        val diffCallback = ChatDiffCallback(assets, datas)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        assets.clear()
        assets.addAll(datas)

        android.util.Log.d("zwj" ,"updateListItem")
        for (asset in datas) {
            android.util.Log.d("zwj" ,"isMe ${asset.isMe}" )
        }
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ChatViewHolder {
        return ChatViewHolder(
            ChatDialogRightItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatViewHolder, position: Int) {
        val asset = assets[position]
        holder.bind(asset, position)
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    inner class ChatViewHolder(
        val binding: ChatDialogRightItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {

        }
        fun bind(item: PersonChat, position: Int) {
            binding.apply {
                binding.tvname.text = item.content
                binding.tvChatMeMessage.text = item.content
                if (item.isMe) {
                    binding.me.visibility = View.VISIBLE
                    binding.firend.visibility = View.GONE
                } else {
                    binding.me.visibility = View.GONE
                    binding.firend.visibility = View.VISIBLE
                }
                executePendingBindings()
            }
        }
    }

private class ChatDiffCallback(
    val oldList: List<PersonChat>,
    val newList: List<PersonChat>
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