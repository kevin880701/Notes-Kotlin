package com.android.notesk.util.ChooseFile.BackupChoose

import android.content.Context
import androidx.core.app.ActivityCompat.startActivityForResult
import com.android.notesk.Model.Model
import com.android.notesk.util.ChooseFile.ChooseFileActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

class BackupChoosePresenter(mContext: Context) {
    var mContext = mContext

    fun uploadClick(){
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope(DriveScopes.DRIVE_FILE),
                Scope(DriveScopes.DRIVE_APPDATA)
            )
            .requestIdToken("1072640924634-58t06k8q3g9umanh3fajvf5s3drim4q5.apps.googleusercontent.com")
            .build()
        val client = GoogleSignIn.getClient(mContext, signInOptions)

        // The result of the sign-in Intent is handled in onActivityResult.
        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(mContext as ChooseFileActivity, client.signInIntent, Model.ACCOUNT_CHOOSE, null)
    }


}