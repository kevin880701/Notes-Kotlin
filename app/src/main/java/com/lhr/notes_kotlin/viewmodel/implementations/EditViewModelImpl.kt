package com.lhr.notes_kotlin.viewmodel.implementations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.api.client.json.Json
import com.google.gson.Gson
import com.lhr.notes.data.NotesRepository
import com.lhr.notes_kotlin.AppConfig
import com.lhr.notes_kotlin.data.NoteData
import com.lhr.notes_kotlin.sqlLite.NotesEntity
import com.lhr.notes_kotlin.sqlLite.SqlDatabase
import com.lhr.notes_kotlin.util.showToast
import com.lhr.notes_kotlin.viewmodel.EditViewModel
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException

class EditViewModelImpl : EditViewModel, ViewModel() {
    private val repository = NotesRepository.getInstance(SqlDatabase.getInstance().getNotesDao())

    /**
     * 儲存紀錄。
     * @param notesEntity 要儲存的紀錄
     */
    override fun save(notesEntity: NotesEntity) {
        repository.insertNotes(notesEntity)
    }

    /**
     * 更新紀錄。
     * @param notesEntity 要更新的紀錄
     */
    override fun updateRecord(notesEntity: NotesEntity) {
        repository.updateNotes(notesEntity)
    }

    /**
     * 刪除紀錄。
     * @param notesEntity 要刪除的紀錄
     */
    override fun deleteRecord(number: String) {
        repository.deleteNotes(number)
    }

    /**
     * 將內容存於本地資料夾
     * @param notesEntity 要寫入本地資料夾的紀錄
     * @param context
     * @param imageView 要取得的圖片元件
     */
    override fun saveLocalNote(notesEntity: NotesEntity, context: Context, imageView: ImageView) {
        //創建一個名為 notesEntity.number 的資料夾，並確保目錄存在
        val number = notesEntity.number
        val directory = File(AppConfig.FILES_PATH, number)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        //在名稱notesEntity.number的資料夾中創建一個TXT文件，將 title 和 content 寫入其中
        val file = File(directory, "data.json")
        try {
            // 如果文件已經存在，先刪除它
            if (file.exists()) {
                file.delete()
            }
            var noteData = NoteData(notesEntity.title, notesEntity.content)

            val gson = Gson()
            val jsonString = gson.toJson(noteData)
            file.writeText(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //將 ImageView 的圖像保存到該資料夾中
        val imageFile = File(directory, "image.png")
        if (imageView.drawable != null) {
            // 如果有設置圖片則檢查資料夾有無舊圖片，有則覆蓋儲存
            if (imageView.drawable is BitmapDrawable) {
//            val bitmap = imageView.bitmap

                val bitmapDrawable = imageView.drawable as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap

                try {
                    // 如果圖像文件已經存在，先刪除它
                    if (imageFile.exists()) {
                        imageFile.delete()
                    }

                    val outputStream = FileOutputStream(imageFile)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            // 如果沒有設置圖片則檢查資料夾有無舊圖片，有則刪除
            }else if(imageView.drawable is VectorDrawable){

                // 文件存在且是文件类型，可以进行删除操作
                if (imageFile.delete()) {
                    // 删除成功
                } else {
                    // 删除失败，处理失败情况
                }
            }
        }
    }

    /**
     * 刪除紀錄。
     * @param context
     * @param notesEntity 要刪除的紀錄
     */
    override fun deleteLocalFolder(context: Context, notesEntity: NotesEntity) {
        // 要刪除的資料夾的路徑
        val directory = File(AppConfig.FILES_PATH, notesEntity.number)
        // 刪除資料夾
        if (directory.exists() && directory.isDirectory) {
            val deleted = directory.deleteRecursively()
            val message = if (deleted) {
                "刪除${notesEntity.title}"
            } else {
                "刪除${notesEntity.title}失敗"
            }
            showToast(context, message)
        } else {
            // 處理資料夾不存在或不是資料夾的情況
            showToast(context,  "Title：${notesEntity.title} number：${notesEntity.number}檔案錯誤")
        }
    }
}