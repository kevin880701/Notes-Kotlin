package com.android.notesk.util.ChooseFile.BackupChoose

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.android.notesk.Model.Model
import com.android.notesk.util.ChooseFile.ChooseFileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import java.io.File

class MakeBackUpPresenter(mContext: Context) {
    var mContext = mContext

    fun saveLocal(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*")
        startActivityForResult(mContext as Activity,intent, Model.SAVE_LOCAL,null)
    }

    fun saveDrive(){
        var shareIntent = Intent(Intent.ACTION_SEND)
        val backupUri = FileProvider.getUriForFile(
            mContext,
            mContext.packageName + ".provider",
            File(File(Model.PACKAGE_FILES_PATH).parent + File.separator + Model.BACKUP_NAME)
        )
        shareIntent.putExtra(Intent.EXTRA_STREAM,backupUri)
        shareIntent.setType("*/*")
        mContext.startActivity(shareIntent)
    }
}