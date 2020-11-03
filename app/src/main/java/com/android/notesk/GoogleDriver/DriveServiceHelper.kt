package com.android.notesk.GoogleDriver

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.android.notesk.Model.Model.Companion.PACKAGE_FILES_PATH
import com.android.notesk.Model.Model.Companion.key
import com.android.notesk.util.ChooseFile.ChooseFileActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.drive.CreateFileActivityOptions
import com.google.android.gms.drive.Drive.getDriveClient
import com.google.android.gms.drive.Drive.getDriveResourceClient
import com.google.android.gms.drive.DriveContents
import com.google.android.gms.drive.MetadataChangeSet
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import java.io.OutputStreamWriter
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class DriveServiceHelper(driveService: Drive) {

    private val mExecutor: Executor = Executors.newSingleThreadExecutor()
    private var mDriveService = driveService

    fun createFolder(){
        Tasks.call(mExecutor, Callable {
            var fileMetaData = File()
            fileMetaData.setName("Notes");
            fileMetaData.setMimeType("application/vnd.google-apps.folder");
            var googleFolder: File? = null
            try {
                googleFolder= mDriveService.files().create(fileMetaData)
                        .setFields("id")
                        .execute()
            } catch (e: Exception) {
                Log.v("ERROR：","" + e)
            }
//            if (googleFolder == null) {
//                throw IOException("Null result when request file creation")
//            }

            fileMetaData = File()
            fileMetaData.name = key
            fileMetaData.parents = Collections.singletonList((googleFolder as File).id)
            val filePath = java.io.File(PACKAGE_FILES_PATH + "/" + key)
            val mediaContent = FileContent("", filePath)
            val keyFile : File = mDriveService.files().create(fileMetaData, mediaContent)
                    .setFields("id, parents")
                    .execute()
        })
    }

    fun searchFile(){
        Tasks.call(mExecutor, Callable {
            var pageToken: String? = null
            var result : FileList?
            do {
                result = mDriveService.files().list()
                        .setQ("trashed = false and name = '" + key + "'" )
                        .setSpaces("drive")
                        .setFields("nextPageToken, files(id,parents)")
                        .setPageToken(pageToken)
                        .execute()
                pageToken = result.nextPageToken
            } while (pageToken != null)


            if((result as FileList).files.size == 0){
                Log.v("7778","77778")
            }else{

            }
            Log.v("777","" + result.files)
        })
    }

    fun test(mContext: ChooseFileActivity, googleAccount:GoogleSignInAccount){
            val createContentsTask: Task<DriveContents> = getDriveResourceClient(mContext,googleAccount).createContents()
            createContentsTask.continueWithTask<IntentSender> { task ->
                    val contents = task.result
                    val outputStream = contents!!.outputStream
                    Log.v("777","222")
                    OutputStreamWriter(outputStream)
                        .use { writer -> writer.write("Hello World!") }
                    val changeSet = MetadataChangeSet.Builder()
                        .setTitle("New file")
                        .setMimeType("text/plain")
                        .setStarred(true)
                        .build()
                    val createOptions = CreateFileActivityOptions.Builder()
                            .setInitialDriveContents(contents)
                            .setInitialMetadata(changeSet)
                            .build()
                    getDriveClient(mContext,googleAccount).newCreateFileActivityIntentSender(createOptions)
                }
                .addOnSuccessListener(mContext,
                    object : OnSuccessListener<IntentSender?> {
                        override fun onSuccess(intentSender: IntentSender?) {
                            try {
                                startIntentSenderForResult(mContext,intentSender as IntentSender, 4, null, 0, 0, 0
                                ,null)
                            } catch (e: SendIntentException) {
                                Log.e(TAG, "Unable to create file", e)
                            }
                        }
                    })
                .addOnFailureListener(mContext, object : OnFailureListener {
                    override fun onFailure(e: java.lang.Exception) {
                        Log.e(TAG, "Unable to create file", e)
                    }
                })
    }

    fun tt(mContext: ChooseFileActivity, googleAccount:GoogleSignInAccount){
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
////        intent.addCategory(Intent.CATEGORY_OPENABLE)
//
////        var a = "content://com.google.android.apps.docs.storage/"
////        var b = Uri.parse("content://com.google.android.apps.docs.storage/document/")
//        var b = Uri.parse("content://com.android.providers.downloads.documents/tree/downloads")
////        intent.setType("*/*")
//        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, b)
////        intent.setDataAndType(b, "*/*");
//        startActivityForResult(mContext, intent,0,null)
    }

}