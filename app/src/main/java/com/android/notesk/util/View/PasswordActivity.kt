package com.android.notesk.util.View

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.notesk.R
import com.android.notesk.Model.Model.Companion.key
import com.android.notesk.Model.Model.Companion.PACKAGE_FILES_PATH
import kotlinx.android.synthetic.main.activity_password.*
import java.io.File


class PasswordActivity : AppCompatActivity() {

    var vibrator: Vibrator? = null
    var count: Int = 0
    var realCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_password)

        var permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        while (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
                break
            } else {
                break
            }
        }
        vibrator = application.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

        PACKAGE_FILES_PATH = getExternalFilesDir(null)!!.absolutePath
        val file = File(PACKAGE_FILES_PATH + "/" + key)
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    fun count(v : View) {
        count++
        when (count) {
            1 -> {vibrator()
                cir1.setBackgroundResource(R.drawable.password_cir2)}
            2 -> {vibrator()
                cir2.setBackgroundResource(R.drawable.password_cir2)}
            3 -> {vibrator()
                cir3.setBackgroundResource(R.drawable.password_cir2)}
            4 -> {
                cir4.setBackgroundResource(R.drawable.password_cir2)
                Handler().postDelayed({
                    textView1.text = "密碼錯誤"
                    cir1.setBackgroundResource(R.drawable.password_cir)
                    cir2.setBackgroundResource(R.drawable.password_cir)
                    cir3.setBackgroundResource(R.drawable.password_cir)
                    cir4.setBackgroundResource(R.drawable.password_cir)
                    vibrator()
                }, 100)
                count = 0
            }
        }
    }

    fun real(v : View){
        realCount++
        if (realCount == 2) {
            val it = Intent(this, ChooseFileActivity::class.java)
            startActivity(it)
            finish()
        }
    }

    //震動
    fun vibrator(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(count == 4){
                vibrator!!.vibrate(VibrationEffect.createOneShot(90, VibrationEffect.DEFAULT_AMPLITUDE));
            }else{
                vibrator!!.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        } else {
            //deprecated in API 26
            if(count == 4){
                vibrator!!.vibrate(90);
            }else{
                vibrator!!.vibrate(20);
            }
        }
    }
}
