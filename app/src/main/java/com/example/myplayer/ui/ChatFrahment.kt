package com.example.myplayer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.databinding.FragmentChatsBinding
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

import com.example.myplayer.adapter.ChatAdapter
import com.example.myplayer.data.db.ChatDatabase
import com.example.myplayer.data.db.ChatEntity
import com.example.myplayer.data.db.InfoDatabase
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.reponse.PersonChat
import com.example.myplayer.viewmodels.ChatViewModel
import com.netease.nimlib.sdk.Observer
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChatFrahment : Fragment() {
    private lateinit var cityBinding: FragmentChatsBinding

    private var cityJob: Job? = null

    private val chatViewModel: ChatViewModel by viewModels()

    private lateinit var assetAdapter: ChatAdapter

    private var token:String? = null
    private var accid:String? = null

    //
    private lateinit var assetLayoutManager: LinearLayoutManager

    private var msgList: MutableList<ChatEntity> = mutableListOf()


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
        getChatHistory()
        subscribeUi()
    }

    private fun getChatHistory() {
        cityJob?.cancel()
        cityJob = lifecycleScope.launch {
            chatViewModel.chatHistory()
                ?.observe(viewLifecycleOwner) { it ->
                    msgList.addAll(it)
                }
        }
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
            if (cityBinding.etChatMessage.text.toString().isNotEmpty()) {
                sendTextMsg(cityBinding.etChatMessage.text.toString())
            }

        }
    }
    override fun onStart() {
        super.onStart()
        registerMsgReceiver()
    }


    private fun sendTextMsg(content: String) {
        val sessionType = SessionTypeEnum.P2P
        val textMessage = MessageBuilder.createTextMessage("15940850831", sessionType, content+accid)
        val personChat:ChatEntity = ChatEntity(content, true)
        assetAdapter.addOneItem(personChat)
//        msgList.add(personChat)
//        android.util.Log.d("zwj", "size ${msgList.size}")
//        assetAdapter.updateListItem(msgList)
        cityBinding.lvChatDialog.scrollToPosition(msgList.size - 1);
        updateDao(msgList)
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
                            val personChat:ChatEntity = ChatEntity(content, false)
                            assetAdapter.addOneItem(personChat)
//                            msgList.add(personChat)
//                            for (asset in msgList) {
//                                android.util.Log.d("zwj" ,"size ${msgList.size}" )
//                            }
//                            assetAdapter.updateListItem(msgList)
                            cityBinding.lvChatDialog.scrollToPosition(msgList.size - 1);
                            updateDao(msgList)
                        }
                    }
                }
            }
        NIMClient.getService(MsgServiceObserve::class.java)
            .observeReceiveMessage(incomingMessageObserver, true)
    }

    fun updateDao(chatList: MutableList<ChatEntity>) {
        cityJob = lifecycleScope.launch {
            val infos: MutableList<ChatEntity> = mutableListOf()

            repeat(chatList.size) { i ->
                infos.add(ChatEntity(chatList[i].content, chatList[i].isMe))
            }

            val database = ChatDatabase.getInstance(requireContext())

            database.chatDao().deleteInfos()

            database.chatDao().insertChat(chatList)
        }
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