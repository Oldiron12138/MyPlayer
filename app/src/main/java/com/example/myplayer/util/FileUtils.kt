package com.example.myplayer.util

import android.animation.Animator
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import com.example.myplayer.MainApplication
import java.lang.Exception
import android.graphics.Bitmap
import android.view.View
import com.example.myplayer.util.Bulr.doBlur
import androidx.interpolator.view.animation.FastOutLinearInInterpolator

import android.view.ViewGroup

import android.view.animation.Animation
import android.view.animation.Transformation
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import androidx.core.animation.addListener


/**
 * Title: FileUri
 * <p>
 * Description: Uri 工具类
 * </p>
 * @author javakam
 * @date 2020/8/24  11:24
 */
object FileUtils {
    private const val BLUR_RADIUS = 25f

    fun getPingMuSize(mContext: Context): Int {
        val height: Int = mContext.getResources().getDisplayMetrics().heightPixels
        return px2dip(mContext,height.toFloat())
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }



    fun toBlur(originBitmap: Bitmap, scaleRatio: Int): Bitmap? {
        val blurRadius = 8
        if (scaleRatio <= 0) {
            val scaleRatio: Int = 10
        }
        val scaledBitmap = Bitmap.createScaledBitmap(
            originBitmap,
            originBitmap.width / scaleRatio,
            originBitmap.height / scaleRatio,
            false
        )
        return doBlur(scaledBitmap, blurRadius, true)
    }

    fun expand(view: View) {
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val viewHeight: Int = view.measuredHeight
        view.alpha = 0f
        //view.layoutParams.height = 0
        view.visibility = View.VISIBLE

        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 400
        valueAnimator.addUpdateListener { animation ->
            val curValue = animation.animatedValue as Float
           // view.layoutParams.height = curValue
            view.alpha = curValue
            view.requestLayout()
//            if (curValue == viewHeight) {
//
//                view.visibility = View.VISIBLE
//                view.requestLayout()
//            }
        }
        valueAnimator.start()
    }

    @SuppressLint("WrongConstant")
    fun collapse(view: View) {
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val viewWidth = view.measuredWidth
        val valueAnimator = ValueAnimator.ofInt(viewWidth, 0)
        valueAnimator.duration = 400
        valueAnimator.addUpdateListener { animation ->
            val curValue = animation.animatedValue as Int
            view.layoutParams.width = curValue
            view.requestLayout()
            if (curValue == 0) {
                view.layoutParams.width = 0
                view.visibility = View.INVISIBLE
                view.requestLayout()
            }
        }
        valueAnimator.start()
    }




private val imageType = arrayOf("image/png", "image/jpeg")
    //从 File Path 中获取 Uri
    //----------------------------------------------------------------

//    private val imageType = arrayOf("image/png", "image/jpeg")
//    fun getRealPathFromUri(context: Context, uri: Uri): String? {
//        Thread {
//            var data: String? = null
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                data= queryImagesP(null)
//            } else {
//                data = queryImages(null)
//            }
//            return@Thread data
//        }.start()
//
//    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private fun getRealPathFromUriBelowAPI19(context: Context, uri: Uri): String? {
        return getDataColumn(context, uri, null, null)
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String? {
        var filePath: String? = null
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            val documentId = DocumentsContract.getDocumentId(uri)
            if (isMediaDocument(uri)) {
                val contentUri:Uri = MediaStore.Files.getContentUri("external")
                // 使用':'分割
                val id = documentId.split(":").toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(id)
                filePath = getDataColumn(
                    context,
                    contentUri,
                    selection,
                    selectionArgs
                )
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(documentId)
                )
                filePath = getDataColumn(context, contentUri, null, null)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null)
        } else if ("file" == uri.scheme) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.path
        }
        return filePath
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        val sortOrder = MediaStore.Files.FileColumns._ID + " DESC"
        val bundle = selection?.let { createSqlQueryBundle(it, imageType, sortOrder, 1) }
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if(cursor == null) {
            }
            if (cursor != null) {
                if(cursor.moveToFirst()) {
                }
            }
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(projection[0])
                path = cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            if (cursor != null) {
                cursor.close()
            }
        }
        return path
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
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
                    data.getString(data.getColumnIndexOrThrow(ScreenShotProjection[1]))
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
        val uri = MediaStore.Files.getContentUri("external")
        val sortOrder = MediaStore.Files.FileColumns._ID + " DESC"
        var selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) +
                " AND " + MediaStore.Images.Media.MIME_TYPE + "=?" +
                " or " + MediaStore.Images.Media.MIME_TYPE + "=?"

        val bundle = createSqlQueryBundle(selection, imageType, sortOrder, 1)

        try {
            val data = MainApplication.context?.contentResolver?.query(
                uri,
                ScreenShotProjection,
                bundle,
                null
            )

            if (data == null) {
                return filePath
            }

            if (data.moveToFirst()) {
                //查询数据
                val imagePath: String =
                    data.getString(data.getColumnIndexOrThrow(ScreenShotProjection[1]))
                return imagePath
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return filePath
    }

    /*
    * 创建Android11 所需要的bundle对象
    * */
    fun createSqlQueryBundle(
        selection: String,
        selectionArgs: Array<String>,
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

    private val ScreenShotProjection = arrayOf( //查询图片需要的数据列
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,  //图片的显示名称  aaa.jpg
        MediaStore.Images.Media.DATA,  //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
        MediaStore.Images.Media.SIZE,  //图片的大小，long型  132492
        MediaStore.Images.Media.WIDTH,  //图片的宽度，int型  1s920
        MediaStore.Images.Media.HEIGHT,  //图片的高度，int型  1080
        MediaStore.Images.Media.MIME_TYPE,  //图片的类型     image/jpeg
        MediaStore.Images.Media.DATE_ADDED //图片被添加的时间，long型  1450518608
    )


}