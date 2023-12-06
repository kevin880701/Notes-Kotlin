package com.lhr.notes_kotlin.sqlLite

import androidx.room.*
import androidx.room.RoomMasterTable.TABLE_NAME
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.NOTES_TABLE_NAME
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.content
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.id
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.image
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.number
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.recyclerPosition
import com.lhr.notes_kotlin.sqlLite.SqlModel.Companion.title

@Dao
interface NotesDao {
    @Query("SELECT * FROM $NOTES_TABLE_NAME")
    fun getAll(): List<NotesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: NotesEntity)

    @Query("SELECT * FROM  $NOTES_TABLE_NAME WHERE $number = :number")
    fun getNotesByNumber(number: String): NotesEntity

    @Query("UPDATE $NOTES_TABLE_NAME SET $title = :newTitle, $content = :newContent, $image = :image WHERE $number = :number")
    fun updateNotesByNumber(number: String, newTitle: String, newContent: String, image: Int)

    @Query("DELETE FROM $NOTES_TABLE_NAME WHERE $number = :number")
    fun deleteNotesByNumber(number: String)

    /**
     * 清除資料表
     */
    @Query("DELETE FROM $NOTES_TABLE_NAME")
    fun clearTable()
}