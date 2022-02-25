package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplayer.R
import com.example.myplayer.adapter.InfoAdapter
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.databinding.FragmentInfoBinding
import com.example.myplayer.ui.DetailFragment.Companion.setInfoViewModel
import com.example.myplayer.viewmodels.InfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.myplayer.viewmodels.SplashViewModel
import com.example.myplayer.widget.MyScrollView
import kotlinx.coroutines.delay


@AndroidEntryPoint
class InfoFragment: Fragment(),MyScrollView.OnTouchUpListener {
    private lateinit var moviesBinding: FragmentInfoBinding

    private var infoJob: Job? = null

    private val infoViewModel: InfoViewModel by viewModels()
    private val splashViewModel: SplashViewModel by viewModels()

    private lateinit var infoAdapter: InfoAdapter
    private lateinit var myScrollView : MyScrollView

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
        fragmentManager1 = childFragmentManager
        binding= moviesBinding
        myScrollView = binding.scoller
        myScrollView.setTouchUpListener(this)
        return moviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        view.setClickable(true)
        val sharedPref1 =
            requireContext().getSharedPreferences("CITY_CACHE", Context.MODE_PRIVATE)
        defaultCurrent = sharedPref1.getString("CITY", "乌鲁木齐").toString()

        val sharedPref =
            requireContext().getSharedPreferences("CURRENT_CITY", Context.MODE_PRIVATE)
        current = sharedPref.getString("CURRENT",defaultCurrent).toString()
        if (arguments?.getString("city") != null){
            current = arguments?.getString("city")!!

            sharedPref.edit().putString("CURRENT", current).apply()
        } else {
            current = current
        }
//        //
        moviesBinding.current = current
        registerListener()
        subscribeUi()
        setInfoViewModel(infoViewModel)
        moviesBinding.root.setFocusableInTouchMode(true)
        moviesBinding.root.requestFocus()
        moviesBinding.root.setOnKeyListener(object :View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                when (keyCode) {
                    KeyEvent.KEYCODE_BACK -> {
                        findNavController().navigate(R.id.action_navigation_dashboard_to_movies_fragment)
                        return true
                    }
                    else -> return false
                }
            }
        })


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
        infoAdapter.setItemClickListener(object :InfoAdapter.OnItemClickListener{
            override fun onItemClick(postion:Int,current:String) {
                moviesBinding.childContainer.visibility = View.VISIBLE
                val fragment = DetailFragment()
                val bundle = Bundle()

                bundle.putInt("index", postion)
                bundle.putString("current", current)

                fragment.arguments = bundle
                childFragmentManager.beginTransaction()
                    .add(R.id.child_container, fragment, DetailFragment.DETAIL_TAG)
                    .commit()
            }

        })

        assetLayoutManager = LinearLayoutManager(requireContext())
        assetLayoutManager.orientation = LinearLayoutManager.VERTICAL
        moviesBinding.infoList.layoutManager = assetLayoutManager
        moviesBinding.infoList.adapter = infoAdapter
        moviesBinding.infoList.itemAnimator = null

        moviesBinding.infoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager =  moviesBinding.infoList.layoutManager
                if(moviesBinding.infoList.canScrollVertically(1)){

                }else {
                    myScrollView.setNeed(false)
                    moviesBinding.loading.visibility = View.GONE
                    //moviesBinding.loadingP.visibility = View.GONE
                }
                if(moviesBinding.infoList.canScrollVertically(-1)){
                   // Log.i(TAG, "direction -1: true");
                    moviesBinding.loading.visibility = View.INVISIBLE
                    //moviesBinding.loadingP.visibility = View.GONE
                    //myScrollView.setNeed(false)
                }else {
                    //moviesBinding.loadingP.visibility = View.VISIBLE
                    moviesBinding.loading.visibility = View.VISIBLE
                    myScrollView.setNeed(true)
                }
            }
        })



        //
        getInfoDetailData()

        val mState: RecyclerView.State = RecyclerView.State()
        moviesBinding.titleAll.setOnClickListener {
            android.util.Log.d("zwj" ,"titleAll press")
            assetLayoutManager.smoothScrollToPosition(moviesBinding.infoList, mState ,0)
        }

    }

    private fun getInfoDetailData() {
        infoJob?.cancel()
        infoJob = lifecycleScope.launch {
            infoViewModel.seriesDetail
                ?.observe(viewLifecycleOwner) { seriesDetailData ->
                    val newInfoList: MutableList<InfoEntity> = mutableListOf()
                    repeat(seriesDetailData.size) { i ->
                        if (seriesDetailData[i].city == current) {
                            newInfoList.add(InfoEntity(seriesDetailData[i].num,seriesDetailData[i].title, seriesDetailData[i].city, seriesDetailData[i].desc, seriesDetailData[i].street, seriesDetailData[i].phone, seriesDetailData[i].price,seriesDetailData[i].url,seriesDetailData[i].lock))
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

    companion object {
        lateinit var fragmentManager1: FragmentManager
        lateinit var binding: FragmentInfoBinding
        public fun clearChildFragmentByTag(tag: String) {
            binding.childContainer.visibility = View.GONE
            val fragmentTransaction = fragmentManager1.beginTransaction()
            val fragment = fragmentManager1.findFragmentByTag(tag)
            fragment?.let {
                fragmentTransaction.remove(fragment)
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    override fun onTouchUp() {
        val rotateAnimation = RotateAnimation(0f,360f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 1000
        val lin = LinearInterpolator()
        rotateAnimation.interpolator = lin
        rotateAnimation.repeatCount = -1
        moviesBinding.loading.startAnimation(rotateAnimation)

        infoJob?.cancel()
        infoJob = lifecycleScope.launch {
            delay(1000)
            if(splashViewModel.infos(requireContext())) {
                moviesBinding.loading.animation.cancel()
                moviesBinding.loading.visibility = View.GONE
               // myScrollView.rollBack()
               // myScrollView.setNeed(false)
            }
        }

    }

}