package com.example.myplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.adapter.CityAdapter
import com.example.myplayer.databinding.FragmentCityBinding
import com.example.myplayer.viewmodels.CityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
@AndroidEntryPoint
class CityFragment : Fragment() {
    private lateinit var cityBinding: FragmentCityBinding

    private var cityJob: Job? = null

    private val cityViewModel: CityViewModel by viewModels()

    private lateinit var infoAdapter: CityAdapter

    //
    private lateinit var assetLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cityBinding = FragmentCityBinding.inflate(inflater, container, false)
        return cityBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        subscribeUi()
    }

    private fun subscribeUi() {
        infoAdapter = CityAdapter(requireContext())
        assetLayoutManager = LinearLayoutManager(requireContext())
        assetLayoutManager.orientation = LinearLayoutManager.VERTICAL
        cityBinding.cityList.layoutManager = assetLayoutManager
        cityBinding.cityList.adapter = infoAdapter
        cityBinding.cityList.itemAnimator = null

        //
        getInfoDetailData()
    }

    private fun getInfoDetailData() {
        var cityList: ArrayList<String> = ArrayList()
        cityJob?.cancel()
        cityJob = lifecycleScope.launch {
            cityViewModel.cityAdd()
                ?.observe(viewLifecycleOwner) { seriesDetailData ->

                    for (seriesDetailDatum in seriesDetailData) {
                        cityList.add(seriesDetailDatum.title)
                        cityList.addAll(seriesDetailDatum.city)
                    }
                    if (cityList != null) {
                        infoAdapter.updateListItem(cityList)
                    }
                }
        }
    }
}