package com.example.module_teacher.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bumptech.glide.Glide
import com.example.module_teacher.R
import com.example.module_teacher.bean.CreateVideoRequest
import com.example.module_teacher.viewmodel.UploadViewModel
import com.example.module_teacher.databinding.ActivityCreateBinding
import com.example.module_teacher.util.replaceLastFiveCharactersWithJpg
import com.example.module_teacher.util.replaceLastFiveCharactersWithMp4
import com.example.module_teacher.viewmodel.CreateVideoModel
import com.example.redrockai.lib.utils.toast
import com.example.redrockai.lib.utils.view.gone
import com.example.redrockai.lib.utils.view.visible
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.github.ybq.android.spinkit.style.Wave
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateActivity : AppCompatActivity() {
    private val mBinding: ActivityCreateBinding by lazy {
        ActivityCreateBinding.inflate(layoutInflater)
    }
    private lateinit var coverFile: File
    private var isCovered = false
    private var isCovered1 = false
    private lateinit var progressBar: ProgressBar

    // 定义所需的权限和请求码
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private val uploadViewModel by lazy { ViewModelProvider(this)[UploadViewModel::class.java] }
    private val createViewModel by lazy { ViewModelProvider(this)[CreateVideoModel::class.java] }

    private lateinit var photoUri: Uri
    private lateinit var cover_url: String
    private lateinit var play_url: String

    private lateinit var pvtype: OptionsPickerView<String>
    private var index = 0
    private var isChanged = false
    private var selectedType: Int = -1
    private val typeList =
        mutableListOf("高等数学", "线性代数", "人工智能", "机器学习", "离散数学", "数据结构")

    private val permissionsRequestCode = 100

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private val takePhotoResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                startCropActivity(photoUri)
            } else {
                // 拍照取消或失败
                Toast.makeText(applicationContext, "拍照取消或失败", Toast.LENGTH_SHORT).show();
            }
        }
    private val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            check()
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initView()
        initTextListener()
        initListener()
        progressBar = mBinding.spinKit
        val wave: Sprite = Wave()
        progressBar.indeterminateDrawable = wave
        // 观察图片文件的 LiveData
        uploadViewModel.uploadResponse.observe(this, Observer { response ->
            Log.d("CreateActivity", "uploadFile response: ${response}")
            response?.let {
                if (it.code == 0) {
                    cover_url = it.url
                    isCovered = true
                    Log.d("hui", "Upload success: ${it.url}")
                } else {
                    Log.e("hui", "Upload failed with message: ${it.msg}")
                }
            } ?: run {
                Log.e("hui", "Upload failed")
            }
            check()
        })
        // 观察视频文件的 LiveData

        uploadViewModel.uploadResponseVideo.observe(this, Observer { response ->
            Log.d("hui", "uploadFile response: ${response}")
            response?.let {
                if (it.code == 0) {
                    play_url = it.url
                    Log.d("hui", "onCreate: ")
                    if (play_url.isNotEmpty()) {
                        mBinding.ivCoverVideo.visible()
                        isCovered1 = true
                        Glide.with(this)
                            .load(R.drawable.right)
                            .into(mBinding.ivCoverVideo)
                        mBinding.tvCoverVideo.gone()
                        progressBar.gone()
                        mBinding.ivCoverVideo.setOnClickListener(null)
                    } else {
                        Glide.with(this)
                            .load(R.drawable.error)
                            .into(mBinding.ivCoverVideo)
                        mBinding.ivCoverVideo.gone()
                        mBinding.tvCoverVideo.visible()
                        mBinding.tvCoverVideo.text="正在上传"
                        progressBar.visible()
                    }

                    Log.d("hui", "Upload success: ${it.url}")
                } else {
                    Log.e("hui", "Upload failed with message: ${it.msg}")
                }
            } ?: run {
                Log.e("hui", "Upload failed")
            }
            check()
        })
        createViewModel.response.observe(this, Observer { response ->
            if (response.isSuccessful) {
                val result = response.body()?.code
                if (result == 200) {
                    toast("上传成功")
                    finish()
                } else {
                    toast("上传失败")
                }
            } else { // 处理错误
                Log.d("hui", "onCreate: ")
                toast("上传失败")
            }
        })

    }

    fun check() {
        val name = mBinding.ufieldEtName.text.toString()
        val introduce = mBinding.etIntroduce.text.toString()
        if (name.isNotEmpty() && introduce.isNotEmpty() && isChanged && selectedType != -1) {
            if (isCovered && isCovered1) {
                val requestBody = CreateVideoRequest(selectedType, name, introduce, cover_url, play_url)
                mBinding.btCreate.apply {
                    setBackgroundResource(R.drawable.ufield_shape_createbutton2)
                    setOnClickListener {
                        createViewModel.createVideo(requestBody)
                        setOnClickListener(null)
                    }
                }
            }
        } else {
            mBinding.btCreate.setBackgroundResource(R.drawable.ufield_shape_createbutton)
            mBinding.btCreate.setOnClickListener(null)
        }
    }


    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    private fun initListener() {
        mBinding.tvChoose.setOnClickListener {
            pvtype = OptionsPickerBuilder(this) { p1, _, _, _ ->
                index = p1
                isChanged = true
                mBinding.tvChoose.text = typeList[index]
                selectedType = when (typeList[index]) {
                    "高等数学" -> 1
                    "线性代数" -> 2
                    "人工智能" -> 3
                    "机器学习" -> 4
                    "离散数学" -> 5
                    "数据结构" -> 6
                    else -> -1
                }
            }
                .setLayoutRes(R.layout.popup_activitytype_layout) {
                    it.findViewById<TextView>(R.id.ufield_tv_dialog_ensure).setOnClickListener {
                        pvtype.returnData()
                        pvtype.dismiss()
                    }
                    it.findViewById<TextView>(R.id.ufield_type_cancel).setOnClickListener {
                        pvtype.dismiss()
                    }
                }
                .setContentTextSize(16)
                .setLineSpacingMultiplier(2.5f)
                .setOutSideCancelable(false)
                .setSelectOptions(index)
                .build()

            pvtype.setPicker(typeList)
            val dialog = pvtype.dialog
            if (dialog != null) {
                val params = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
                )
                    .apply {
                        leftMargin = 0
                        rightMargin = 0
                    }
                pvtype.dialogContainerLayout.layoutParams = params

                val window = dialog.window
                window?.apply {
                    setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)
                    setGravity(Gravity.BOTTOM)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        setDimAmount(0.3f)
                    }
                }
            }
            pvtype.show()
        }
    }

    private fun initTextListener() {
        mBinding.ufieldEtName.addTextChangedListener(watcher)
        mBinding.tvChoose.addTextChangedListener(watcher)
        mBinding.etIntroduce.addTextChangedListener(watcher)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView() {
        mBinding.ufieldIvBack.setOnClickListener {
            finishAfterTransition()
        }
        mBinding.ivCover.setOnClickListener {
            getPhoto()
        }
        mBinding.ivCoverVideo.setOnClickListener {
            getVideo()
        }
        val hint = "关于课程的简介（不超过100个字）"

        val spannableStringBuilder = SpannableStringBuilder(hint)
        // 设置文字"（不超过100字）"的大小
        val limitSizeSpan = RelativeSizeSpan(0.8f)
        spannableStringBuilder.setSpan(
            limitSizeSpan,
            7,
            hint.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        mBinding.etIntroduce.hint = spannableStringBuilder


    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    @SuppressLint("CheckResult")
    fun getPhoto() {
        MaterialDialog(this).show {
            listItems(items = listOf("拍照", "从相册中选择")) { _, index, _ ->
                if (index == 0) {
                    takePhoto()
                } else {
                    getPhotoInPhotoAlbum.launch("image/*")
                }
            }

            cornerRadius(16F)
        }
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun takePhoto() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 请求权限
            ActivityCompat.requestPermissions(this, permissions, permissionsRequestCode)
        } else {
            // 已经拥有权限，直接启动相机
            launchCamera()
        }
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private val getPhotoInPhotoAlbum =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    contentResolver.openInputStream(uri)?.use { _ ->
                        startCropActivity(uri)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    private val getVideoInPhotoAlbum =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                contentResolver.openInputStream(uri)?.use { _ ->
                    startUploadVideo(uri)
                }
            }
        }

    private fun startUploadVideo(uri: Uri) {
        val tempFile = createTempFile("upload", ".mp4", cacheDir)
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            uploadViewModel.uploadFileVideo(tempFile)
            mBinding.ivCoverVideo.gone()
            mBinding.tvCoverVideo.visible()
            mBinding.tvCoverVideo.text="正在上传"
            progressBar.visible()
        } catch (e: Exception) {
            Log.e("startUploadVideo", "Failed to open input stream", e)
        }
    }


    @SuppressLint("CheckResult")
    private fun getVideo() {
        MaterialDialog(this).show {
            listItems(items = listOf("选择视频")) { _, index, _ ->
                if (index == 0) {
                    getVideoInPhotoAlbum.launch("video/*")
                }
            }

            cornerRadius(16F)
        }
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createPhotoFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "$packageName.fileProvider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        takePhotoResultLauncher.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun createPhotoFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmm ss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun startCropActivity(uri: Uri) {
        val destinationFile = createDestinationFile()
        val uCrop = UCrop.of(uri, Uri.fromFile(destinationFile))
        val options = UCrop.Options()
        options.setCropGridStrokeWidth(5)
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)
        options.setCompressionQuality(100)
        options.setToolbarColor(
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        )
        options.setStatusBarColor(
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        )
        uCrop.withOptions(options)
            .withAspectRatio(1F, 1F)
            .withMaxResultSize(convertDpToPx(106), convertDpToPx(106))
            .start(this)
    }

    private fun convertDpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    private fun createDestinationFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmm ss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, "cropped_$timeStamp.jpg")
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // 检查权限请求码
        if (requestCode == permissionsRequestCode) {
            // 检查权限授予结果
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已被授予，启动相机
                launchCamera()
            } else {
                // 权限被拒绝，显示一个提示或执行其他操作
                toast("需要相机和存储权限才能继续操作")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            // 获取裁剪后图片的实际文件路径
            val imageFile = File(resultUri!!.path)
            // 获取实际图片路径
            val imagePath = imageFile.absolutePath

            coverFile = convertToPNG(imagePath)?.let { File(it) }!!
            uploadViewModel.uploadFile(coverFile) // 触发上传文件的功能

            isCovered = true
            mBinding.ivCover.setImageURI(resultUri)
            mBinding.tvCover.gone()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            toast("裁剪失败")
        }
    }


    private fun convertToPNG(filePath: String): String? {
        val originalFile = File(filePath)

        // 检查文件是否存在
        if (!originalFile.exists()) {
            return null
        }

        // 检查文件是否已经是 PNG 格式
        if (isPNGFile(originalFile)) {
            return filePath
        }

        // 加载原始图片文件为 Bitmap
        val originalBitmap = BitmapFactory.decodeFile(filePath)

        // 创建目标文件的路径
        val targetFilePath = originalFile.parent + File.separator + "converted.png"
        val targetFile = File(targetFilePath)

        try {
            // 创建目标 Bitmap，格式为 ARGB_8888
            val targetBitmap = Bitmap.createBitmap(
                originalBitmap.width,
                originalBitmap.height,
                Bitmap.Config.ARGB_8888
            )

            // 在目标 Bitmap 上绘制原始 Bitmap
            val canvas = Canvas(targetBitmap)

            canvas.drawBitmap(originalBitmap, 0f, 0f, null)

            // 将目标 Bitmap 保存为 PNG 文件
            val outputStream = FileOutputStream(targetFile)
            targetBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()

            // 回收原始 Bitmap 和目标 Bitmap 的资源
            originalBitmap.recycle()
            targetBitmap.recycle()

            return targetFilePath
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun isPNGFile(file: File): Boolean {
        return file.extension.equals("png", true)
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            val v = currentFocus

            //如果不是落在EditText区域，则需要关闭输入法
            if (hideKeyboard(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                v?.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private fun hideKeyboard(view: View?, event: MotionEvent): Boolean {
        if (view != null && view is EditText) {

            val location = intArrayOf(0, 0)
            view.getLocationInWindow(location)

            //获取现在拥有焦点的控件view的位置，即EditText
            val left = location[0]
            val top = location[1]
            val bottom = top + view.height
            val right = left + view.width
            //判断我们手指点击的区域是否落在EditText上面，如果不是，则返回true，否则返回false
            val isInEt = (event.x > left && event.x < right && event.y > top
                    && event.y < bottom)
            return !isInEt
        }
        return false
    }
}