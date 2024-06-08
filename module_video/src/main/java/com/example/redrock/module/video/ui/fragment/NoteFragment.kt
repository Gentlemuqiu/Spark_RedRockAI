package com.example.redrock.module.video.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.redrock.module.video.databinding.FragmentNoteBinding
import com.example.redrockai.lib.utils.BaseApp
import com.yalantis.ucrop.UCrop
import jp.wasabeef.richeditor.RichEditor
import java.io.File
import java.io.IOException

class NoteFragment : Fragment() {
    private var _noteBinding: FragmentNoteBinding? = null
    private val binding: FragmentNoteBinding get() = _noteBinding!!
    private lateinit var mEditor: RichEditor


    private var noteId: String? = "lyt"

    private val REQUEST_CODE_PICK_IMAGE = 1
    private val REQUEST_CODE_CROP_IMAGE = 2
    private val PREFS_NAME = "NotePref"
    private val PREF_IMAGE_URI = "NoteImage"


    //todo：一定要确保noteID赋值，我这里兜底措施，lyt，
    //定时保存笔记
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {


            val content = binding.editor.html
            if (content != null) {
                saveNoteToPreferences(noteId!!, content)
            }
            sendEmptyMessageDelayed(0, 1000)//每隔1s笔记自动保存


        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _noteBinding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId = requireActivity().intent.getIntExtra("id", 0).toString()
        initNoteView()
        // 读取本地笔记内容
        val noteContent = readNoteFromPreferences(noteId!!)
        noteContent?.let {
            binding.editor.html = it
        }

        //2秒后开始笔记的自动保存
        handler.sendEmptyMessageDelayed(0, 2000)


    }

    /**
     * 初始化笔记参数
     */
    private fun initNoteView() {
        mEditor = binding.editor
        mEditor.setEditorHeight(200)
        mEditor.setEditorFontSize(22)
        mEditor.setEditorFontColor(Color.RED)
        mEditor.setPadding(10, 10, 10, 10)
        mEditor.setPlaceholder("Insert text here...")


//        mEditor.setOnTextChangeListener { text ->
//            binding.preview.text = text
//        }

        binding.actionUndo.setOnClickListener {
            mEditor.undo()
        }

        binding.actionRedo.setOnClickListener {
            mEditor.redo()
        }

        binding.actionBold.setOnClickListener {
            mEditor.setBold()
        }

        binding.actionItalic.setOnClickListener {
            mEditor.setItalic()
        }

        binding.actionSubscript.setOnClickListener {
            mEditor.setSubscript()
        }

        binding.actionSuperscript.setOnClickListener {
            mEditor.setSuperscript()
        }

        binding.actionStrikethrough.setOnClickListener {
            mEditor.setStrikeThrough()
        }

        binding.actionUnderline.setOnClickListener {
            mEditor.setUnderline()
        }

        binding.actionHeading1.setOnClickListener {
            mEditor.setHeading(1)
        }

        binding.actionHeading2.setOnClickListener {
            mEditor.setHeading(2)
        }

        binding.actionHeading3.setOnClickListener {
            mEditor.setHeading(3)
        }

        binding.actionHeading4.setOnClickListener {
            mEditor.setHeading(4)
        }

        binding.actionHeading5.setOnClickListener {
            mEditor.setHeading(5)
        }

        binding.actionHeading6.setOnClickListener {
            mEditor.setHeading(6)
        }

        binding.actionTxtColor.setOnClickListener(object : View.OnClickListener {
            var isChanged = false
            override fun onClick(v: View) {
                mEditor.setTextColor(if (isChanged) Color.BLACK else Color.RED)
                isChanged = !isChanged
            }
        })

        binding.actionBgColor.setOnClickListener(object : View.OnClickListener {
            var isChanged = false
            override fun onClick(v: View) {
                mEditor.setTextBackgroundColor(if (isChanged) Color.TRANSPARENT else Color.YELLOW)
                isChanged = !isChanged
            }
        })

        binding.actionIndent.setOnClickListener {
            mEditor.setIndent()
        }

        binding.actionOutdent.setOnClickListener {
            mEditor.setOutdent()
        }

        binding.actionAlignLeft.setOnClickListener {
            mEditor.setAlignLeft()
        }

        binding.actionAlignCenter.setOnClickListener {
            mEditor.setAlignCenter()
        }

        binding.actionAlignRight.setOnClickListener {
            mEditor.setAlignRight()
        }

        binding.actionBlockquote.setOnClickListener {
            mEditor.setBlockquote()
        }

        binding.actionInsertBullets.setOnClickListener {
            mEditor.setBullets()
        }

        binding.actionInsertNumbers.setOnClickListener {
            mEditor.setNumbers()
        }

        binding.actionInsertImage.setOnClickListener {
            openGallery()
        }



        binding.actionInsertAudio.setOnClickListener {
            mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3")
        }
//
//        binding.actionInsertVideo.setOnClickListener {
//            mEditor.insertVideo(
//                "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4",
//                360
//            )
//        }

        binding.actionInsertLink.setOnClickListener {
            mEditor.insertLink("https://github.com/wasabeef", "wasabeef")
        }

        binding.actionInsertCheckbox.setOnClickListener {
            mEditor.insertTodo()
        }


    }


    //把笔记保存起来
    fun saveNoteToPreferences(noteId: String, content: String) {
        val sharedPreferences: SharedPreferences =
            BaseApp.getAppContext().getSharedPreferences("notes", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(noteId, content)
        editor.apply()
    }

    private fun readNoteFromPreferences(noteId: String): String? {
        val sharedPreferences: SharedPreferences =
            BaseApp.getAppContext().getSharedPreferences("notes", Context.MODE_PRIVATE)
        return sharedPreferences.getString(noteId, null)
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
                        resultUri?.let {
                            val base64String = uriToBase64(requireContext(), it)
                            Log.d("ewfawfwefaw", "测试数据${base64String}")

                            base64String?.let { encodedString ->

                                val imageHtml =
                                    "<img src='data:image/jpeg;base64,$encodedString' />"

                                binding.editor.insertImage(encodedString, "localImage", 50)
                            }
                        }


                    }
                }
            }
        }
    }


    // 将 Uri 转换为 Base64 编码的字符串
    private fun uriToBase64(context: Context, uri: Uri): String? {
        val imageBytes = getBytesFromUri(context, uri)
        return encodeImageToBase64(imageBytes)
    }

    private fun getBytesFromUri(context: Context, uri: Uri): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.buffered()?.use { it.readBytes() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    private fun encodeImageToBase64(imageBytes: ByteArray?): String? {
        return if (imageBytes != null) {
            Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } else {
            null
        }
    }


    private fun insertImageToEditor(imageUri: Uri) {
        val imagePath = imageUri.path // 使用 Uri 的路径
        if (imagePath != null) {
            binding.editor.insertImage(imagePath, "Local Image")
        } else {
            Toast.makeText(context, "Unable to get image path", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPathFromUri(context: Context, uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = it.getString(columnIndex)
            }
        }
        return path
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

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun saveImageUri(uri: Uri) {
        val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE).edit()
        prefs.putString(PREF_IMAGE_URI, uri.toString())
        prefs.apply()
    }

    private fun getSavedImageLogo(): String {
        //加载已经有的
        val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE)
        val savedImageUri = prefs.getString(PREF_IMAGE_URI, "")
        return savedImageUri!!
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _noteBinding = null
    }


}