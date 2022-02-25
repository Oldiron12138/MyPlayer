package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myplayer.MainActivity
import com.example.myplayer.MainApplication
import com.example.myplayer.R
import com.example.myplayer.adapter.ViewPagerAdapter
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
import java.text.FieldPosition
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
    var position: Int = 0
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
        view.setClickable(true)
        if (arguments?.getInt("index") != null){
            index = arguments?.getInt("index")!!
            current = arguments?.getString("current")!!
        } else {
            index = 0
        }
        getInfoDetailData()
        subscribeUi()

    }
    public fun clearChildFragmentByTag(tag: String) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = fragmentManager.findFragmentByTag(tag)
        fragment?.let {
            fragmentTransaction.remove(fragment)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }
    private fun subscribeUi() {



        detailBinding.playerBack.setOnClickListener{
            //this.findNavController().navigate(R.id.action_info_detail_to_navigation_dashboard)
            InfoFragment.clearChildFragmentByTag(DETAIL_TAG)
        }
        val animation: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_scale_in)
        detailBinding.anim.startAnimation(animation)
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
                            newInfoList.add(InfoEntity(seriesDetailData[i].num,seriesDetailData[i].title, seriesDetailData[i].city, seriesDetailData[i].desc, seriesDetailData[i].street, seriesDetailData[i].phone, seriesDetailData[i].price,seriesDetailData[i].url,seriesDetailData[i].lock))
                        }
                    }
                    detailBinding.info = newInfoList.get(index)
                    info = newInfoList.get(index)
                    android.util.Log.d("zwj" ,"detail ${info.lock}")
                    detailBinding.setClickListener {
                        unLockDialog(requireActivity(),info,index)
                    }
                    val viewPager2: ViewPager2 = detailBinding.imageView
                    val adapter = ViewPagerAdapter(info.url,requireContext(),object :ScanFragment.OnFragmentClick{
                        override fun fragmentClick(position: Int) {
                            detailBinding.childContainer.visibility = View.VISIBLE
                            val fragment = ScanFragment(object: ScanFragment.OnFragmentClick{
                                override fun fragmentClick(position: Int) {
                                    clearChildFragmentByTag(ScanFragment.SCAN_TAG)
                                    detailBinding.childContainer.visibility = View.GONE
                                }
                            })
                            val bundle = Bundle()
                            var photoString  = arrayListOf<String>()
                            for (it in info.url) {
                                photoString.add(it)
                            }
                            bundle.putStringArrayList("url", photoString)
                            bundle.putInt("position", position)
                            fragment.arguments = bundle
                            childFragmentManager.beginTransaction()
                                .add(R.id.child_container, fragment, ScanFragment.SCAN_TAG)
                                .commit()
                        }
                    })
                    viewPager2.adapter = adapter
                    viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    if (position != null) {
                        viewPager2.setCurrentItem(position,false)
                    }
                }
        }
    }

    private fun buyContent(info: InfoEntity, index: Int?) {
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
                        info.title.let { it1 -> updateMovies(it1, info.num) }
                    }
                }
        }
    }

    private fun updateMovies(title: String,index: Int?) {
        detailJob?.cancel()
        detailJob = lifecycleScope.launch {
            // Init DB.
            val database = InfoDatabase.getInstance(requireContext())
            database.infoDao().updateTour(title, false)
        }
        val sharedPref =
            MainApplication.applicationContext?.getSharedPreferences("UNLOCK", Context.MODE_PRIVATE)
        val unLock: MutableSet<String>? = sharedPref?.getStringSet("UNLOCK_INFO", mutableSetOf("unlock"))
        //sharedPref?.edit()?.clear()?.commit()

        var unLock1: MutableSet<String>? = mutableSetOf()
        if (unLock1 != null) {
            unLock1.clear()
        }
        unLock!!.forEach {
            unLock1!!.add(it)
        }
        unLock1!!.add(index.toString())
        sharedPref.edit().putStringSet("UNLOCK_INFO", unLock1).apply()
    }
    fun popDialog(activity: FragmentActivity, string: String) {
        val popDialog = PopDialog(activity, string)
        popDialog.show()
    }

    fun unLockDialog(activity: FragmentActivity, info: InfoEntity, index: Int) {
        val exitDialog = ExitDialog(activity, "消耗1金币购买此内容？", this, info,index)
        exitDialog.show()
    }

    interface OnBackClick {
        fun fragmentClick(position: Int)
    }

    companion object {
        private var infoViewModel: InfoViewModel? = null
        fun setInfoViewModel(viewModel: InfoViewModel?) {
            infoViewModel = viewModel
        }
        const val DETAIL_TAG = "DETAIL_TAG"
    }

    override fun onDialogButtonClickForInfo(isPositive: Boolean, info: InfoEntity ,index: Int?) {
        if (isPositive) {
            buyContent(info, index)
        } else {

        }
    }

}