package com.example.myplayer.ui

import android.annotation.SuppressLint
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
import com.example.myplayer.adapter.MoviesAdapter
import com.example.myplayer.data.MoivesReceiveData
import com.example.myplayer.data.db.MoviesDatabase
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.databinding.FragmentMoviesBinding
import com.example.myplayer.viewmodels.MoviesViewModel
import com.example.myplayer.widget.ExitDialog
import com.example.myplayer.widget.PopDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import android.view.GestureDetector.SimpleOnGestureListener
import androidx.navigation.findNavController
import com.example.myplayer.MainActivity
import com.example.myplayer.adapter.CategoryAdapter
import com.example.myplayer.util.FileUtils
import java.lang.Exception


@AndroidEntryPoint
class MoivesFragment: Fragment(), ExitDialog.OnDialogButtonClickListener,
    GestureDetector.OnGestureListener {

    private lateinit var moviesBinding: FragmentMoviesBinding

    private var moviesJob: Job? = null

    private val seriesDetailViewModel: MoviesViewModel by viewModels()

    private lateinit var assetAdapter: MoviesAdapter

    private lateinit var categoryAdapter: CategoryAdapter

    //
    private lateinit var assetLayoutManager: LinearLayoutManager

    private lateinit var categoryLayoutManager: LinearLayoutManager

    private var receiveData: MoivesReceiveData? = null

    private val verticalMinistance = 100

    private val minVelocity = 10
    private var mGestureDetector: GestureDetector? = null

    var mNum: String = ""
    var mPwd: String = ""
    var mCoin: Int = 0
    var mId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moviesBinding = FragmentMoviesBinding.inflate(inflater, container, false)

        val sharedPref =
            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        mNum = sharedPref.getString("USERNAME","").toString()
        mPwd = sharedPref.getString("PASSWORD","").toString()
        mCoin = sharedPref.getInt("COIN",0)
        mId = sharedPref.getInt("ID",0)
        initGesture()
        return moviesBinding.root
    }

    private fun initGesture() {
        val mGestureDetector = GestureDetector(
            activity, this
        )
        val myOnTouchListener: MainActivity.MyOnTouchListener =
            object : MainActivity.MyOnTouchListener {
                override fun onTouch(ev: MotionEvent?): Boolean {
                    return mGestureDetector.onTouchEvent(ev)
                }
            }

        (activity as MainActivity?)
            ?.registerMyOnTouchListener(myOnTouchListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        subscribeUi()
    }

    private fun subscribeUi() {
        moviesBinding.releaseBtn.setOnClickListener { it ->
            it.findNavController().navigate(R.id.action_movies_fragment_to_uploadmovie_fragment)
        }
        assetAdapter = MoviesAdapter(requireContext())
        assetLayoutManager = GridLayoutManager(requireContext(), 2)
        assetLayoutManager.orientation = LinearLayoutManager.VERTICAL
        moviesBinding.moivesList.layoutManager = assetLayoutManager
        moviesBinding.moivesList.adapter = assetAdapter
        moviesBinding.moivesList.itemAnimator = null
        assetAdapter.setItemClickListener(object : MoviesAdapter.OnItemClickListener {
            override fun onItemClick(asset: MoviesEntity) {
                unLockDialog(requireActivity(),asset)
            }
        })

        categoryAdapter = CategoryAdapter(requireContext())
        categoryLayoutManager = LinearLayoutManager(requireContext())
        categoryLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        moviesBinding.categoryList.layoutManager = categoryLayoutManager
        moviesBinding.categoryList.adapter = categoryAdapter
        moviesBinding.categoryList.itemAnimator = null
        categoryAdapter.setItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(asset: String) {
                if (moviesBinding.categoryList.visibility == View.VISIBLE)
                    FileUtils.collapse(moviesBinding.categoryList) else FileUtils.expand(moviesBinding.categoryList)
            }
        })
        moviesBinding.menu.setOnClickListener{
            if (moviesBinding.categoryList.visibility == View.VISIBLE)
            FileUtils.collapse(moviesBinding.categoryList) else FileUtils.expand(moviesBinding.categoryList)
        }
        categoryAdapter.updateListItem()



        //
        getSeriesDetailData()
    }

    fun unLockDialog(activity: FragmentActivity, asset: MoviesEntity) {
        val exitDialog = ExitDialog(activity, "消耗1金币购买此内容？", this, asset)
        exitDialog.show()
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

    override fun onDestroy() {
        //
        moviesJob?.cancel()
        super.onDestroy()
    }

    override fun onDialogButtonClick(isPositive: Boolean, asset: MoviesEntity) {
        if (isPositive) {
            buyContent(asset)
        } else {

        }
    }

    private fun buyContent(asset:MoviesEntity) {
        if (mCoin < 1) {
            popDialog(requireActivity(), "金币不足,请充值")
            return
        }
        moviesJob?.cancel()
        mCoin--
        moviesJob = lifecycleScope.launch {
            seriesDetailViewModel.buy(mId, mNum, mPwd, mCoin)
                ?.observe(viewLifecycleOwner) { it ->
                    if (it.resultData) {
                        asset.num.let { it1 -> updateMovies(it1) }
                        navigateToPlayer(asset)
                    }
                }
        }
    }
    private fun navigateToPlayer(asset:MoviesEntity) {
        val bundle = Bundle().apply {
            putString("fromWhere", "BUY")
            putString("url", asset.video)
        }
        this.findNavController().navigate(R.id.action_movies_fragment_to_player_fragment, bundle)
    }

    private fun updateMovies(num: String) {
        moviesJob?.cancel()
        moviesJob = lifecycleScope.launch {

            // Init DB.
            val database = MoviesDatabase.getInstance(requireContext())
            database.moviesDao().updateTour(num, false)
        }
    }
    fun popDialog(activity: FragmentActivity, string: String) {
        val popDialog = PopDialog(activity, string)
        popDialog.show()
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
       return
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
       return false
    }

    override fun onLongPress(e: MotionEvent?) {
        return
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        try {
            if (e1!!.x - e2!!.x < -89) {
                android.util.Log.d("zwj" ,"Left")
                return true
            } else if ((e1.x - e2.x > 89) && Math.abs(e1.y - e2.y) < 20) {
                requireView().findNavController().navigate(R.id.action_movies_fragment_to_navigation_dashboard)
                android.util.Log.d("zwj" ,"GRight")
                return true
            }
        } catch (e: Exception) {
        }
        return false
    }

    override fun onDestroyView() {
        moviesJob?.cancel()
        super.onDestroyView()
    }

}