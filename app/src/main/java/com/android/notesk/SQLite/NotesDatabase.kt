package com.android.notesk.SQLite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(NotesEntity::class)], version = 1)
abstract class NotesDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = SqlModel.DB_NAME
        @Volatile private var instance: NotesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            NotesDatabase::class.java, SqlModel.DB_NAME).build()
    }

    abstract fun getNotesDao(): NotesDao
}