package com.lhr.notes_kotlin.util

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date


/**
 * 以Glide載入圖片
 * @param imageFile 載入的圖片
 * @param imageView 要載入圖片的元件
 */
fun ImageView.loadImageWithGlide(imageFile: Uri, imageView: ImageView) {
    Glide.with(this)
        .load(imageFile)
        .diskCacheStrategy(DiskCacheStrategy.NONE) // 禁用磁盤緩存
        .skipMemoryCache(true) // 禁用內存緩存
        .into(imageView)
}

/**
 * 顯示Toast通知
 * @param context
 * @param message 要顯示的文字
 */
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


/**
 * 刪除指定目錄裡的所有檔案
 * @param directoryPath 指定的目錄
 */
fun deleteAllFilesInDirectory(directoryPath: String) {
    val directory = File(directoryPath)
    if (directory.exists()) {
        val contents = directory.listFiles()

        if (contents != null) {
            for (file in contents) {
                if (file.isDirectory) {
                    // 遞歸刪除子目錄
                    deleteAllFilesInDirectory(file.path)
                } else {
                    // 刪除文件
                    file.delete()
                }
            }
        }
    }
}