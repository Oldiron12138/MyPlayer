package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myplayer.R
import com.example.myplayer.data.db.InfoDatabase
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.db.MoviesDatabase
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.data.reponse.InfoResponse
import com.example.myplayer.databinding.FragmentDetailBinding
import com.example.myplayer.databinding.FragmentPlayerBinding
import com.example.myplayer.manager.ExoPlayerManager
import com.example.myplayer.viewmodels.DetailViewModel
import com.example.myplayer.viewmodels.InfoViewModel
import com.example.myplayer.viewmodels.MoviesViewModel
import com.example.myplayer.widget.ExitDialog
import com.example.myplayer.widget.PopDialog
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class DetailFragment : Fragment(), ExitDialog.OnDialogButtonClickListenerForInfo{
    private lateinit var detailBinding: FragmentDetailBinding
    private var detailJob: Job? = null
    lateinit var detailResponse: InfoResponse
    lateinit var url: String
    var index : Int =0
    var current: String = "乌鲁木齐"
    var mNum: String = ""
    var mPwd: String = ""
    var mCoin: Int = 0
    var mId: Int = 0
    lateinit var info : InfoEntity
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailBinding = FragmentDetailBinding.inflate(inflater, container, false)
        val sharedPref =
            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        mNum = sharedPref.getString("USERNAME","").toString()
        mPwd = sharedPref.getString("PASSWORD","").toString()
        mCoin = sharedPref.getInt("COIN",0)
        mId = sharedPref.getInt("ID",0)
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
                            newInfoList.add(InfoEntity(seriesDetailData[i].title, seriesDetailData[i].city, seriesDetailData[i].desc, seriesDetailData[i].street, seriesDetailData[i].phone, seriesDetailData[i].price,seriesDetailData[i].url,seriesDetailData[i].lock))
                        }
                    }
                    detailBinding.info = newInfoList.get(index)
                    info = newInfoList.get(index)
                    detailBinding.setClickListener {
                        unLockDialog(requireActivity(),info)
                    }
                }
        }
    }

    private fun buyContent(info: InfoEntity) {
        if (mCoin < 1) {
            popDialog(requireActivity(), "金币不足,请充值")
            return
        }
        detailJob?.cancel()
        mCoin--
        detailJob = lifecycleScope.launch {
            detailViewModel.buy(mId, mNum, mPwd, mCoin)
                ?.observe(viewLifecycleOwner) { it ->
                    if (it.resultData) {
                        info.title.let { it1 -> updateMovies(it1) }

                    }
                }
        }
    }

    private fun updateMovies(title: String) {
        detailJob?.cancel()
        detailJob = lifecycleScope.launch {
            android.util.Log.d("zwj" ,"updateMovies222")

            // Init DB.
            val database = InfoDatabase.getInstance(requireContext())
            database.infoDao().updateTour(title, false)
        }
    }
    fun popDialog(activity: FragmentActivity, string: String) {
        val popDialog = PopDialog(activity, string)
        popDialog.show()
    }

    fun unLockDialog(activity: FragmentActivity, info: InfoEntity) {
        val exitDialog = ExitDialog(activity, "消耗1金币购买此内容？", this, info)
        exitDialog.show()
    }

    companion object {
        private var infoViewModel: InfoViewModel? = null
        fun setInfoViewModel(viewModel: InfoViewModel?) {
            infoViewModel = viewModel
        }
    }

    override fun onDialogButtonClickForInfo(isPositive: Boolean, info: InfoEntity) {
        if (isPositive) {
            buyContent(info)
        } else {

        }
    }

}