package com.example.myplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.adapter.CharsAdapter
import com.example.myplayer.adapter.CityAdapter
import com.example.myplayer.databinding.FragmentCityBinding
import com.example.myplayer.util.FileUtils
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
    private lateinit var charAdapter: CharsAdapter
    private var max:Int = 0

    val chars :ArrayList<String> = arrayListOf("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")

    //
    private lateinit var assetLayoutManager: LinearLayoutManager
    private lateinit var assetLayoutManager2: LinearLayoutManager
    var cityList: ArrayList<String> = ArrayList()

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
        max = FileUtils.getPingMuSize(requireContext()) / 50
        val size = FileUtils.getPingMuSize(requireContext())

        android.util.Log.d("zwj" ,"current Click $size")
        infoAdapter = CityAdapter(requireContext())
        assetLayoutManager = LinearLayoutManager(requireContext())
        assetLayoutManager.orientation = LinearLayoutManager.VERTICAL
        cityBinding.cityList.layoutManager = assetLayoutManager
        cityBinding.cityList.adapter = infoAdapter
        cityBinding.cityList.itemAnimator = null
        getInfoDetailData()
        //
        charAdapter = CharsAdapter(requireContext())
        assetLayoutManager2 = LinearLayoutManager(requireContext())
        assetLayoutManager2.orientation = LinearLayoutManager.VERTICAL
        cityBinding.charList.layoutManager = assetLayoutManager2
        cityBinding.charList.adapter = charAdapter
        cityBinding.charList.itemAnimator = null
        charAdapter.updateListItem(chars)
        charAdapter.setItemClickListener(object :CharsAdapter.OnItemClickListener{
            override fun onItemClick(zimu: String) {
                val index: Int = cityList.indexOf(zimu)
                val indexCurrent= assetLayoutManager.findFirstVisibleItemPosition()
                if(index> indexCurrent) {
                    assetLayoutManager.scrollToPosition(index+max)
                } else {
                    assetLayoutManager.scrollToPosition(index)
                }

                android.util.Log.d("zwj" ,"current Click $zimu $index $max")
                //assetLayoutManager.scrollToPosition(index+max)
            }

        })
    }

    private fun getInfoDetailData() {
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