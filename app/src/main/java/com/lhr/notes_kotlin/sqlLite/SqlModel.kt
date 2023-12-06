package com.lhr.notes_kotlin.sqlLite

class SqlModel {
    companion object {
        const val DB_NAME : String = "Notes.db"
        const val NOTES_TABLE_NAME : String = "NotesTable"
        const val id : String = "id"
        const val recyclerPosition : String = "recyclerPosition"
        const val title : String = "title"
        const val content : String = "content"
        const val image : String = "image"
        const val number : String = "number"
    }
}