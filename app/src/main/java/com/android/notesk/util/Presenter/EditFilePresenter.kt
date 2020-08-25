package com.android.notesk.util.Presenter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.android.notesk.Model.RandomName
import com.android.notesk.SQLite.NotesDatabase
import com.android.notesk.SQLite.NotesEntity
import com.android.notesk.util.View.ChooseFileActivity
import com.android.notesk.util.View.EditFileActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditFilePresenter(context:Context) {
    var mContext = context
    val database = NotesDatabase(mContext)
    val randomName = RandomName()

    fun insert(status: Boolean,notesEntity: NotesEntity){
        GlobalScope.launch {
            database.getNotesDao().insert(notesEntity)
            back()
        }
    }

    fun delete(status: Boolean,notesEntity: NotesEntity){
        GlobalScope.launch {
            database.getNotesDao().delete(notesEntity)
            File(notesEntity.picPath).delete()
            back()
        }
    }

    fun displayPic(picUri: Uri,imageView: ImageView){
        val parcelFileDescriptor = mContext.contentResolver.openFileDescriptor(picUri, "r")
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        imageView.setImageBitmap(image)
    }

    //儲存圖片
    fun savePic(picUri: Uri,path: String,oldPicName: String): String {
        var picName =""
        Log.v("7777","00" + oldPicName)
        if(oldPicName.equals("")){
            picName = path + "/" + randomName.getRandomName() + ".png"
        }else{
            picName = oldPicName
        }

        try {
            if (picUri != null) {
                var byteread = 0
                val inStream: InputStream = mContext.getContentResolver().openInputStream(picUri) as InputStream//讀取圖片
                val fs = FileOutputStream(picName) //輸出圖片
                val buffer = ByteArray(1444)
                while (inStream.read(buffer).also { byteread = it } != -1) {
                    fs.write(buffer, 0, byteread)
                }
                inStream.close()
            }
        } catch (e: Exception) {
            Log.v("ERROR", "" + e)
        }
        return picName
    }

    fun back(){
        val intent = Intent(mContext, ChooseFileActivity::class.java)
        mContext.startActivity(intent)
        (mContext as EditFileActivity).finish()
    }
}
