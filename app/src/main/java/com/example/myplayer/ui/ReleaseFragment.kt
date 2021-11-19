package com.example.myplayer.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myplayer.R
import com.example.myplayer.adapter.InfoAdapter
import com.example.myplayer.adapter.MoviesAdapter
import com.example.myplayer.adapter.bindInfoImage
import com.example.myplayer.data.MoivesReceiveData
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.data.reponse.InfoResponse
import com.example.myplayer.databinding.FragmentInfoBinding
import com.example.myplayer.databinding.FragmentMoviesBinding
import com.example.myplayer.databinding.FragmentReleaseBinding
import com.example.myplayer.ui.DetailFragment.Companion.setInfoViewModel
import com.example.myplayer.viewmodels.InfoViewModel
import com.example.myplayer.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.core.app.ActivityCompat.startActivityForResult

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import java.io.FileOutputStream
import java.lang.Exception


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
            relseaseContent()
        }
    }

    private fun relseaseContent() {
        val file = saveBitMapToFile(requireContext(), "toUpload" ,toUpBitMap, true)
        val photoRequestBody = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
        val photoPart = MultipartBody.Part.createFormData("image_file", "toUpload", photoRequestBody)
        val return_attributes = RequestBody.create("text/plain".toMediaTypeOrNull(), "zwj")
        relseaseJob?.cancel()
        relseaseJob = lifecycleScope.launch {
            releaseViewModel.upload(return_attributes, photoPart)
                ?.observe(viewLifecycleOwner) {
                    if (it.resultData) {
                        Toast.makeText(requireContext(), "上传成功", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), "上传失败", Toast.LENGTH_LONG).show()                    }
                }
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
            return "null"
        }
        if (TextUtils.isEmpty(fileName)) {
            return "null"
        }
        var fOut: FileOutputStream? = null
        return try {
            var file: File? = null
            var fileDstPath = ""
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {

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
                bitmap.recycle()
            }
            Log.i(
                "FileSave", "saveDrawableToFile " + fileName
                        + " success, save path is " + fileDstPath
            )
            fileDstPath
        } catch (e: Exception) {
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