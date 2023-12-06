package com.lhr.notes_kotlin.sqlLite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.DB_NAME

@Database(entities = [NotesEntity::class], version = 1, exportSchema = false)
abstract class SqlDatabase : RoomDatabase() {

    companion object {
        private var instance: SqlDatabase?=null
        fun getInstance(): SqlDatabase {
            return instance!!
        }
        fun init(context: Context): SqlDatabase {
            return instance ?:Room.databaseBuilder(context, SqlDatabase::class.java,DB_NAME)
                .allowMainThreadQueries()
                .build().also {
                    instance = it
                }
        }
    }

    abstract fun getNotesDao(): NotesDao
}