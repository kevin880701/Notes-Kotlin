package com.android.notesk.GoogleDriver

import android.util.Log
import com.android.notesk.Model.Model.Companion.key
import com.android.notesk.Model.Model.Companion.PACKAGE_FILES_PATH
import com.google.android.gms.tasks.Tasks
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
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
}