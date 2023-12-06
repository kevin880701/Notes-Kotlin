package com.lhr.notes_kotlin.util.manager

import android.util.Log
import com.lhr.notes.data.NotesRepository
import com.lhr.notes_kotlin.sqlLite.NotesEntity
import com.lhr.notes_kotlin.sqlLite.SqlDatabase
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.content
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.image
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.number
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.recyclerPosition
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.title
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.Scanner

object csvManager {
    /**
     * 將資料庫轉入CSV
     * @param csvFilePath CSV檔案路徑
     */
    fun sqlToCsv(csvFilePath: String) {
        val repository = NotesRepository.getInstance(SqlDatabase.getInstance().getNotesDao())
        val csvFile = File(csvFilePath)
        try {
            csvFile.printWriter().use { out ->
                runBlocking {     // 阻塞主執行緒
                    launch(Dispatchers.IO) {
                        var notesList = repository.loadNotes()
                        // CSV 文件的標題行
                        out.println("$recyclerPosition,$title,$content,$image,$number")

                        // 將數據每一行寫入 CSV 文件
                        for (notesEntity in notesList) {
                            out.println("${notesEntity.recyclerPosition},${notesEntity.title},${notesEntity.content},${notesEntity.image},${notesEntity.number}")
                        }
                    }
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 將CSV資料轉入資料庫
     * @param csvFilePath CSV檔案路徑
     */
    fun csvToSql(csvFilePath: String) {
        try {
            FileReader(csvFilePath).use { fileReader ->
                val csvReader = CSVReaderBuilder(fileReader)
                    .withSkipLines(1) // 跳過表頭行
                    .build()

                var record: Array<String>?
                while (csvReader.readNext().also { record = it } != null) {
                    // 檢查欄位數量
                    if (record!!.size >= 5) {
                        val recyclerPosition = record!![0].toInt()
                        val title = record!![1]
                        val content = record!![2]
                        val image = record!![3].toInt()
                        val number = record!![4]

                        // 創建 NotesEntity 對象
                        val notesEntity = NotesEntity()
                        notesEntity.recyclerPosition = recyclerPosition
                        notesEntity.title = title
                        notesEntity.content = content
                        notesEntity.image = image
                        notesEntity.number = number

                        // 插入數據到資料庫
                        SqlDatabase.getInstance().getNotesDao().insert(notesEntity)
                    }
                }

                println("CSV data has been imported to Room database using OpenCSV.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}