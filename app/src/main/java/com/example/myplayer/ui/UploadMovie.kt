package com.example.myplayer.ui

import android.Manifest
import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplayer.databinding.FragmentUploadmovieBinding
import kotlinx.coroutines.Job
import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import java.lang.Exception
import android.provider.MediaStore

import android.provider.DocumentsContract

import android.content.ContentUris

import android.os.Environment

import android.os.Build

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import com.example.myplayer.util.FileUtils
import java.io.*
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.media.ThumbnailUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.myplayer.MainApplication
import com.example.myplayer.R
import com.example.myplayer.viewmodels.ReleaseViewModel
import com.example.myplayer.viewmodels.UploadViewModel
import com.example.myplayer.widget.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.net.Socket
import android.R.attr.bitmap

import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable




@AndroidEntryPoint
class UploadMovie: Fragment() {
    private lateinit var uploadMovie: FragmentUploadmovieBinding

    var filePath: String? = null
    var thumbNail: Bitmap? = null
    private var uploadJob: Job? = null
    private var loadingDialog: LoadingDialog? = null
    private val uploadViewModel: UploadViewModel by viewModels()
    var permissions = Manifest.permission.READ_EXTERNAL_STORAGE
    var permissionArray = arrayOf(permissions)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val checkPermission = requireActivity().let { ActivityCompat.checkSelfPermission(it, permissions) }
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissionArray, 0)
        }
        uploadMovie = FragmentUploadmovieBinding.inflate(inflater, container, false)
        return uploadMovie.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    private fun subscribeUi() {
        uploadMovie.shangchuan.visibility = View.GONE
        uploadMovie.imageView2.setOnClickListener {
            openAlbum()
        }
        uploadMovie.shangchuan.setOnClickListener{
            loadingDialog = loadDialog(requireActivity())
            val file: File = File(filePath)
            var files:MutableList<File> = mutableListOf()
            files.add(file)
            val thumbNailPath: String = ReleaseFragment.saveBitMapToFile(requireContext(),"thumbnail",thumbNail,true)
            files.add(File(thumbNailPath))
            uploadSinglePicture(files)
        }
    }

    private fun openAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "video/*"
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
                    uploadMovie.shangchuan.visibility = View.VISIBLE
                    filePath = uploadViewModel.getLatestImage(data?.getString(2))
                    thumbNail = getThumbnail(filePath!!)
                    uploadMovie.imageView2.setImageBitmap(thumbNail!!)
                    //uploadFile(filePath!!)
                } catch (e: Exception) {
                } catch (e: OutOfMemoryError) {
                }
            }
    }

    fun getThumbnail(filePath: String):Bitmap? {
        val file: File = File(filePath)
        val bitmap = ThumbnailUtils.createVideoThumbnail(
            file.getAbsolutePath(), MediaStore.Images.Thumbnails.MICRO_KIND)//创建一个视频缩略图
        return ThumbnailUtils.extractThumbnail(bitmap, 300, 400,ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
    }

    private fun uploadFile(filePath: String) {
        uploadJob?.cancel()
        uploadJob = lifecycleScope.launch {
            val file: File = File(filePath)
            uploadViewModel.uploadFile(file)

        }
    }

    fun loadDialog(activity: FragmentActivity): LoadingDialog {
        val loadDialog = LoadingDialog(activity)
        loadDialog.show()
        return loadDialog
    }


    fun uploadSinglePicture(files: List<File>) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM) //表单类型

        for (file in files) {
            val requestFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            requestFile.contentType()
            builder.addFormDataPart("file", file.name, requestFile)
        }
        val part: List<MultipartBody.Part> = builder.build().parts
        uploadJob?.cancel()
        uploadJob = lifecycleScope.launch {
            uploadViewModel.upload(part)
                ?.observe(viewLifecycleOwner) {
                    if (it.resultData != null) {
                        loadingDialog!!.dismissDialog()
                        Toast.makeText(requireContext(), "上传成功", Toast.LENGTH_LONG).show()
                    } else {
                        loadingDialog!!.dismissDialog()
                        Toast.makeText(requireContext(), "上传失败", Toast.LENGTH_LONG).show()
                    }
                }

        }
    }


//    @SuppressLint("NewApi")
//    private fun getDataColumn(
//        context: Context?,
//        uri: Uri?,
//        selection: String?,
//        selectionArgs: Array<String>?
//    ): String? {
//        try {
//            String[] pojo = {MediaStore.Images.Media.DATA};
//            Cursor cursor = managedQuery(uri, pojo, null, null,null);
//            if(cursor!=null)
//            {
//                ContentResolver cr = this.getContentResolver();
//                int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                String path = cursor.getString(colunm_index);
//                /***
//                 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名
//                 * 如果是图片格式的话，那么才可以
//                 */
//                if(path.endsWith("jpg")||path.endsWith("png"))
//                {
//                    picPath = path;
//                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                    imageView.setImageBitmap(bitmap);
//                }else{alert();}
//            }else{alert();}
//
//        } catch (Exception e) {
//        }
//    }

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

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri?): Boolean {
        return "com.android.externalstorage.documents" == uri?.authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri?): Boolean {
        return "com.android.providers.downloads.documents" == uri?.authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri?): Boolean {
        return "com.android.providers.media.documents" == uri?.authority
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.content/...
     */
    fun isGooglePhotosUri(uri: Uri?): Boolean {
        return "com.google.android.apps.photos.content" == uri?.authority
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.contentprovider/0/1/mediakey:/local%3A821abd2f-9f8c-4931-bbe9-a975d1f5fabc/ORIGINAL/NONE/1075342619
     */
    fun isGooglePlayPhotosUri(uri: Uri?): Boolean {
        return "com.google.android.apps.photos.contentprovider" == uri?.authority
    }

    fun getImageUrlWithAuthority(context: Context?, uri: Uri?): String? {
        var `is`: InputStream? = null
        if (uri?.authority != null) {
            try {
                `is` = context?.contentResolver?.openInputStream(uri)
                val bmp = BitmapFactory.decodeStream(`is`)
                return writeToTempImageAndGetPathUri(context, bmp).toString()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                try {
                    `is`?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    /**
     * 将图片流读取出来保存到手机本地相册中
     */
    fun writeToTempImageAndGetPathUri(inContext: Context?, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext?.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
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