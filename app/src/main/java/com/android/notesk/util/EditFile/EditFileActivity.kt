package com.android.notesk.util.EditFile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.android.notesk.Model.Model.Companion.IMAGE_REQUEST_CODE
import com.android.notesk.Model.Model.Companion.comma
import com.android.notesk.Model.Model.Companion.slashN
import com.android.notesk.R
import com.android.notesk.SQLite.NotesEntity
import com.android.notesk.ScrollView.ScrollBindHelper
import kotlinx.android.synthetic.main.activity_edit_file.*
import java.io.File
import android.widget.Toast

import android.content.DialogInterface
import android.util.Log
import com.android.notesk.util.ChooseFile.ChooseFileActivity.Companion.allList


class EditFileActivity : AppCompatActivity(), View.OnClickListener  {

    var status : Boolean = false
    lateinit var id : Integer
    lateinit var picUri: Uri
    lateinit var path: String
    lateinit var getNotesEntity : NotesEntity
    var saveNotesEntity = NotesEntity()
    lateinit var presenter : EditFilePresenter
    lateinit var scrollBindHelper: ScrollBindHelper

    companion object {
        var isChangePic: Boolean = false
        var isPicExists: Boolean = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_edit_file)
        editContent.movementMethod = ScrollingMovementMethod.getInstance()

        path = getExternalFilesDir(null)!!.absolutePath
        presenter = EditFilePresenter(this)

        val getIntent = this.intent
        val getBundle: Bundle? = getIntent.getExtras()

        getBundle?.let {
            status = true
            getNotesEntity = getBundle.getSerializable("notesEntity") as NotesEntity
            saveNotesEntity.id = getNotesEntity.id
            saveNotesEntity.recyclerPosition = getNotesEntity.recyclerPosition
            if (!getNotesEntity.picPath.equals("")) {
                if (File(getNotesEntity.picPath).exists()) {
                    isPicExists = true
                    saveNotesEntity.picPath = getNotesEntity.picPath
                    addPic.setImageBitmap(BitmapFactory.decodeFile(getNotesEntity.picPath))
                }
            }
            fileTitle.setText(getNotesEntity.title)
            editTitle.setText(getNotesEntity.title.replace(comma, ",").replace(slashN, "\n"))
            editContent.setText(getNotesEntity.content.replace(comma, ",").replace(slashN, "\n"))
        }
        fileTitle.isSelected = true

        ok.setOnClickListener(this)
        del.setOnClickListener(this)
        addPic.setOnClickListener(this)
        back.setOnClickListener(this)

        scrollBindHelper = ScrollBindHelper(seekBar, scrollView)
        scrollBindHelper.bind(seekBar, scrollView)
        presenter.scrollViewTouch(scrollView)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ok -> {
                if (saveNotesEntity.recyclerPosition == 0) {
                    if(allList.size == 0){
                        saveNotesEntity.recyclerPosition = 1
                    }else{
                        saveNotesEntity.recyclerPosition = allList.get(allList.size -1).recyclerPosition + 1
                    }
                }
                if (isChangePic) {
                    if (addPic.drawable.current.constantState == getDrawable(R.drawable.button_non_image)?.constantState) {
                        if (!saveNotesEntity.picPath.equals("")) {
                            File(saveNotesEntity.picPath).delete()
                        }
                        saveNotesEntity.picPath = ""
                    } else {
                        saveNotesEntity.picPath = presenter.savePic(
                            picUri,
                            path,
                            saveNotesEntity.picPath
                        )
                    }
                }
                saveNotesEntity.title = editTitle.text.toString().replace("\n", slashN)
                                                                 .replace(",",comma)
                saveNotesEntity.content = editContent.text.toString().replace("\n", slashN)
                                                                     .replace(",",comma)
                presenter.insert(saveNotesEntity)
            }
            R.id.del -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("確定要刪除?")
                    .setPositiveButton("確定") { arg0, arg1 -> // TODO Auto-generated method stub
                        presenter.delete(saveNotesEntity)
                    }
                    .setNegativeButton("取消", null)
                    .show()
            }
            R.id.addPic -> {
                val choose = PicChangePopupWindow(this,addPic)
                val view: View = LayoutInflater.from(this@EditFileActivity).inflate(
                    R.layout.popup_window_pic_change,
                    null
                )
                choose.showAtLocation(view, Gravity.CENTER, 0, 0)
                //強制隱藏鍵盤
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            }
            R.id.back -> {
                presenter.back()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode ==  IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            isChangePic = true
            isPicExists = true
            picUri = intent?.data as Uri
            presenter.displayPic(picUri, addPic)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            presenter.back()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
