package com.example.myplayer.ui

import android.Manifest
import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import android.content.Intent
import com.example.myplayer.viewmodels.ReleaseViewModel
import com.example.myplayer.MainActivity

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat
import android.graphics.BitmapFactory

import android.graphics.Bitmap

import android.content.ContentResolver
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toFile
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import java.io.File
import java.io.FileNotFoundException
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import java.io.FileOutputStream
import java.lang.Exception
import okhttp3.MultipartBody
import android.R.attr.path
import com.example.myplayer.databinding.FragmentReleaseBinding
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody


@AndroidEntryPoint
class ReleaseFragment: Fragment() {
    private lateinit var releaseBinding: FragmentReleaseBinding

    private var relseaseJob: Job? = null

    private val releaseViewModel: ReleaseViewModel by viewModels()

    private lateinit var toUpBitMap: Bitmap
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        releaseBinding = FragmentReleaseBinding.inflate(inflater, container, false)
        return releaseBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    private fun subscribeUi() {
        releaseBinding.imageView2.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    1
//                )
//            } else {
                openAlbum()
//            }
        }
        releaseBinding.releaseBtn.setOnClickListener {
            if (ableToUpload()) {
                releaseJson()
                relseaseContent()
            }
        }
    }

    private fun relseaseContent() {
        val builder: MultipartBody.Builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

        val file = saveBitMapToFile(requireContext(), "toUpload" ,toUpBitMap, true)
        val photoRequestBody =  RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            //携带一个表单参数
            .addFormDataPart("username", "Chen-XiaoMo")
            //设置参数名、文件名和文件
            .addFormDataPart("myfile","Naruto.jpg",photoRequestBody)
            .build()
        val files: File = File(file)
        val photoPart = MultipartBody.Part.createFormData("multipartFiles", "toUpload", requestBody)
        uploadSinglePicture(files)
    }

    private fun releaseJson() {
        val gson = Gson()
        var paramsMap: MutableMap<String,String> = mutableMapOf()
        paramsMap.put("title" , releaseBinding.contentTitle.text.toString())
        paramsMap.put("city" , releaseBinding.contentCity.text.toString())
        paramsMap.put("desc" , releaseBinding.contentDesc.text.toString())
        paramsMap.put("street" , releaseBinding.contentStreet.text.toString())
        paramsMap.put("phone" , releaseBinding.contentPhoneDeatil.text.toString())
        paramsMap.put("price" , releaseBinding.contentPrice.text.toString())

        val strEntity = gson.toJson(paramsMap)
        val body: RequestBody =
            strEntity.toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
        relseaseJob?.cancel()
        relseaseJob = lifecycleScope.launch {
            releaseViewModel.uploadJson(body)
                ?.observe(viewLifecycleOwner) {
                    android.util.Log.d("zwjJson","json result ${it.resultData}")
                }
//                    if (it.resultData) {
//                        Toast.makeText(requireContext(), "上传成功", Toast.LENGTH_LONG).show()
//                    } else {
//                        Toast.makeText(requireContext(), "上传失败", Toast.LENGTH_LONG).show()                    }
//                }
        }
    }

    private fun ableToUpload(): Boolean {
        if (TextUtils.isEmpty(releaseBinding.contentCity.text) || TextUtils.isEmpty(releaseBinding.contentDesc.text) || TextUtils.isEmpty(releaseBinding.contentPhoneDeatil.text) ||
            TextUtils.isEmpty(releaseBinding.contentPrice.text) || TextUtils.isEmpty(releaseBinding.contentStreet.text) || TextUtils.isEmpty(releaseBinding.contentTitle.text) ||
            releaseBinding.imageView2.drawable == null)
        {return false}
        return true
    }


    fun uploadSinglePicture(file: File) {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM) //表单类型
        val requestFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        requestFile.contentType()
        builder.addFormDataPart("file", file.name, requestFile)
        val part = builder.build().part(0)
        relseaseJob?.cancel()
        relseaseJob = lifecycleScope.launch {
            releaseViewModel.upload(part)
                ?.observe(viewLifecycleOwner) {
                    Log.d("zwj" ,"result data ${it.resultData}")
                }
//                    if (it.resultData) {
//                        Toast.makeText(requireContext(), "上传成功", Toast.LENGTH_LONG).show()
//                    } else {
//                        Toast.makeText(requireContext(), "上传失败", Toast.LENGTH_LONG).show()                    }
//                }
        }
    }

    private fun openAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        android.util.Log.d("zwj" ,"000")
        if (resultCode == RESULT_OK) {

            val uri: Uri? = data?.data
            val cr: ContentResolver = requireContext().contentResolver
            try {
                val bitmap = BitmapFactory.decodeStream(uri?.let { cr.openInputStream(it) })
                toUpBitMap = bitmap
                releaseBinding.imageView2.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
               // Log.e("Exception", e.getMessage(), e)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun saveBitMapToFile(
        context: Context?,
        fileName: String,
        bitmap: Bitmap?,
        isCover: Boolean
    ): String {
        if (null == context || null == bitmap) {
            Log.d("zwj", "111")
            return "null"
        }
        if (TextUtils.isEmpty(fileName)) {
            Log.d("zwj", "2222")
            return "null"
        }
        var fOut: FileOutputStream? = null
        return try {
            var file: File? = null
            var fileDstPath = ""
            if (false) {

                // 保存到sd卡
                fileDstPath = (Environment.getExternalStorageDirectory().absolutePath
                        + File.separator + "MyFile" + File.separator + fileName)
                val homeDir = File(
                    Environment.getExternalStorageDirectory().absolutePath
                            + File.separator + "MyFile" + File.separator
                )
                if (!homeDir.exists()) {
                    homeDir.mkdirs()
                }
            } else {

                // 保存到file目录
                fileDstPath = (context.filesDir.absolutePath
                        + File.separator + "MyFile" + File.separator + fileName)
                val homeDir = File(
                    context.filesDir.absolutePath
                            + File.separator + "MyFile" + File.separator
                )
                if (!homeDir.exists()) {
                    homeDir.mkdir()
                }
            }
            file = File(fileDstPath)
            if (!file.exists() || isCover) {

                // 简单起见，先删除老文件，不管它是否存在。
                file.delete()
                fOut = FileOutputStream(file)
                if (fileName.endsWith(".jpg")) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fOut)
                } else {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                }
                fOut.flush()
                //bitmap.recycle()
            }
            Log.i(
                "FileSave", "saveDrawableToFile " + fileName
                        + " success, save path is " + fileDstPath
            )
            fileDstPath
        } catch (e: Exception) {
            Log.d("zwj", "111 $e")
            "null"
        } finally {
            if (null != fOut) {
                try {
                    fOut.close()
                } catch (e: Exception) {
                }
            }
        }
    }
}