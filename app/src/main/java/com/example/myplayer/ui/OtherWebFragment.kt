package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myplayer.R
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.databinding.FragmentOtherBinding
import com.example.myplayer.viewmodels.MoviesViewModel
import com.example.myplayer.viewmodels.WebViewModel
import com.example.myplayer.widget.DetailDialog
import com.example.myplayer.widget.ExitDialog
import com.example.myplayer.widget.PopDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
@AndroidEntryPoint
class OtherWebFragment : Fragment(),ExitDialog.OnDialogButtonClickListenerForWeb {

    private lateinit var binding:FragmentOtherBinding
    private val seriesDetailViewModel: WebViewModel by viewModels()
    private var until_time: Long = 0
    private var until_time2: Long = 0
    var mTimer = Timer()
    var mNum: String = ""
    var mPwd: String = ""
    var mCoin: Int = 0

    private lateinit var num: String
    private lateinit var pwd: String

    var mId: Int = 0
    private var moviesJob: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPref =
            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        mNum = sharedPref.getString("USERNAME","").toString()
        mPwd = sharedPref.getString("PASSWORD","").toString()
        mCoin = sharedPref.getInt("COIN",0)
        mId = sharedPref.getInt("ID",0)
        binding = FragmentOtherBinding.inflate(inflater,container,false)
        binding.untilTime = until_time
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshInfo()
        subscriedUi()
    }

    private fun subscriedUi() {
        binding.back.setOnClickListener{
            this.findNavController().popBackStack()
        }
        binding.infoDesc.setOnClickListener{
            context?.resources?.let { it1 -> popDialog(requireActivity(), it1.getString(R.string.info_desc)) }
        }
        mTimer.schedule(object :TimerTask() {
            override fun run() {
                binding.untilTime = until_time
                binding.untilTime2 = until_time2
            }
        }, 1000*60, 1000*60)

        binding.enter.setOnClickListener{
            if (until_time > System.currentTimeMillis()/1000) {
                val bundle = Bundle().apply { putString("url", "https://www.m58yw.com/index/home.html")
                putLong("util_time",until_time)}
                this.findNavController().navigate(R.id.action_other_fragment_to_web_fragment,bundle)
            } else {
                popDialog2(requireActivity(),"时长不足，请前往购买")
            }
        }

        binding.buy.setOnClickListener{
            if (until_time > System.currentTimeMillis()/1000) {
                unLockDialog(requireActivity(),"还有剩余时间，是否继续购买",1)
            } else {
                unLockDialog(requireActivity(),"是否花费1金币购买1小时体验卡",1)
            }
        }

        binding.enter2.setOnClickListener{
            if (until_time2 > System.currentTimeMillis()/1000) {
                val bundle = Bundle().apply { putString("url", "https://www.baidu.com")
                    putLong("util_time",until_time2)}
                this.findNavController().navigate(R.id.action_other_fragment_to_web_fragment,bundle)
            } else {
                popDialog2(requireActivity(),"时长不足，请前往购买")
            }
        }

        binding.buy2.setOnClickListener{
            if (until_time2 > System.currentTimeMillis()/1000) {
                unLockDialog(requireActivity(),"还有剩余时间，是否继续购买",2)
            } else {
                unLockDialog(requireActivity(),"是否花费1金币购买1小时体验卡",2)
            }
        }
    }

    private fun refreshInfo() {
        val sharedPref =
            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        num = sharedPref.getString("USERNAME","").toString()
        pwd = sharedPref.getString("PASSWORD","").toString()
        loginNew(num, pwd)
    }

    private fun loginNew(num: String, pwd: String) {
        moviesJob?.cancel()
        moviesJob = lifecycleScope.launch {
            seriesDetailViewModel.user(num, pwd)
                .observe(viewLifecycleOwner) {
                    if (it != null && it.num != null) {
                        if (until_time < System.currentTimeMillis()/1000) {
                            until_time = 0
                        }
                        if (until_time2 < System.currentTimeMillis()/1000) {
                            until_time2 = 0
                        }
                        until_time = it.card1
                        until_time2 = it.card2
                        binding.untilTime = until_time
                        binding.untilTime2 = until_time2
                    } else {
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    fun popDialog(activity: FragmentActivity, string: String) {
        val popDialog = DetailDialog(activity, string)
        popDialog.show()
    }

    fun popDialog2(activity: FragmentActivity, string: String) {
        val popDialog = PopDialog(activity, string)
        popDialog.show()
    }

    fun unLockDialog(activity: FragmentActivity,tishi: String,index: Int) {
        val exitDialog = ExitDialog(activity, tishi, this,index)
        exitDialog.show()
    }


    private fun buyContent(tishi:String, index: Int) {
        android.util.Log.d("zwj " ,"adddaddd1")
        if (mCoin < 1) {
            popDialog(requireActivity(), tishi)
            return
        }
        android.util.Log.d("zwj " ,"adddaddd2")
        moviesJob?.cancel()
        mCoin--
        moviesJob = lifecycleScope.launch {
            seriesDetailViewModel.buy(mId, mNum, mPwd, mCoin)
                ?.observe(viewLifecycleOwner) { it ->
                    if (it.resultData) {
                        if (index == 1) {
                            if(until_time > System.currentTimeMillis()/1000) {
                                android.util.Log.d("zwj " ,"adddaddd time $until_time")
                                until_time += 60 * 60
                                android.util.Log.d("zwj " ,"adddaddd time1 $until_time")
                                binding.untilTime = until_time
                                android.util.Log.d("zwj " ,"adddaddd")
                            } else {
                                android.util.Log.d("zwj " ,"adddaddd222")
                                until_time = System.currentTimeMillis()/1000 + 60 * 60
                                binding.untilTime = until_time
                            }
                            updateCardInfo()
                        } else {
                            if(until_time2 > System.currentTimeMillis()/1000) {
                                android.util.Log.d("zwj " ,"adddaddd time $until_time")
                                until_time2 += 60 * 60
                                android.util.Log.d("zwj " ,"adddaddd time1 $until_time")
                                binding.untilTime2 = until_time2
                                android.util.Log.d("zwj " ,"adddaddd")
                            } else {
                                android.util.Log.d("zwj " ,"adddaddd222")
                                until_time2 = System.currentTimeMillis()/1000 + 60 * 60
                                binding.untilTime2 = until_time2
                            }
                            updateCardInfo()
                        }
                    }
                }
        }
    }
    private fun updateCardInfo() {
        moviesJob = lifecycleScope.launch {
            seriesDetailViewModel.updataCard(mId, mNum, until_time, until_time2)
                ?.observe(viewLifecycleOwner) { it ->
                    if (it.resultData) {

                    }
                }
        }
    }

    override fun onDialogButtonClick(isPositive: Boolean ,index:Int) {
        buyContent("金币不足,请充值",index)
    }
}