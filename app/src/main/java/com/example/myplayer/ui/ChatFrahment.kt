package com.example.myplayer.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.MainApplication
import com.example.myplayer.databinding.FragmentChatsBinding
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
import com.example.myplayer.viewmodels.ChatViewModel
import com.netease.nimlib.sdk.Observer
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import org.json.JSONObject


@AndroidEntryPoint
class ChatFrahment : Fragment() {
    private lateinit var cityBinding: FragmentChatsBinding

    private var cityJob: Job? = null
    var permissions = Manifest.permission.READ_EXTERNAL_STORAGE
    var permissionArray = arrayOf(permissions)
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
        val checkPermission = requireActivity().let { ActivityCompat.checkSelfPermission(it, permissions) }
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissionArray, 0)
        }
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
        cityBinding.sndImage.setOnClickListener{
            openAlbum()
        }
    }
    override fun onStart() {
        super.onStart()
        registerMsgReceiver()
    }


    private fun sendTextMsg(content: String) {
        val sessionType = SessionTypeEnum.P2P
        val textMessage = MessageBuilder.createTextMessage("15940850831", sessionType, content+accid)
        val personChat:ChatEntity = ChatEntity(content, true,false,"null")
        assetAdapter.addOneItem(personChat)
        msgList.add(personChat)
        assetLayoutManager.scrollToPosition(msgList.size-1)
        assetAdapter.notifyDataSetChanged()
        NIMClient.getService(MsgService::class.java).sendMessage(textMessage, false)
    }

    private fun sendImgMsg(pathName: String, url: String) {
        val account = "15940850831"

        val sessionType = SessionTypeEnum.P2P

        val file = File(pathName)

        val message:IMMessage = MessageBuilder.createImageMessage(account, sessionType, file, file.getName())
        val personChat:ChatEntity = ChatEntity("null", true,true,url)
        assetAdapter.addOneItem(personChat)
        msgList.add(personChat)
        assetLayoutManager.scrollToPosition(msgList.size-1)
        //  updateDao(msgList)
        assetAdapter.notifyDataSetChanged()
        NIMClient.getService(MsgService::class.java).sendMessage(message, false)
            .setCallback(object : RequestCallback<Void?> {
                override fun onSuccess(param: Void?) {
                }
                override fun onFailed(code: Int) {
                }
                override fun onException(exception: Throwable) {
                }
            })

    }

    private fun openAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                var uri: Uri = data?.data!!
                val data = MainApplication.applicationContext.contentResolver.query(
                    uri, null, null,
                    null, null
                )
                if (data != null) {
                    data?.moveToFirst()
                }
                val filePath:String = getLatestImage(data?.getString(2))
                sendImgMsg(filePath, uri.toString())
            } catch (e: Exception) {
            } catch (e: OutOfMemoryError) {
            }
        }
    }

    //获取截图图片
    fun getLatestImage(bucketId: String? = null):String {

        var data: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            data = chatViewModel.queryImagesP(bucketId)
        } else {
            data = chatViewModel.queryImages(bucketId)
        }
//                val imagePath = data.path?.toLowerCase()
//                screenShoot.forEach {
//                    if (imagePath?.contains(it)!! && (System.currentTimeMillis() / 1000 - data.addTime < 2)) {
//                        _screentShotInfoData.postValue(data)
//                        return@forEach
//                    }
        return data
    }

    private fun getDataColumn(
        context: Context?,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        try {
            val pojo = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? =
                uri?.let { context?.contentResolver?.query(it, pojo, null, null, null) }
            if (cursor != null) {

                if(cursor.moveToFirst()){
                }
                val colunm_index:Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val columnIndex: Int = cursor.getColumnIndexOrThrow(pojo[0])
                val path = cursor.getString(colunm_index)
                /***
                 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名
                 * 如果是图片格式的话，那么才可以
                 */
                return path
            } else {
            }
        } catch (e: Exception) {
        }
        return null
    }


    private fun registerMsgReceiver() {
        val incomingMessageObserver: Observer<List<IMMessage?>?> =
            object : Observer<List<IMMessage?>?> {
                override fun onEvent(t: List<IMMessage?>?) {
                    if (t != null && t[0]!!.msgType.equals(MsgTypeEnum.text)) {
                        for (message in t) {

                            //val content: String = message?.content.toString()
                            val content: String = message?.content.toString()
                            val personChat:ChatEntity = ChatEntity(content, false, false, "null")
                            assetAdapter.addOneItem(personChat)
                            msgList.add(personChat)
                            assetLayoutManager.scrollToPosition(msgList.size - 1)
                           // updateDao(msgList)
                        }
                    } else if(t != null && t[0]!!.msgType.equals(MsgTypeEnum.image)) {
                        for (message in t) {
                            val json: String? = message?.attachment?.toJson(true)
                            val url: String? = parseEasyJson(json)
                            val personChat:ChatEntity? =
                                url?.let { ChatEntity("null", false, true, it) }
                            assetAdapter.addOneItem(personChat!!)
                            msgList.add(personChat)
                            assetLayoutManager.scrollToPosition(msgList.size - 1)
                        }

                    }
                }
            }

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeReceiveMessage(incomingMessageObserver, true)
    }

    private fun parseEasyJson(json: String?):String? {
        try {
            val jsonObject = JSONObject(json)
            return jsonObject.getString("url")
        } catch (e: Exception) {
            e.printStackTrace()
            return "eeeee"
        }
        return "111"
    }

    fun doLogin() {
        val info: LoginInfo = LoginInfo(accid ,token)
        val callback: RequestCallback<LoginInfo?> = object : RequestCallback<LoginInfo?> {
            override fun onSuccess(param: LoginInfo?) {
                // your code
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "权限申请成功", Toast.LENGTH_LONG).show()
                }
        }
    }

}