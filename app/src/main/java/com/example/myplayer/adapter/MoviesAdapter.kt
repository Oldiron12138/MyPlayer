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
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.data.reponse.MoviesResponse
import com.example.myplayer.databinding.ListItemMoviesBinding
import timber.log.Timber

class MoviesAdapter(
    val context: Context
) : RecyclerView.Adapter<MoviesAdapter.MoivesViewHolder>() {

    //
    private val assets: MutableList<MoviesEntity> = mutableListOf()

    //
    fun updateListItem(datas: List<MoviesEntity>) {
        val diffCallback = SeriesDetailAssetDiffCallback(assets, datas)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        assets.clear()
        assets.addAll(datas)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoivesViewHolder {
        return MoivesViewHolder(
            ListItemMoviesBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MoivesViewHolder, position: Int) {
        val asset = assets[position]
        holder.bind(asset, position)
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    private fun navigateToPlayer(
        view: View, asset: MoviesEntity
    ) {
        val bundle = Bundle().apply { putString("url", asset.video) }
        view.findNavController().navigate(R.id.action_movies_fragment_to_player_fragment, bundle)
    }

    inner class MoivesViewHolder(
        val binding: ListItemMoviesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.asset?.let {

                        asset ->
                    navigateToPlayer(it, asset)
                }
            }
        }

        fun bind(item: MoviesEntity, position: Int) {
            binding.apply {
                asset = item
                executePendingBindings()
            }
        }
    }
}

private class SeriesDetailAssetDiffCallback(
    val oldList: List<MoviesEntity>,
    val newList: List<MoviesEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].num == newList[newItemPosition].num
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}