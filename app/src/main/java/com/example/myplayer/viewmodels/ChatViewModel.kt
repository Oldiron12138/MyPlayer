package com.example.myplayer.viewmodels

import android.content.ContentResolver
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myplayer.MainApplication
import com.example.myplayer.data.db.ChatEntity
import com.example.myplayer.data.reponse.CityResponse
import com.example.myplayer.data.repository.ChatRepository
import com.example.myplayer.data.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val ScreenShotProjection = arrayOf(
        //查询图片需要的数据列
        MediaStore.Images.Media.DATA,  //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
    )
    private val imageType = arrayOf("images/*")

    var infoCache: LiveData<List<ChatEntity>>? = null
    suspend fun chatHistory(): LiveData<List<ChatEntity>>? {
        if (infoCache == null) {
            infoCache = chatRepository.chatHistory().asLiveData()

        }
        return infoCache
    }

    fun queryImages(bucketId: String?): String {
        val filePath: String = "null"

        val uri = MediaStore.Files.getContentUri("external")
        val sortOrder = MediaStore.Images.Media._ID + " DESC limit 1 "
        var selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) +
                " AND " + MediaStore.Images.Media.MIME_TYPE + "=?" +
                " or " + MediaStore.Images.Media.MIME_TYPE + "=?"
        try {
            val data = MainApplication.context?.contentResolver?.query(
                uri,
                ScreenShotProjection,
                selection,
                imageType,
                sortOrder
            )

            if (data == null) {
                return filePath
            }

            if (data.moveToFirst()) {
                //查询数据
                val imagePath: String =
                    data.getString(data.getColumnIndexOrThrow(ScreenShotProjection[0]))
                return imagePath
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return filePath
    }

    /**
     * 只获取普通图片，不获取Gif（在Android11的机器中）
     * 在targetSdkVersion适配到30后  查询图片的Sql发生了变化
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @WorkerThread
    fun queryImagesP(bucketId: String?): String {
        val filePath :String = "null"
        android.util.Log.d("zwj" ,"111")
        val uri = MediaStore.Files.getContentUri("external")
        val sortOrder = MediaStore.Files.FileColumns._ID + " DESC"
        var selection = (MediaStore.Files.FileColumns.DISPLAY_NAME + "="
                + "")

        val bundle = createSqlQueryBundle(null, null, sortOrder, 2)
        android.util.Log.d("zwj" ,"2222")
        try {
            val data = MainApplication.applicationContext.contentResolver?.query(
                uri,
                ScreenShotProjection,
                bundle,
                null
            )
            if (data == null) {
                android.util.Log.d("zwj" ,"333")
                return filePath
            }

            while (data.moveToNext()) {
                //查询数据
                android.util.Log.d("zwj" ,"222")
                val imagePath: String =
                    data.getString(data.getColumnIndexOrThrow(ScreenShotProjection[0]))
                val path: String = data.getString(0)
                android.util.Log.d("zwj" ,"path111 $path")
                if (bucketId != null) {
                    if(imagePath.indexOf(bucketId) != -1) {
                        android.util.Log.d("zwj" ,"imagePath $imagePath")
                        return imagePath
                    }
                }
            }

        } catch (e: Exception) {
            android.util.Log.d("zwj" ,"444$e")
            e.printStackTrace()
        }
        return filePath
    }

    fun createSqlQueryBundle(
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?, limitCount: Int = 0, offset: Int = 0
    ): Bundle? {
        if (selection == null && selectionArgs == null && sortOrder == null) {
            return null
        }
        val queryArgs = Bundle()
        if (selection != null) {
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
        }
        if (selectionArgs != null) {
            queryArgs.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
        }
        if (sortOrder != null) {
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, sortOrder)
        }
        queryArgs.putString(ContentResolver.QUERY_ARG_SQL_LIMIT, "$limitCount offset $offset")
        return queryArgs
    }

}