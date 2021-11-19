package com.example.myplayer.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.adapter.MoviesAdapter
import com.example.myplayer.data.MoivesReceiveData
import com.example.myplayer.data.db.MoviesDatabase
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.databinding.FragmentMoviesBinding
import com.example.myplayer.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MoivesFragment: Fragment() {

    private lateinit var moviesBinding: FragmentMoviesBinding

    private var moviesJob: Job? = null

    private val seriesDetailViewModel: MoviesViewModel by viewModels()

    private lateinit var assetAdapter: MoviesAdapter

    //
    private lateinit var assetLayoutManager: LinearLayoutManager

    private var receiveData: MoivesReceiveData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moviesBinding = FragmentMoviesBinding.inflate(inflater, container, false)
        return moviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        subscribeUi()
    }

    private fun subscribeUi() {
        assetAdapter = MoviesAdapter(requireContext())
        assetLayoutManager = GridLayoutManager(requireContext(), 2)
        assetLayoutManager.orientation = LinearLayoutManager.VERTICAL
        moviesBinding.moivesList.layoutManager = assetLayoutManager
        moviesBinding.moivesList.adapter = assetAdapter
        moviesBinding.moivesList.itemAnimator = null
        assetAdapter.setItemClickListener(object : MoviesAdapter.OnItemClickListener {
            override fun onItemClick(asset: MoviesEntity) {
            }
        })
        //
        getSeriesDetailData()
    }

    private fun getSeriesDetailData() {
        android.util.Log.d("zwj " ,"getSeriesDetailData")
        moviesJob?.cancel()
        moviesJob = lifecycleScope.launch {
            seriesDetailViewModel.seriesDetail
                ?.observe(viewLifecycleOwner) { seriesDetailData ->
                    android.util.Log.d("zwj " ,"getSeriesDetailData ${seriesDetailData.size}")
                    assetAdapter.updateListItem(seriesDetailData)
                }
            //seriesDetailViewModel.devices()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        //
        moviesJob?.cancel()
        super.onDestroy()
    }
}