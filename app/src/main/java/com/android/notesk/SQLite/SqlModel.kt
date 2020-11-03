package com.android.notesk.SQLite

class SqlModel {
    companion object {
        const val DB_NAME : String = "Notes.db"
        const val TABLE_NAME : String = "Note"
        const val SQLITE_SEQUENCE : String = "sqlite_sequence"
        const val id : String = "id"
        const val title : String = "title"
        const val content : String = "content"
        const val picPath : String = "picPath"
        const val recyclerPosition : String = "recyclerPosition"
    }
}