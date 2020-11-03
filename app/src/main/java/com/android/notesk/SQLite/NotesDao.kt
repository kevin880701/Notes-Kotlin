package com.android.notesk.SQLite

import android.database.Cursor
import androidx.room.*
import com.android.notesk.SQLite.SqlModel.Companion.TABLE_NAME
import com.android.notesk.SQLite.SqlModel.Companion.id
import com.android.notesk.SQLite.SqlModel.Companion.recyclerPosition

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: NotesEntity)

    @Query("SELECT * FROM " + TABLE_NAME)
    fun getAll(): List<NotesEntity>

    @Query("SELECT * FROM " + TABLE_NAME)
    fun getCursor(): Cursor

    @Query("DELETE FROM " + TABLE_NAME)
    fun deleteTable()

    @Query("UPDATE " + TABLE_NAME + " SET " + recyclerPosition + " = :recyclerPosition WHERE " + id + " = :id")
    fun updateChangePosition(id: Int, recyclerPosition: Int)

    @Query("UPDATE " + TABLE_NAME + " SET " + recyclerPosition + " = :newId WHERE " + recyclerPosition + " = :oldId")
    fun delUpdateId(oldId: Int, newId: Int)

    @Delete
    fun delete(item: NotesEntity)
}