package com.android.notesk.Zip

import android.net.Uri
import android.util.Log
import com.android.notesk.util.ChooseFile.ChooseFileActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream


class UnZip() {

    init {
    }

    fun unzip(zipFile: Uri, location: String, chooseFileActivity: ChooseFileActivity) {
        try {
            val f = File(location)
            if (!f.isDirectory) {
                f.mkdirs()
            }

            val inStream: InputStream = chooseFileActivity.getContentResolver().openInputStream(zipFile) as InputStream//讀取

            val zin = ZipInputStream(inStream)
            try {
                var ze: ZipEntry?
                while (zin.nextEntry.also { ze = it } != null) {
                    val path = location + ze!!.name
//                    if(!File(File(path).parent).exists()){
//                        val unzipFile = File(File(path).parent)
//                        unzipFile.mkdirs()
//                    }

                    if (ze!!.isDirectory) {
                        val unzipFile = File(path)
                        if (!unzipFile.isDirectory) {
                            unzipFile.mkdirs()
                        }
                    } else {
                        val fout = FileOutputStream(path, false)
                        try {
                            var c = zin.read()
                            while (c != -1) {
                                fout.write(c)
                                c = zin.read()
                            }
                            zin.closeEntry()
                        } finally {
                            fout.close()
                        }
                    }
                }
            } finally {
                zin.close()
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Unzip exception", e)
        }
    }
}