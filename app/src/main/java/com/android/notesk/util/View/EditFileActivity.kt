package com.android.notesk.util.View

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.android.notesk.R
import com.android.notesk.Model.Model.Companion.IMAGE_REQUEST_CODE
import com.android.notesk.util.Presenter.EditFilePresenter
import com.android.notesk.SQLite.NotesEntity
import kotlinx.android.synthetic.main.activity_edit_file.*
import java.io.File


class EditFileActivity : AppCompatActivity(), View.OnClickListener  {

    var status : Boolean = false
    var allList : ArrayList<NotesEntity> = ArrayList<NotesEntity>()
    lateinit var id : Integer
    lateinit var picUri: Uri
    lateinit var path: String
    lateinit var getNotesEntity : NotesEntity
    var saveNotesEntity = NotesEntity()
    lateinit var presenter : EditFilePresenter

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
            if(!getNotesEntity.picPath.equals("")){
                if(File(getNotesEntity.picPath).exists()){
                    isPicExists = true
                    saveNotesEntity.picPath = getNotesEntity.picPath
                    addPic.setImageBitmap(BitmapFactory.decodeFile(getNotesEntity.picPath))
                }
            }
            editTitle.setText(getNotesEntity.title)
            editContent.setText(getNotesEntity.content)
        }

        ok.setOnClickListener(this)
        del.setOnClickListener(this)
        addPic.setOnClickListener(this)
        back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ok -> {
                if(isChangePic){
                    if(addPic.drawable.current.constantState == getDrawable(R.drawable.none)?.constantState){
                        if(!saveNotesEntity.picPath.equals("")) {
                            File(saveNotesEntity.picPath).delete()
                        }
                        saveNotesEntity.picPath = ""
                    }else{
                        saveNotesEntity.picPath = presenter.savePic(picUri,path,saveNotesEntity.picPath)
                    }
                }
                saveNotesEntity.title = editTitle.text.toString()
                saveNotesEntity.content = editContent.text.toString()
                presenter.insert(status,saveNotesEntity)
            }R.id.del -> {
                presenter.delete(status,saveNotesEntity)
            }R.id.addPic -> {
                val choose = PicChangePopupWindow(this,addPic,isPicExists)
                val view: View =LayoutInflater.from(this@EditFileActivity).inflate(R.layout.popupwindow_pic_change, null)
                choose.showAtLocation(view, Gravity.CENTER, 0, 0)
                //強制隱藏鍵盤
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            }R.id.back -> {
                presenter.back()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode ==  IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            isChangePic = true
            picUri = intent?.data as Uri
            presenter.displayPic(picUri,addPic)
        }
    }
}
