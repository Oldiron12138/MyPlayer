package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.R
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
import androidx.fragment.app.FragmentTransaction
import com.example.myplayer.widget.PopDialog
import com.example.myplayer.widget.ShareDialog
import android.view.WindowManager


@AndroidEntryPoint

class CircleFragment: Fragment(), ShareDialog.OnVideoClick, ShareDialog.OnPhotoClick{
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


        circleBinding.share.setOnClickListener{
            popDialog(requireActivity())
//            circleBinding.childContainer2.visibility = View.VISIBLE
//            val fragment = ShareDynamicFragment()
//            val ft: FragmentTransaction = childFragmentManager.beginTransaction()
//            ft.setCustomAnimations(
//                R.anim.slide_in_bottom, R.anim.nothing, R.anim.nothing,
//                R.anim.slide_out_bottom
//            )
//            ft.add(R.id.child_container2, fragment, ShareDynamicFragment.SHARE_TAG)
//                .commit()

        }
    }

    fun popDialog(activity: FragmentActivity) {
        val popDialog = ShareDialog(activity, this, this)
        val window: Window? = popDialog.window
        val layoutParams = window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = layoutParams
        if (window != null) {
            window.setGravity(Gravity.BOTTOM)
        }

        window.decorView.setPadding(0, 0, 0, 0)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
      //  layoutParams.horizontalMargin = 0f
        window.attributes = layoutParams
        popDialog.show()
    }

    private fun getInfoDetailData() {
        circleJob?.cancel()
        circleJob = lifecycleScope.launch {
            circleViewModel?.circleDetail()
                ?.observe(viewLifecycleOwner) { seriesDetailData ->
                    circleAdapter.setData(seriesDetailData)
                    circleAdapter.setItemClickListener(object :CircleAdapter.OnItemClickListener {
                        override fun onItemClick(photo:String) {
                            circleBinding.childContainer.visibility = View.VISIBLE
                            val fragment = ScanFragment(object: ScanFragment.OnFragmentClick{
                                override fun fragmentClick() {
                                    clearChildFragmentByTag(ScanFragment.SCAN_TAG)
                                    circleBinding.childContainer.visibility = View.GONE
                                }
                            })
                            val bundle = Bundle()
                            bundle.putString("url", photo)
                            fragment.arguments = bundle
                            childFragmentManager.beginTransaction()
                                .add(R.id.child_container, fragment, ScanFragment.SCAN_TAG)
                                .commit()
                        }
                    })
                    circleAdapter.setVideoClickListener(object :CircleAdapter.OnVideoClickListener{
                        override fun onVideoClick(video:String) {
                            circleBinding.childContainer.visibility = View.VISIBLE
                            val fragment = PlayerFragment()
                            val bundle = Bundle()
                            bundle.putString("url", video)
                            bundle.putString("fromWhere", "circle")
                            fragment.arguments = bundle
                            fragment.setOnBackClick(object: PlayerFragment.OnBackFromCircle{
                                override fun backFromCircle() {
                                    clearChildFragmentByTag(PlayerFragment.PLAYER_TAG)
                                    circleBinding.childContainer.visibility = View.GONE
                                }
                            })
                            childFragmentManager.beginTransaction()
                                .add(R.id.child_container, fragment, PlayerFragment.PLAYER_TAG)
                                .commit()
                        }
                    })
                }
        }
    }
    private fun clearChildFragmentByTag(tag: String) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = fragmentManager.findFragmentByTag(tag)
        fragment?.let {
            fragmentTransaction.remove(fragment)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    override fun onVideoClick() {
        circleBinding.childContainer.visibility = View.VISIBLE
        val fragment = SelectVideo()
        childFragmentManager.beginTransaction()
            .add(R.id.child_container, fragment, SelectVideo.SELECT_TAG)
            .commit()
        circleBinding.capture.visibility = View.GONE
    }

    override fun onPhotoClick() {
        circleBinding.childContainer.visibility = View.VISIBLE
        val fragment = SelectVideo()
        childFragmentManager.beginTransaction()
            .add(R.id.child_container, fragment, SelectVideo.SELECT_TAG)
            .commit()
        circleBinding.capture.visibility = View.GONE
    }
//    override fun onBackKeyPress() {
//        val fragment = ScanFragment()
//        childFragmentManager.beginTransaction().remove(fragment)
//    }
}