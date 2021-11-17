package com.example.myplayer.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.reponse.InfoResponse
import com.example.myplayer.databinding.FragmentDetailBinding
import com.example.myplayer.databinding.FragmentPlayerBinding
import com.example.myplayer.manager.ExoPlayerManager
import com.example.myplayer.viewmodels.InfoViewModel
import com.example.myplayer.viewmodels.MoviesViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class DetailFragment : Fragment(){
    private lateinit var detailBinding: FragmentDetailBinding
    private var detailJob: Job? = null
    lateinit var detailResponse: InfoResponse
    lateinit var url: String
    var index : Int =0
    var current: String = "乌鲁木齐"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailBinding = FragmentDetailBinding.inflate(inflater, container, false)
        return detailBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getInt("index") != null){
            index = arguments?.getInt("index")!!
            current = arguments?.getString("current")!!
        } else {
            index = 0
        }
        getInfoDetailData()
    }

    override fun onResume() {
        super.onResume()

    }

    private fun getInfoDetailData() {
        detailJob?.cancel()
        detailJob = lifecycleScope.launch {
            infoViewModel?.seriesDetail
                ?.observe(viewLifecycleOwner) { seriesDetailData ->
                    val newInfoList: MutableList<InfoEntity> = mutableListOf()
                    repeat(seriesDetailData.size) { i ->
                        if (seriesDetailData[i].city == current) {
                            newInfoList.add(InfoEntity(seriesDetailData[i].title, seriesDetailData[i].city, seriesDetailData[i].desc, seriesDetailData[i].street, seriesDetailData[i].phone, seriesDetailData[i].price,seriesDetailData[i].url))
                        }
                    }
                    detailBinding.info = newInfoList.get(index)
                }
        }
    }
    companion object {
        private var infoViewModel: InfoViewModel? = null
        fun setInfoViewModel(viewModel: InfoViewModel?) {
            infoViewModel = viewModel
        }
    }

}