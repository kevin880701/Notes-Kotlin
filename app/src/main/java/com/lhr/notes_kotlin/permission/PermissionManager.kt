package com.lhr.notes_kotlin.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.lhr.notes.data.NotesRepository
import com.lhr.notes_kotlin.sqlLite.NotesDao

/**
 * 權限管理
 */
class PermissionManager(mActivity: Activity) {
    var mActivaty = mActivity

    companion object {
         const val CAMERA = Manifest.permission.CAMERA
         const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
         const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

        private var instance: PermissionManager? = null
        fun getInstance(activity: Activity): PermissionManager {
            if (instance == null) {
                instance = PermissionManager(activity)
            }
            return instance!!
        }
    }
    fun isPermissionGranted(name: String) = ContextCompat.checkSelfPermission(
        mActivaty, name
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * 相機權限
     */
    fun isCameraPermissionGranted() = ContextCompat.checkSelfPermission(
        mActivaty, CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * 寫入資料夾權限
     */
    fun isWriteExternalStoragePermissionGranted() = ContextCompat.checkSelfPermission(
        mActivaty, WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * 讀取資料夾權限
     */
    fun isReadExternalStoragePermissionGranted() = ContextCompat.checkSelfPermission(
        mActivaty, READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

}