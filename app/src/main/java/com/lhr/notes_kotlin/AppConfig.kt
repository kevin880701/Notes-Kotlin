package com.lhr.notes_kotlin

object AppConfig {
    const val KEY = ".0BwwA4oUTnoTesGRPeTVjaWRDY1E"
    lateinit var DATABASES_PATH: String  // /data/user/0/com.lhr.notes_kotlin/databases
    lateinit var FILES_PATH: String  // /storage/emulated/0/Android/data/com.lhr.notes_kotlin/files/Notes
    lateinit var BACKUP_PATH: String  // /storage/emulated/0/Android/data/com.lhr.notes_kotlin/files/Backup
    const val CSV_NAME = "Notes.csv"
    const val ZIP_NAME = "Notes.rar"
}