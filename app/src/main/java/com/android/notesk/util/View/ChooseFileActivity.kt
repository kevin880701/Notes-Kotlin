package com.android.notesk.util.View

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.notesk.R
import com.android.notesk.Model.Model.Companion.ACCOUNT_CHOOSE
import com.android.notesk.Model.Model.Companion.DATABASES_PATH
import com.android.notesk.Model.MyAdapter
import com.android.notesk.util.Presenter.ChooseFilePresenter
import com.android.notesk.SQLite.NotesEntity
import kotlinx.android.synthetic.main.activity_choose_file.*


class ChooseFileActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var presenter : ChooseFilePresenter
    lateinit var adapter : MyAdapter

    companion object {
        var allList : ArrayList<NotesEntity> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_choose_file)

        presenter = ChooseFilePresenter(this)
        adapter = MyAdapter(presenter)

        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setAdapter(adapter)
        presenter.selectSql(adapter)
        DATABASES_PATH = this.getCacheDir().parent + "/databases"
        add.setOnClickListener(this)
        sync.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add -> {
                presenter.editFilePage(null)
            }
            R.id.sync -> {
                val choose = SyncPopupWindow(this,presenter)
                val view: View =LayoutInflater.from(this).inflate(R.layout.popupwindow_pic_change, null)
                choose.showAtLocation(view, Gravity.CENTER, 0, 0)
                //強制隱藏鍵盤
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
//                presenter.zipFile()  //製作備份檔
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ACCOUNT_CHOOSE -> {
                    Log.v("777","2222")
                    presenter.handleSignInResult(intent as Intent)
                }
            }
        }
    }
}
