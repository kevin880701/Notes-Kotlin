package com.android.notesk.SQLite

import android.content.Context
import android.database.Cursor
import android.util.Log
import com.android.notesk.Model.Model.Companion.DATABASES_PATH
import com.android.notesk.Model.Model.Companion.PACKAGE_FILES_PATH
import com.android.notesk.Model.Model.Companion.csvBackUp
import kotlinx.coroutines.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class SqlToCsv {
    var dataBase : NotesDatabase
    lateinit var cursor : Cursor

    constructor(mContext : Context) {
        dataBase = NotesDatabase(mContext)

        runBlocking {     // 阻塞主執行緒
            launch(Dispatchers.IO) {
                cursor = dataBase.getNotesDao().getCursor()
            }
        }
        var rowcount = 0
        var colcount = 0
        var sdCardDir = PACKAGE_FILES_PATH
        var filename = csvBackUp
        // the name of the file to export with
        var saveFile = File(sdCardDir, filename)
        var fw = FileWriter(saveFile);

        var bw = BufferedWriter(fw)
        rowcount = cursor.getCount()
        colcount = cursor.getColumnCount()
        if (rowcount > 0) {
            cursor.moveToFirst();
            for (i in 0 until colcount step 1) {
                if (i != colcount - 1) {
                    bw.write(cursor.getColumnName(i) + ",")
                } else {
                    bw.write(cursor.getColumnName(i))
                }
            }
            bw.newLine();

            for (i in 0 until rowcount step 1) {
                cursor.moveToPosition(i)
                for (j in 0 until colcount step 1) {
                    if (j != colcount - 1)
                        bw.write(cursor.getString(j) + ",")
                    else
                        bw.write(cursor.getString(j))
                }
                bw.newLine();
            }
            bw.flush()
        }
    }
}