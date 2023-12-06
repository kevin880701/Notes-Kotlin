package com.lhr.notes_kotlin.sqlLite

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = SqlModel.NOTES_TABLE_NAME)
class NotesEntity : Serializable  {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = SqlModel.recyclerPosition, typeAffinity = ColumnInfo.INTEGER)
    var recyclerPosition = 0

    @ColumnInfo(name = SqlModel.title, typeAffinity = ColumnInfo.TEXT)
    var title = ""

    @ColumnInfo(name = SqlModel.content, typeAffinity = ColumnInfo.TEXT)
    var content : String = ""

    @ColumnInfo(name = SqlModel.image, typeAffinity = ColumnInfo.INTEGER)
    var image = 0

    @ColumnInfo(name = SqlModel.number, typeAffinity = ColumnInfo.TEXT)
    var number = ""
}