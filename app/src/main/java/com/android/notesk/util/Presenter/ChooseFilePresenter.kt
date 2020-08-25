package com.android.notesk.util.Presenter

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.android.notesk.GoogleDriver.DriveServiceHelper
import com.android.notesk.Model.Model.Companion.ACCOUNT_CHOOSE
import com.android.notesk.Model.MyAdapter
import com.android.notesk.SQLite.NotesDatabase
import com.android.notesk.SQLite.NotesEntity
import com.android.notesk.Zip.ZipUtils
import com.android.notesk.util.View.ChooseFileActivity
import com.android.notesk.util.View.EditFileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.Drive.getDriveResourceClient
import com.google.android.gms.drive.OpenFileActivityOptions
import com.google.android.gms.drive.query.Filters
import com.google.android.gms.drive.query.SearchableField
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class ChooseFilePresenter(context : ChooseFileActivity) {
    val mContext = context
    var allList : ArrayList<NotesEntity> = ArrayList<NotesEntity>()
    val dataBase = NotesDatabase(context)

    fun selectSql(adapter : MyAdapter){
        GlobalScope.launch(Dispatchers.IO) {
            allList = dataBase.getNotesDao().getAll() as ArrayList<NotesEntity>
            withContext(Dispatchers.Main)
            {
                adapter.update(allList)
            }
        }
    }

    fun picGone(notesEntity: NotesEntity){
        GlobalScope.launch {
            dataBase.getNotesDao().insert(notesEntity)
        }
    }

    fun zipFile(){
        val appZip = ZipUtils()
    }

    fun syncClick(){
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(
                    Scope(DriveScopes.DRIVE_FILE),
                    Scope(DriveScopes.DRIVE_APPDATA)
                )
                .requestIdToken("1072640924634-58t06k8q3g9umanh3fajvf5s3drim4q5.apps.googleusercontent.com")
                .build()
        val client = GoogleSignIn.getClient(mContext, signInOptions)

        startActivityForResult(mContext,client.signInIntent, ACCOUNT_CHOOSE,null)
    }

    fun handleSignInResult(result: Intent) {
        Log.v("777","1111")
        GoogleSignIn.getSignedInAccountFromIntent(result)
            .addOnSuccessListener { googleAccount: GoogleSignInAccount ->
                Log.d(TAG, "Signed in as " + googleAccount.email)
                val credential = GoogleAccountCredential.usingOAuth2(mContext, Collections.singleton(DriveScopes.DRIVE_FILE))
                credential.selectedAccount = googleAccount.account
                val googleDriveService = Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    GsonFactory(),
                    credential
                ).setApplicationName("Notes")
                    .build()
                // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                // Its instantiation is required before handling any onClick actions.

                var mDriveServiceHelper = DriveServiceHelper(googleDriveService)
//                mDriveServiceHelper.createFolder()
//                mDriveServiceHelper.searchFile()
            }
            .addOnFailureListener { exception: Exception? ->
                Log.e(TAG,"Unable to sign in.",exception)
            }
    }

    fun editFilePage(notesEntity: NotesEntity?){
        val intent = Intent(mContext, EditFileActivity::class.java)
        notesEntity?.let {
            val bundle = Bundle()
            bundle.putSerializable("notesEntity", notesEntity)
            intent.putExtras(bundle)
        }
        mContext.startActivity(intent)
        mContext.finish()
    }
}
