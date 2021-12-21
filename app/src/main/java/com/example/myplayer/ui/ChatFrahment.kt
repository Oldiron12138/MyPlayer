package com.example.myplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.adapter.CityAdapter
import com.example.myplayer.databinding.FragmentChatsBinding
import com.example.myplayer.databinding.FragmentCityBinding
import com.example.myplayer.viewmodels.CityViewModel
import com.netease.nimlib.sdk.NIMClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.MsgService

import com.netease.nimlib.sdk.msg.model.IMMessage

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.AuthService


import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.msg.MsgServiceObserve

import android.os.Build
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myplayer.adapter.ChatAdapter
import com.example.myplayer.adapter.MoviesAdapter
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.data.reponse.PersonChat
import com.netease.nimlib.sdk.Observer


@AndroidEntryPoint
class ChatFrahment : Fragment() {
    private lateinit var cityBinding: FragmentChatsBinding

    private var cityJob: Job? = null

    private val cityViewModel: CityViewModel by viewModels()

    private lateinit var assetAdapter: ChatAdapter

    private var token:String? = null
    private var accid:String? = null

    //
    private lateinit var assetLayoutManager: LinearLayoutManager

    private var msgList: MutableList<PersonChat> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cityBinding = FragmentChatsBinding.inflate(inflater, container, false)
        return cityBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accid = arguments?.getString("num")
        token = arguments?.getString("token")
        //
        subscribeUi()
    }

    private fun subscribeUi() {
        assetAdapter = ChatAdapter(requireContext() )
        assetLayoutManager = LinearLayoutManager(requireContext())
        assetLayoutManager.orientation = LinearLayoutManager.VERTICAL
        cityBinding.lvChatDialog.layoutManager = assetLayoutManager
        cityBinding.lvChatDialog.adapter = assetAdapter
        cityBinding.lvChatDialog.itemAnimator = null
        doLogin()
        cityBinding.btnChatMessageSend.setOnClickListener {
            sendTextMsg()
        }
    }
    override fun onStart() {
        super.onStart()
        registerMsgReceiver()
    }


    private fun sendTextMsg() {
        val sessionType = SessionTypeEnum.P2P
        val text = "this is an example"
        val textMessage = MessageBuilder.createTextMessage("15940850831", sessionType, text)
        val personChat:PersonChat = PersonChat(true, text)
        msgList.add(personChat)
        assetAdapter.updateListItem(msgList)
        NIMClient.getService(MsgService::class.java).sendMessage(textMessage, false)
    }


    private fun registerMsgReceiver() {
        val incomingMessageObserver: Observer<List<IMMessage?>?> =
            object : Observer<List<IMMessage?>?> {
                override fun onEvent(t: List<IMMessage?>?) {
                    android.util.Log.d("zwj " ,"receive")
                    if (t != null) {
                        for (message in t) {

                            val content: String = message?.content.toString()
                            android.util.Log.d("zwj " ,"receive $content")
                            val personChat:PersonChat = PersonChat(false, content)
                            msgList.add(personChat)
                            for (asset in msgList) {
                                android.util.Log.d("zwj" ,"size ${msgList.size}" )
                            }
                            assetAdapter.updateListItem(msgList)
                        }
                    }
                }
            }
        NIMClient.getService(MsgServiceObserve::class.java)
            .observeReceiveMessage(incomingMessageObserver, true)
    }


    fun doLogin() {
        val info: LoginInfo = LoginInfo(accid ,token)
        val callback: RequestCallback<LoginInfo?> = object : RequestCallback<LoginInfo?> {
            override fun onSuccess(param: LoginInfo?) {
                // your code
                android.util.Log.d("zwj", "login success")
            }

            override fun onFailed(code: Int) {
                if (code == 302) {
                    // your code
                } else {
                    // your code
                }
            }

            override fun onException(exception: Throwable) {
                // your code
            }
        }

        //执行手动登录
        NIMClient.getService(AuthService::class.java).login(info).setCallback(callback)
    }

}