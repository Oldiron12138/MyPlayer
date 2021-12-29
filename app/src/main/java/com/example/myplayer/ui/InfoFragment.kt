package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.R
import com.example.myplayer.adapter.InfoAdapter
import com.example.myplayer.adapter.MoviesAdapter
import com.example.myplayer.adapter.bindInfoImage
import com.example.myplayer.data.MoivesReceiveData
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.data.reponse.InfoResponse
import com.example.myplayer.databinding.FragmentInfoBinding
import com.example.myplayer.databinding.FragmentMoviesBinding
import com.example.myplayer.ui.DetailFragment.Companion.setInfoViewModel
import com.example.myplayer.viewmodels.InfoViewModel
import com.example.myplayer.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InfoFragment: Fragment() {
    private lateinit var moviesBinding: FragmentInfoBinding

    private var infoJob: Job? = null

    private val infoViewModel: InfoViewModel by viewModels()

    private lateinit var infoAdapter: InfoAdapter

    //
    private lateinit var assetLayoutManager: LinearLayoutManager

    private var current: String = "乌鲁木齐"
    private var defaultCurrent: String = "AAAA"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moviesBinding = FragmentInfoBinding.inflate(inflater, container, false)
        val sharedPref1 =
            requireContext().getSharedPreferences("CITY_CACHE", Context.MODE_PRIVATE)
        defaultCurrent = sharedPref1.getString("CITY", "乌鲁木齐").toString()

        val sharedPref =
            requireContext().getSharedPreferences("CURRENT_CITY", Context.MODE_PRIVATE)
        current = sharedPref.getString("CURRENT",defaultCurrent).toString()
        moviesBinding.current = current
        return moviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref1 =
            requireContext().getSharedPreferences("CITY_CACHE", Context.MODE_PRIVATE)
        defaultCurrent = sharedPref1.getString("CITY", "乌鲁木齐").toString()

        val sharedPref =
            requireContext().getSharedPreferences("CURRENT_CITY", Context.MODE_PRIVATE)
        current = sharedPref.getString("CURRENT",defaultCurrent).toString()
        if (arguments?.getString("city") != null){
            current = arguments?.getString("city")!!

            android.util.Log.d("zwj" ,"current $current")
            sharedPref.edit().putString("CURRENT", current).apply()
        } else {
            current = current
        }
//        //
        moviesBinding.current = current
        registerListener()
        subscribeUi()
        setInfoViewModel(infoViewModel)
    }

    private fun registerListener() {
        moviesBinding.setClickListener {
            it.findNavController().navigate(R.id.action_navigation_dashboard_to_city_fragment)
        }
        moviesBinding.releaseBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_dashboard_to_release_fragment)
        }
        moviesBinding.setClickListener1 {
            val sharedPref =
                requireContext().getSharedPreferences("CURRENT_CITY", Context.MODE_PRIVATE)
            moviesBinding.current = defaultCurrent
            current = defaultCurrent
            sharedPref.edit().putString("CURRENT", current).apply()
            subscribeUi()
        }
    }

    private fun subscribeUi() {

        infoAdapter = InfoAdapter(requireContext())
        assetLayoutManager = LinearLayoutManager(requireContext())
        assetLayoutManager.orientation = LinearLayoutManager.VERTICAL
        moviesBinding.infoList.layoutManager = assetLayoutManager
        moviesBinding.infoList.adapter = infoAdapter
        moviesBinding.infoList.itemAnimator = null

        //
        getInfoDetailData()
    }

    private fun getInfoDetailData() {
        infoJob?.cancel()
        infoJob = lifecycleScope.launch {
            infoViewModel.seriesDetail
                ?.observe(viewLifecycleOwner) { seriesDetailData ->
                    val newInfoList: MutableList<InfoEntity> = mutableListOf()
                    repeat(seriesDetailData.size) { i ->
                        if (seriesDetailData[i].city == current) {
                            newInfoList.add(InfoEntity(seriesDetailData[i].title, seriesDetailData[i].city, seriesDetailData[i].desc, seriesDetailData[i].street, seriesDetailData[i].phone, seriesDetailData[i].price,seriesDetailData[i].url,seriesDetailData[i].lock))
                        }
                    }
                    infoAdapter.updateListItem(newInfoList, current)
                    if (newInfoList.isEmpty()) {
                        moviesBinding.empty.visibility = View.VISIBLE
                    } else {
                        moviesBinding.empty.visibility = View.GONE
                    }
                }
        }
    }
    override fun onDestroyView() {
        infoJob?.cancel()
        super.onDestroyView()
    }
}