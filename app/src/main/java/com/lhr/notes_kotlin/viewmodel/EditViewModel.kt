package com.lhr.notes_kotlin.viewmodel

import android.content.Context
import android.widget.ImageView
import com.lhr.notes_kotlin.sqlLite.NotesEntity

interface EditViewModel{

    fun save(notesEntity: NotesEntity)

    fun updateRecord(notesEntity: NotesEntity)

    fun deleteRecord(number: String)

    fun saveLocalNote(notesEntity: NotesEntity, context: Context, imageView: ImageView)

    fun deleteLocalFolder(context: Context, notesEntity: NotesEntity)
}