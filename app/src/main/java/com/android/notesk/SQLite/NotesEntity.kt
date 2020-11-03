package com.android.notesk.SQLite

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = NotesEntity.TABLE_NAME)
class NotesEntity : Serializable  {

    companion object {
        const val TABLE_NAME = SqlModel.TABLE_NAME
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = SqlModel.recyclerPosition, typeAffinity = ColumnInfo.INTEGER)
    var recyclerPosition : Int = 0

    @ColumnInfo(name = SqlModel.title, typeAffinity = ColumnInfo.TEXT)
    var title = ""

    @ColumnInfo(name = SqlModel.content, typeAffinity = ColumnInfo.TEXT)
    var content = ""

    @ColumnInfo(name = SqlModel.picPath, typeAffinity = ColumnInfo.TEXT)
    var picPath : String = ""

//    operator fun compareTo(o: NotesEntity): Int {
//        return this.recyclerPosition
//    }
}