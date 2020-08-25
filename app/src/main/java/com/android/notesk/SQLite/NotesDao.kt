package com.android.notesk.SQLite

import androidx.room.*

@Dao
interface NotesDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(item: NotesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: NotesEntity)

    @Query("SELECT * FROM " + SqlModel.TABLE_NAME + " WHERE id LIKE :id")
    fun findById(id: Int): NotesEntity

    @Query("SELECT * FROM " + SqlModel.TABLE_NAME)
    fun getAll(): List<NotesEntity>

    @Delete
    fun delete(item: NotesEntity)

    @Update
    fun update(item: NotesEntity)
}