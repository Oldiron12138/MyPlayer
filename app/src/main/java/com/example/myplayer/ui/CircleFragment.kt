package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.adapter.CircleAdapter
import com.example.myplayer.adapter.InfoAdapter
import com.example.myplayer.adapter.MoviesAdapter
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.reponse.CircleResponse
import com.example.myplayer.databinding.FragmentCircleBinding
import com.example.myplayer.databinding.FragmentInfoBinding
import com.example.myplayer.viewmodels.CircleViewModel
import com.example.myplayer.viewmodels.InfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
@AndroidEntryPoint
class CircleFragment: Fragment() {
    private lateinit var circleBinding: FragmentCircleBinding

    private var circleJob: Job? = null

    private val circleViewModel: CircleViewModel by viewModels()

    private lateinit var circleAdapter: CircleAdapter

    //
    private lateinit var assetLayoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        circleBinding = FragmentCircleBinding.inflate(inflater, container, false)
        return circleBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // registerListener()
        subscribeUi()
        getInfoDetailData()
    }

    private fun subscribeUi() {
        circleAdapter = CircleAdapter(requireContext())
        assetLayoutManager = LinearLayoutManager(requireContext())
        assetLayoutManager.orientation = LinearLayoutManager.VERTICAL
        circleBinding.lvCircle.layoutManager = assetLayoutManager
        circleBinding.lvCircle.adapter = circleAdapter
        circleBinding.lvCircle.itemAnimator = null
    }

    private fun getInfoDetailData() {
        circleJob?.cancel()
        circleJob = lifecycleScope.launch {
            circleViewModel?.circleDetail()
                ?.observe(viewLifecycleOwner) { seriesDetailData ->
                    circleAdapter.setData(seriesDetailData)
                }
        }
    }
}