package com.android.notesk.util.ChooseFile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.notesk.Adapter.ChooseFileAdapter
import com.android.notesk.Adapter.ChooseFileItemTouchHelper
import com.android.notesk.Model.Model.Companion.DATABASES_PATH
import com.android.notesk.Model.Model.Companion.KEEP_FILE
import com.android.notesk.Model.Model.Companion.OVER_FILE
import com.android.notesk.Model.Model.Companion.PACKAGE_FILES_PATH
import com.android.notesk.R
import com.android.notesk.SQLite.CsvToSql
import com.android.notesk.SQLite.NotesEntity
import com.android.notesk.Zip.UnZip
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_choose_file.*
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class ChooseFileActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var presenter : ChooseFilePresenter
    lateinit var adapter : ChooseFileAdapter

    companion object {
        var allList : ArrayList<NotesEntity> = ArrayList()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_choose_file)
        Stetho.initializeWithDefaults(this)

        presenter = ChooseFilePresenter(this)
        adapter = ChooseFileAdapter(presenter)

        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setAdapter(adapter)
        presenter.selectSql(adapter)

        DATABASES_PATH = this.getCacheDir().parent + "/databases"
        add.setOnClickListener(this)
        presenter.createNavigationView()

        menu.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add -> {
                presenter.editFilePage(null)
            }
            R.id.menu -> { // 側滑選單
                drawerLayout.openDrawer(GravityCompat.START)
            }
            R.id.recyclerView -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                OVER_FILE -> {
                    presenter.deleteTable()
                    var uri = intent?.data as Uri
                    allList.clear()
                    UnZip().unzip(uri, PACKAGE_FILES_PATH + File.separator, this)
                    CsvToSql(this, true)
                    presenter.selectSql(adapter)
                }
                KEEP_FILE -> {
                    var uri = intent?.data as Uri
                    UnZip().unzip(uri, PACKAGE_FILES_PATH + File.separator, this)
                    CsvToSql(this, false)
                    presenter.selectSql(adapter)
                }
            }
        }
    }
}
