package com.android.notesk.SQLite

import android.content.Context
import android.util.Log
import com.android.notesk.Model.Model.Companion.PACKAGE_FILES_PATH
import com.android.notesk.Model.Model.Companion.csvBackUp
import com.android.notesk.util.ChooseFile.ChooseFileActivity.Companion.allList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.properties.Delegates

class CsvToSql {
    var dataBase : NotesDatabase
    val filepath = PACKAGE_FILES_PATH + File.separator + csvBackUp
    var linePosition = 0
    var maxRecyclerPosition by Delegates.notNull<Int>()

    constructor(mContext : Context,state : Boolean) {
        dataBase = NotesDatabase(mContext)

        try {
            val file = FileReader(filepath)
            val buffer = BufferedReader(file)
            var line : String? = ""
            if(allList.size == 0){
                maxRecyclerPosition = 0
            }else{
                maxRecyclerPosition = allList.get(allList.size-1).recyclerPosition
            }

            while (buffer.readLine().also({line = it}) != null) {
                var str = (line as String).split(",".toRegex(), 5)
                var notesEntity = NotesEntity()


                if(linePosition > 0){
                    if(state){
                        maxRecyclerPosition++
                        if(str[1].equals("")){
                            notesEntity.recyclerPosition = maxRecyclerPosition
                        }else{
                            notesEntity.recyclerPosition = Integer.parseInt(str[1])
                        }
//                        Log.v("PPPPP","" + str[1] + str[2])
                    }else{
                        maxRecyclerPosition++
                        notesEntity.recyclerPosition = maxRecyclerPosition
                    }
                    notesEntity.title = str[2]
                    notesEntity.content = str[3]
                    notesEntity.picPath = str[4]

                    runBlocking {     // 阻塞主執行緒
                        launch(Dispatchers.IO) {
                            dataBase.getNotesDao().insert(notesEntity)
                        }
                    }
                }
                linePosition++
            }
        }catch (e : Exception) {
            Log.e("CsvToSqlError","" + e)
        }
    }
}
