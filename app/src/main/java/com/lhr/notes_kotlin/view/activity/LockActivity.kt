package com.lhr.notes_kotlin.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.lhr.notes_kotlin.AppConfig.BACKUP_PATH
import com.lhr.notes_kotlin.AppConfig.DATABASES_PATH
import com.lhr.notes_kotlin.AppConfig.FILES_PATH
import com.lhr.notes_kotlin.AppConfig.KEY
import com.lhr.notes_kotlin.R
import com.lhr.notes_kotlin.databinding.ActivityLockBinding
import com.lhr.notes_kotlin.permission.PermissionManager
import com.lhr.notes_kotlin.view.base.BaseActivity
import com.lhr.notes_kotlin.viewmodel.implementations.LockViewModelImpl
import timber.log.Timber
import java.io.File


class LockActivity : BaseActivity(), View.OnClickListener {

    val PERMISSION_REQUEST_CODE = 500
    private val permissionManager: PermissionManager by lazy { PermissionManager.getInstance(this) }
    private val viewModel: LockViewModelImpl by lazy { ViewModelProvider(this)[LockViewModelImpl::class.java] }
    private var _binding: ActivityLockBinding? = null
    val binding get() = _binding!!
    var isSetting = false //用於判斷是否有盡到設定手動更新權限
    
    lateinit var vibrator: Vibrator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission(this)


        FILES_PATH = getExternalFilesDir("Notes")!!.absolutePath
        BACKUP_PATH = getExternalFilesDir("Backup")!!.absolutePath
        DATABASES_PATH = cacheDir.parent?.plus("/databases")!!
        Log.v("@@@","" + FILES_PATH)
        Log.v("@@@","" + BACKUP_PATH)
        Log.v("@@@","" + DATABASES_PATH)
        // 確認識別檔是否存在
        val file = File("$FILES_PATH/$KEY")
        if (!file.exists()) {
            file.createNewFile()
        }


        // 獲取震動服務(已過時，尚未找到新方法)
//        @SuppressLint("ServiceCast")
//        vibrator = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as Vibrator
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        binding.textViewKeyboard0.setOnClickListener(this)
        binding.textViewKeyboard1.setOnClickListener(this)
        binding.textViewKeyboard2.setOnClickListener(this)
        binding.textViewKeyboard3.setOnClickListener(this)
        binding.textViewKeyboard4.setOnClickListener(this)
        binding.textViewKeyboard5.setOnClickListener(this)
        binding.textViewKeyboard6.setOnClickListener(this)
        binding.textViewKeyboard7.setOnClickListener(this)
        binding.textViewKeyboard8.setOnClickListener(this)
        binding.textViewKeyboard9.setOnClickListener(this)
        binding.textViewReal.setOnClickListener(this)
    }

    fun checkPermission(activity: Activity){
        var permissionManager = PermissionManager(activity)
        if (permissionManager.isCameraPermissionGranted() &&
            permissionManager.isWriteExternalStoragePermissionGranted() &&
            permissionManager.isReadExternalStoragePermissionGranted()
        ) {
//            val intent = Intent(activity, LockActivity::class.java)
//            activity.startActivity(intent)
//            activity.finish()
        } else {
            // 如果沒有權限，您可以向使用者要求該權限
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    PermissionManager.WRITE_EXTERNAL_STORAGE,
                    PermissionManager.READ_EXTERNAL_STORAGE,
                    PermissionManager.CAMERA
                ), PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.textViewKeyboard0 -> {viewModel.count(this, vibrator)}
            R.id.textViewKeyboard1 -> {viewModel.count(this, vibrator)}
            R.id.textViewKeyboard2 -> {viewModel.count(this, vibrator)}
            R.id.textViewKeyboard3 -> {viewModel.count(this, vibrator)}
            R.id.textViewKeyboard4 -> {viewModel.count(this, vibrator)}
            R.id.textViewKeyboard5 -> {viewModel.count(this, vibrator)}
            R.id.textViewKeyboard6 -> {viewModel.count(this, vibrator)}
            R.id.textViewKeyboard7 -> {viewModel.count(this, vibrator)}
            R.id.textViewKeyboard8 -> {viewModel.count(this, vibrator)}
            R.id.textViewKeyboard9 -> {viewModel.count(this, vibrator)}
            R.id.textViewReal -> {viewModel.real(this)}
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // 判斷權限是否都開啟
                if ((grantResults.isNotEmpty() && grantResults.all { it == 0 })
                ) {
                    // 有權限並重新啟動Cover頁面
                    val intent = Intent(this, LockActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    // 無權限
                    AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("權限未開啟")
                        .setMessage("請先前往[設定]>[應用程式]開啟權限")
                        .setPositiveButton(
                            "開啟設定",
                            DialogInterface.OnClickListener { _, _ ->
                                isSetting = true
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            })
                        .setNegativeButton(
                            "取消",
                            DialogInterface.OnClickListener { _, _ ->
                                finish()
                            })
                        .show()
                }
                return
            }
        }
    }
}