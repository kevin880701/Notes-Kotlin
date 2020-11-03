package com.android.notesk.Model

class Model {
    companion object {
        const val IMAGE_REQUEST_CODE : Int = 100
        const val ACCOUNT_CHOOSE : Int = 200
        const val GOOGLE_SIGN_IN : Int = 201
        const val SAVE_LOCAL : Int = 300
        const val SAVE_DRIVE : Int = 301
        const val OVER_FILE : Int = 400
        const val KEEP_FILE : Int = 401
        const val key : String = ".0BwwA4oUTnoTesGRPeTVjaWRDY1E"
        var PACKAGE_FILES_PATH : String = "" // /storage/emulated/0/Android/data/com.android.notesk/files
        var DATABASES_PATH : String = "" // /data/user/0/com.android.notesk/databases
        val BACKUP_NAME : String = "NotesBackup.rar"
        val driveFolderName = "Notes"
        val csvBackUp = "NotesBackUp.csv"
        val slashN = "#n#"
        val comma = "#4a73h#"
    }
}