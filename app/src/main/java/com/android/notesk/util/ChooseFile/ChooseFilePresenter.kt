package com.android.notesk.util.ChooseFile

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import com.android.notesk.Adapter.ChooseFileAdapter
import com.android.notesk.Adapter.ChooseFileItemTouchHelper
import com.android.notesk.Model.Model
import com.android.notesk.R
import com.android.notesk.SQLite.NotesDatabase
import com.android.notesk.SQLite.NotesEntity
import com.android.notesk.SQLite.SqlModel.Companion.DB_NAME
import com.android.notesk.SQLite.SqlToCsv
import com.android.notesk.Zip.ZipUtils
import com.android.notesk.util.ChooseFile.BackupChoose.BackupChoosePopupWindow
import com.android.notesk.util.ChooseFile.ChooseFileActivity.Companion.allList
import com.android.notesk.util.EditFile.EditFileActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_choose_file.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*


class ChooseFilePresenter(context: ChooseFileActivity) {
    val mContext = context
    val dataBase = NotesDatabase(context)
    lateinit var adapter : ChooseFileAdapter

    fun selectSql(adapter: ChooseFileAdapter) {
        runBlocking {     // 阻塞主執行緒
            launch(Dispatchers.IO) {
                allList = dataBase.getNotesDao().getAll() as ArrayList<NotesEntity>
                Collections.sort(allList,Comparator { o1, o2 -> o1.recyclerPosition - o2.recyclerPosition })
            }
        }

        ChooseFileItemTouchHelper(mContext.recyclerView, allList, adapter,mContext.presenter).helper
        this.adapter = adapter
        adapter.update(allList)

    }

    fun deleteTable() {
        val database = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath(DB_NAME), null)
        val dropTable = "DELETE FROM sqlite_sequence"
        database.execSQL(dropTable)
        runBlocking {     // 阻塞主執行緒
            launch(Dispatchers.IO) {
                dataBase.getNotesDao().deleteTable()
            }
        }
    }

    fun picGone(notesEntity: NotesEntity) {
        GlobalScope.launch {
            dataBase.getNotesDao().insert(notesEntity)
        }
    }

    fun editFilePage(notesEntity: NotesEntity?) {
        val intent = Intent(mContext, EditFileActivity::class.java)
        notesEntity?.let {
            val bundle = Bundle()
            bundle.putSerializable("notesEntity", notesEntity)
            intent.putExtras(bundle)
        }
        mContext.startActivity(intent)
    }

    fun createNavigationView() {
        mContext.navigationView.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                // 點選時收起選單
                mContext.drawerLayout.closeDrawer(GravityCompat.START)
                // 取得選項id
                val id: Int = item.getItemId()
                // 依照id判斷點了哪個項目並做相應事件
                when (id) {
                    R.id.make_backup -> {
                        SqlToCsv(mContext)
                        ZipUtils()  //製作備份檔
                        var shareIntent = Intent(Intent.ACTION_SEND)
                        val backupUri = FileProvider.getUriForFile(
                            mContext,
                            mContext.packageName + ".provider",
                            File(File(Model.PACKAGE_FILES_PATH).parent + File.separator + Model.BACKUP_NAME)
                        )
                        shareIntent.putExtra(Intent.EXTRA_STREAM, backupUri)
                        shareIntent.setType("*/*")
                        mContext.startActivity(shareIntent)
                        return true
                    }
                    R.id.update_data -> {
                        val choose = BackupChoosePopupWindow(mContext, this@ChooseFilePresenter)
                        val view: View = LayoutInflater.from(mContext).inflate(
                            R.layout.popup_window_backup_choose,
                            null
                        )
                        choose.showAtLocation(view, Gravity.CENTER, 0, 0)
                    }
                }
                return false
            }
        })
    }

    fun changeRecyclerPosition(starPosition: Int, endPosition: Int) {
        runBlocking {     // 阻塞主執行緒
            launch(Dispatchers.IO) {
                for (i in 0..Math.abs(endPosition - starPosition)) {
                    if (endPosition > starPosition) {
                        allList.get(starPosition + i).recyclerPosition = starPosition + i + 1
                        dataBase.getNotesDao().updateChangePosition(allList.get(starPosition + i).id,starPosition + i + 1)
                    } else {
                        allList.get(endPosition + i).recyclerPosition = endPosition + i + 1
                        dataBase.getNotesDao().updateChangePosition(allList.get(endPosition + i).id,endPosition + i + 1)
                    }
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun chooseBackup(requestCode: Int) {
        val picker = Intent(Intent.ACTION_GET_CONTENT)
        picker.type = "*/*"
        startActivityForResult(mContext, picker, requestCode, null)
    }
}
