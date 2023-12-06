package com.lhr.notes.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.lhr.notes_kotlin.sqlLite.NotesDao
import com.lhr.notes_kotlin.sqlLite.NotesEntity
import com.lhr.notes_kotlin.sqlLite.SqlDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NotesRepository private constructor(private val noteDao: NotesDao) {

    companion object{
        private var instance: NotesRepository?=null
        fun getInstance(noteDao: NotesDao): NotesRepository {
            if (instance==null) {
                instance = NotesRepository(noteDao)
            }
            return instance!!
        }
    }

    /**
     * 抓取資料庫全部記錄
     */
    fun loadNotes() = SqlDatabase.getInstance().getNotesDao().getAll()

    /**
     * 插入新增的紀錄。
     * @param notesEntity 要新增的紀錄
     */
    fun insertNotes(notesEntity: NotesEntity) = SqlDatabase.getInstance().getNotesDao().insert(notesEntity)

    /**
     * 更新紀錄。
     * @param notesEntity 要更新的紀錄
     */
    fun updateNotes(notesEntity: NotesEntity) = SqlDatabase.getInstance().getNotesDao()
        .updateNotesByNumber(notesEntity.number, notesEntity.title, notesEntity.content, notesEntity.image)

    /**
     * 刪除紀錄。
     * @param number 要被刪除的紀錄代號
     */
    fun deleteNotes(number: String) = SqlDatabase.getInstance().getNotesDao()
        .deleteNotesByNumber(number)


}