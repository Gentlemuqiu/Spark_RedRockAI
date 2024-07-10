package com.example.redrockai.module.message.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redrockai.lib.utils.BaseUtils.shortToast
import com.example.redrockai.module.message.R
import com.example.redrockai.module.message.adapter.MessageAdapter
import com.example.redrockai.module.message.bean.ChatMessage
import com.example.redrockai.module.message.databinding.FragmentThirdBinding
import com.example.redrockai.module.message.util.GetFilePathFromUri
import com.iflytek.sparkchain.core.LLM
import com.iflytek.sparkchain.core.LLMCallbacks
import com.iflytek.sparkchain.core.LLMConfig
import com.iflytek.sparkchain.core.LLMError
import com.iflytek.sparkchain.core.LLMEvent
import com.iflytek.sparkchain.core.LLMFactory
import com.iflytek.sparkchain.core.LLMResult
import com.iflytek.sparkchain.core.Memory
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ThirdFragment : Fragment(), View.OnClickListener {

    private var _mBinding: FragmentThirdBinding? = null
    private val mBinding: FragmentThirdBinding
        get() = _mBinding!!

    private val REQUEST_CODE_PICK_IMAGE = 1
    private val REQUEST_CODE_CAPTURE_IMAGE = 2
    private val REQUEST_PERMISSION = 3

    private var photoFile: File? = null

    private var sessionFinished = true
    private var imageContent = true
    private lateinit var llm: LLM
    private lateinit var chatAdapter: MessageAdapter
    private val messages = mutableListOf<ChatMessage>()
    private val accumulatedContent = StringBuilder()
    private var temporaryMessageIndex: Int? = null
    private var flag: Boolean = false
    private var imagePath: String? = null
    private var token = 0
    private var llmCallbacks: LLMCallbacks = object : LLMCallbacks {
        override fun onLLMResult(llmResult: LLMResult, usrContext: Any?) {
            val content: String = llmResult.content
            val status: Int = llmResult.status
            activity?.runOnUiThread {
                accumulatedContent.append(content)
                if (status == 2) {
                    // 状态为2，拼接完成，添加新消息
                    if (temporaryMessageIndex != null) {
                        // 如果已经有临时消息，更新它
                        messages[temporaryMessageIndex!!] =
                            ChatMessage(accumulatedContent.toString(), false)
                        chatAdapter.notifyItemChanged(temporaryMessageIndex!!)
                        temporaryMessageIndex = null
                    } else {
                        messages.add(ChatMessage(accumulatedContent.toString(), false))
                        chatAdapter.notifyItemInserted(messages.size - 1)
                    }
                    toEnd()
                    accumulatedContent.clear()
                    sessionFinished = true
                } else {
                    // 状态不是2，更新UI显示正在加载的内容
                    if (temporaryMessageIndex == null) {
                        // 如果没有临时消息，添加一条
                        messages.add(ChatMessage(accumulatedContent.toString(), false))
                        temporaryMessageIndex = messages.size - 1
                        chatAdapter.notifyItemInserted(temporaryMessageIndex!!)
                    } else {
                        // 如果已经有临时消息，更新它
                        messages[temporaryMessageIndex!!] =
                            ChatMessage(accumulatedContent.toString(), false)
                        chatAdapter.notifyItemChanged(temporaryMessageIndex!!)
                    }
                    toEnd()
                }
                imagePath = null //第一轮会话后清空图片信息

            }
        }

        override fun onLLMEvent(event: LLMEvent, usrContext: Any?) {
        }

        override fun onLLMError(error: LLMError, usrContext: Any?) {
            activity?.runOnUiThread {
                messages.add(
                    ChatMessage(
                        "错误: err:${error.errCode} errDesc:${error.errMsg}",
                        false
                    )
                )
                chatAdapter.notifyItemInserted(messages.size - 1)
                toEnd()
            }
            sessionFinished = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentThirdBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    private fun checkAndRequestPermissions(): Boolean {
        val cameraPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val writePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val listPermissionsNeeded = mutableListOf<String>()

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                listPermissionsNeeded.toTypedArray(),
                REQUEST_PERMISSION
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION -> {
                val perms = mutableMapOf<String, Int>()
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED

                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices) {
                        perms[permissions[i]] = grantResults[i]
                    }

                    if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED &&
                        perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    ) {
                        openCamera()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Some Permission is Denied",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLLMConfig()
        initButtonClickListener()
        chatAdapter = MessageAdapter(messages)
        mBinding.rvAnswer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
        mBinding.aigcFragmentAigcPlus.setOnClickListener(this)
        mBinding.aigcFragmentAigcRecommendPhoto.setOnClickListener(this)
        flag = checkAndRequestPermissions()
    }

    //相当于跳转到了UCropActivity
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    data?.data?.let { uri ->
                        startCrop(uri)
                    }
                }

                UCrop.REQUEST_CROP -> {
                    data?.let {
                        val resultUri = UCrop.getOutput(it)
                        resultUri?.let { uri ->
                            mBinding.imageView.setImageURI(uri)
                            val path: String =
                                GetFilePathFromUri.getFileAbsolutePath(this.requireContext(), uri)
                            imagePath = path
                            imageContent = false
                        }
                    }
                }

                REQUEST_CODE_CAPTURE_IMAGE -> {
                    photoFile?.let {
                        val bitmap = BitmapFactory.decodeFile(it.absolutePath)
                        val compressedBitmap = compressBitmap(bitmap, 800, 800)
                        saveBitmapToFile(compressedBitmap, it)
                        mBinding.imageView.setImageURI(Uri.fromFile(it))
                        val path: String =
                            GetFilePathFromUri.getFileAbsolutePath(
                                this.requireContext(),
                                Uri.fromFile(it)
                            )
                        imagePath = path
                        imageContent = false
                    }
                }
            }
        }
    }
    private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun compressBitmap(bitmap: Bitmap, reqWidth: Int, reqHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val aspectRatio = width.toFloat() / height.toFloat()
        var newWidth = reqWidth
        var newHeight = reqHeight

        if (width > height) {
            newHeight = (reqWidth / aspectRatio).toInt()
        } else if (height > width) {
            newWidth = (reqHeight * aspectRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun startChat() {
        val usrInputText = mBinding.aigcFragmentAigcEt.text.toString()
        if (usrInputText.isNotEmpty()) {
            messages.add(ChatMessage(usrInputText, true))
            chatAdapter.notifyItemInserted(messages.size - 1)
            mBinding.aigcFragmentAigcEt.clearFocus()
            mBinding.aigcFragmentAigcEt.setText("")
            sessionFinished = false
            token++
            var ret = -1
            if (imagePath != null) {
                llm.clearHistory() //重新传图片前，需要清空memory，因为memory带有上一次图片的信息
                ret = llm.arun(usrInputText, readFileByBytes(imagePath!!), token) //首轮会话需要带上图片信息
            } else {
                ret = llm.arun(usrInputText, token) //多轮会话可以不用携带图片信息，SDK会在历史会话中自动拼接图片信息。
            }
            if (ret != 0) {
                return
            }
            toEnd()  // 确保在所有操作之后调用 toEnd
        }

    }

    override fun onClick(view: View) {
        val id = view.id
        clearRecyclerView()
        if (id == R.id.aigc_fragment_aigc_plus) {
            openGallery()
        } else if (id == R.id.aigc_fragment_aigc_recommend_photo) {
            if (flag) {
                openCamera()
            } else {
                shortToast("请打开相机权限")
            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            photoFile = createImageFile()
            photoFile?.let {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.redrockai.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_CODE_CAPTURE_IMAGE)
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            imagePath = absolutePath
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearRecyclerView() {
        messages.clear()
        chatAdapter.notifyDataSetChanged()
    }

    private fun readFileByBytes(fileName: String): ByteArray? {
        var `in`: FileInputStream? = null
        try {
            `in` = FileInputStream(fileName)
        } catch (e: FileNotFoundException) {
            Log.e("AEE", "readFileByBytes:$e")
        }
        var bytes: ByteArray? = null
        try {
            val out = ByteArrayOutputStream(1024)
            val temp = ByteArray(1024)
            var size = 0
            while ((`in`!!.read(temp).also { size = it }) != -1) {
                out.write(temp, 0, size)
            }
            `in`!!.close()
            bytes = out.toByteArray()
        } catch (e1: Exception) {
            e1.printStackTrace()
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return bytes
    }

    private fun initButtonClickListener() {
        mBinding.aigcFragmentAigcEt.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (imageContent) {
                Toast.makeText(this.context, "图片不能为空哟", Toast.LENGTH_SHORT).show()
                return@OnEditorActionListener false
            }
            if (sessionFinished) {
                startChat()
                toEnd()
                return@OnEditorActionListener true
            } else {
                Toast.makeText(this.context, "Busying! Please Wait", Toast.LENGTH_SHORT).show()
            }
            false
        }
        )
    }

    private fun startCrop(uri: Uri) {
        //必须要设置为System.currentTimeMillis()保证唯一性，否则第二次裁剪会无效
        val destinationUri =
            Uri.fromFile(File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg"))
        val options = UCrop.Options().apply {
            setFreeStyleCropEnabled(false)
            setCompressionFormat(Bitmap.CompressFormat.PNG)
        }
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withOptions(options)
            .start(requireContext(), this)
    }


    fun toEnd() {
        mBinding.rvAnswer.post {
            mBinding.rvAnswer.scrollToPosition(messages.size - 1)
            mBinding.nsScroll.post {
                mBinding.nsScroll.fullScroll(View.FOCUS_DOWN)
            }
        }
    }


    private fun setLLMConfig() {
        val llmConfig = LLMConfig.builder()
            .domain("image")
        val window_memory = Memory.windowMemory(5)
        llm = LLMFactory.imageUnderstanding(llmConfig, window_memory)
        llm.registerLLMCallbacks(llmCallbacks)
    }

}