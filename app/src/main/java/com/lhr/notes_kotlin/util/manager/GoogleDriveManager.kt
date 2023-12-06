package com.lhr.notes_kotlin.util.manager

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Tasks
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.lhr.notes_kotlin.AppConfig.BACKUP_PATH
import com.lhr.notes_kotlin.AppConfig.ZIP_NAME
import com.lhr.notes_kotlin.R
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class GoogleDriveManager {

    companion object{
        const val Server_Client_Id = "264064949342-jkquvqilfupartsvi15valn8fn390r1n.apps.googleusercontent.com"
//        const val Server_Client_Id = "264064949342-ukijpktnj04cg0do582hcc1vvoc01dqs.apps.googleusercontent.com"
    }

    fun uploadFile(activity: Activity) {
        val credential =
            GoogleSignIn.getLastSignedInAccount(activity)
                ?.let { account ->
                    GoogleAccountCredential.usingOAuth2(
                        activity,
                        setOf(DriveScopes.DRIVE_FILE)
                    )
                        .setBackOff(ExponentialBackOff())
                        .setSelectedAccount(account.account)
                } ?: throw IllegalArgumentException("Google account not found")
        var mDriveService =
            Drive.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory(), credential)
                .setApplicationName(
                    activity.getString(R.string.app_name)
                )
                .build()

        val recordDate = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-")
        val time: String = recordDate.format(Date())
        var fileMetaData = File()
        fileMetaData.name = time + ZIP_NAME

        println("发生了其他异常CC：${fileMetaData.name}")
        val filePath = java.io.File(BACKUP_PATH, ZIP_NAME)
        val mediaContent = FileContent("*/*", filePath)

        try {
            val file = mDriveService.files().create(fileMetaData, mediaContent)
                .setFields("id, parents")
                .execute()
//            DeleteFile(APP_FILES_PATH + java.io.File.separator + BACKUP_NAME)
            if (file != null) {
                val handler = Handler(Looper.getMainLooper())

                handler.post {
                    Log.v("AAAAAAAAAAAAAAAAAA","QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ")
//                    Toast.makeText(activity, "檔案上傳成功", Toast.LENGTH_LONG).show()
                }
            }

        } catch (e: GoogleJsonResponseException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }catch (e: Exception) {
            // 处理其他异常
            println("发生了其他异常：$e")
        }


    }
}