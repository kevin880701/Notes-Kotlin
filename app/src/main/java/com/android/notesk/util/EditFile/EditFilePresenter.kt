package com.android.notesk.util.EditFile

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import com.android.notesk.Model.RandomName
import com.android.notesk.SQLite.NotesDatabase
import com.android.notesk.SQLite.NotesEntity
import com.android.notesk.ScrollView.ObservableScrollView
import com.android.notesk.util.ChooseFile.ChooseFileActivity
import com.android.notesk.util.ChooseFile.ChooseFileActivity.Companion.allList
import com.android.notesk.util.EditFile.EditFileActivity.Companion.isChangePic
import com.android.notesk.util.EditFile.EditFileActivity.Companion.isPicExists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class EditFilePresenter(context: Context) {
    var mContext = context
    val database = NotesDatabase(mContext)
    val randomName = RandomName()

    fun insert(notesEntity: NotesEntity){
        runBlocking {     // 阻塞主執行緒
            launch(Dispatchers.IO) {
                Log.v("PPP","" + notesEntity.recyclerPosition)
                database.getNotesDao().insert(notesEntity)
            }
        }
        back()
    }

    fun delete(notesEntity: NotesEntity){
        runBlocking {     // 阻塞主執行緒
            launch(Dispatchers.IO) {
                database.getNotesDao().delete(notesEntity)
                if(notesEntity.recyclerPosition != 0){
                    for (i in 0..allList.size - notesEntity.recyclerPosition) {
                        database.getNotesDao().delUpdateId(notesEntity.recyclerPosition+1+i,notesEntity.recyclerPosition+i)
                    }
                }
                File(notesEntity.picPath).delete()
            }
        }
        back()
//        GlobalScope.launch {
//        }
    }

    fun displayPic(picUri: Uri, imageView: ImageView){
        val parcelFileDescriptor = mContext.contentResolver.openFileDescriptor(picUri, "r")
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        imageView.setImageBitmap(image)
    }

    //儲存圖片
    fun savePic(picUri: Uri, path: String, oldPicName: String): String {
        var picName = ""
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

    fun scrollViewTouch(scrollView: ObservableScrollView){
        scrollView.setOnTouchListener(object : View.OnTouchListener {
            private var lastY = 0
            private val touchEventId = -9983761
            var handler: Handler = object : Handler() {
                override
                fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    val scroller = msg.obj as ScrollView
                    if (msg.what === touchEventId) {
                        if (lastY == scroller.scrollY) {
                            handleStop(scroller)
                        } else {
                            this.sendMessageDelayed(this.obtainMessage(touchEventId, scroller), 5)
                            lastY = scroller.scrollY
                        }
                    }
                }
            }

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_UP) {
                    handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5)
                }
                return false
            }

            private fun handleStop(view: Any) {
                val scroller = view as ScrollView
                val scrollY = scroller.scrollY
                println("scrollY$scrollY")
            }
        })
    }

    fun back(){
        isChangePic = false
        isPicExists = false
        val intent = Intent(mContext, ChooseFileActivity::class.java)
        mContext.startActivity(intent)
        (mContext as EditFileActivity).finish()
    }
}
