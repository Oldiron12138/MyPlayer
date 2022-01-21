package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.UploadResult
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadRepository @Inject constructor(
    private val androidTvMdsService: MyPlayerService
) {

    suspend fun uploadFile(file: File) {
        Thread(Runnable Thread@{
            try {
                var socket: Socket = Socket("111.229.96.2", 8897)
                val outStream: OutputStream = socket.getOutputStream()
                var `in`: ObjectInputStream? = null
                var out: ObjectOutputStream? = null
                var client: Socket? = null
                try {
                    //创建Socket，连接到服务器
                    client = Socket()
                    val address: SocketAddress = InetSocketAddress("111.229.96.2", 8897)
                    client.connect(address, 10000) // 设置超时时间
                    `in` = ObjectInputStream(client.getInputStream()) // 从服务器读数据
                    out = ObjectOutputStream(client.getOutputStream()) // 向服务器写数据

                    // 构造传输对象
                    val fileOutStream = RandomAccessFile(file, "r")
                    //fileOutStream.seek(Integer.valueOf(position))
                    val buffer = ByteArray(1024)
                    var len = -1
                    //var length: Int = Integer.valueOf(position)
                    while (fileOutStream.read(buffer).also { len = it } != -1) {
                        outStream.write(buffer, 0, len)
                        //                length = len
                        //                val msg = Message()
                        //                msg.getData().putInt("size", length)
                        //                handler.sendMessage(msg)
                    }
                    fileOutStream.close()
                    outStream.close()

                    // 向服务器写数据
                    // out.writeObject(msg)
                    out.flush()

                    // 读取服务器返回的信息
                    val result = `in`.readObject() as String
                    if (result != null && "SUCCESS" == result) {
                        //                showToast(context, context.getResources().getString(R.string.upload_success))
                        //                listener.onFinish(result)
                    }
                } catch (e: Exception) {
                }
                val seriesDetailResponse = MutableLiveData<UploadResult>()
                return@Thread
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }).start()
    }
}