package com.example.myplayer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myplayer.R
import com.example.myplayer.data.db.MoviesDatabase
import com.example.myplayer.databinding.FragmentBuyBinding
import com.example.myplayer.viewmodels.BuyContentViewModel
import com.example.myplayer.widget.PopDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BuyContentDialogFragment: DialogFragment() {
    private lateinit var moviesBinding: FragmentBuyBinding

    private var infoJob: Job? = null

    private var infoJob2: Job? = null

    private val buyViewModel: BuyContentViewModel by viewModels()

    var mNum: String = ""
    var mPwd: String = ""
    var mCoin: Int = 0
    var mId: Int = 0
    var mBundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moviesBinding = FragmentBuyBinding.inflate(inflater, container, false)

        val sharedPref =
            requireContext().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        mNum = sharedPref.getString("USERNAME","").toString()
        mPwd = sharedPref.getString("PASSWORD","").toString()
        mCoin = sharedPref.getInt("COIN",0)
        mId = sharedPref.getInt("ID",0)
        return moviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBundle = arguments
        subscribeUi()
    }

    private fun subscribeUi() {
        moviesBinding.buy.setOnClickListener {
            buyContent()
        }

        moviesBinding.cancel.setOnClickListener {
            this.findNavController().navigate(R.id.action_buy_fragment_to_movies_fragment)
        }
    }
    fun popDialog(activity: FragmentActivity, string: String) {
        val popDialog = PopDialog(activity, string)
        popDialog.show()
    }
    private fun buyContent() {
        if (mCoin < 1) {
            popDialog(requireActivity(), "金币不足,请充值")
            return
        }
        infoJob?.cancel()
        mCoin--
        infoJob = lifecycleScope.launch {
            buyViewModel.buy(mId, mNum, mPwd, mCoin)
                ?.observe(viewLifecycleOwner) { it ->
                    if (it.resultData) {
                        mBundle?.getString("num", "null")?.let { it1 -> updateMovies(it1) }
                        navigateToPlayer(mBundle)
                    }
                }
        }
    }
    private fun navigateToPlayer(bundle: Bundle?) {
        this.dismiss()
        bundle?.apply { putString("fromWhere" ,"BUY") }
        this.findNavController().navigate(R.id.action_buy_fragment_to_player_fragment, bundle)
    }

    private fun updateMovies(num: String) {
        infoJob2?.cancel()
        infoJob2 = lifecycleScope.launch {
            // Init DB.
            val database = MoviesDatabase.getInstance(requireContext())
            database.moviesDao().updateTour(num, false)
        }
    }
}