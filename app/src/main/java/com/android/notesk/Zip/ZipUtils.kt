package com.android.notesk.Zip

import com.android.notesk.Model.Model.Companion.BACKUP_NAME
import com.android.notesk.Model.Model.Companion.DATABASES_PATH
import com.android.notesk.Model.Model.Companion.PACKAGE_FILES_PATH
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry;

class ZipUtils() {
    var filesList: ArrayList<String>  = ArrayList()
    var databasesList: ArrayList<String>  = ArrayList()
    private val OUTPUT_ZIP_FILE = BACKUP_NAME
    private val SOURCE_FOLDER = PACKAGE_FILES_PATH // SourceFolder path
    val source = File(SOURCE_FOLDER).name
    val databasesSource = File(DATABASES_PATH).name
    val buffer = ByteArray(1024)
    var fos = FileOutputStream(File(SOURCE_FOLDER).parent + File.separator + OUTPUT_ZIP_FILE)
    var zos = ZipOutputStream(fos)

    init {
        zipIt(File(SOURCE_FOLDER).parent + File.separator + OUTPUT_ZIP_FILE)
    }

    fun zipIt(zipFile: String) {
        try {
            generateFilesList(File(SOURCE_FOLDER + File.separator))
            generateDatabaseList(File(DATABASES_PATH + File.separator))
            println("Output to Zip : $zipFile")
            var `in`: FileInputStream? = null
            for (file in filesList!!) {
                println("File Added : $file")
                val ze = ZipEntry(source + File.separator + file)
                zos.putNextEntry(ze)
                try {
                    `in` = FileInputStream(SOURCE_FOLDER + File.separator + file)
                    var len = 0
                    while (`in`.read(buffer).also({ len = it }) > 0) {
                        zos.write(buffer, 0, len)
                    }
                } finally {
//                    (`in` as FileInputStream).close()
                }
            }
            for (file in databasesList!!) {
                println("File Added : $file")
                val ze = ZipEntry(databasesSource + File.separator + file)
                zos.putNextEntry(ze)
                try {
                    `in` = FileInputStream(DATABASES_PATH + File.separator + file)
                    var len = 0
                    while (`in`.read(buffer).also({ len = it }) > 0) {
                        zos.write(buffer, 0, len)
                    }
                } finally {
                    (`in` as FileInputStream).close()
                }
            }
            zos.closeEntry()
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            try {
                zos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun generateFilesList(node: File) { // add file only
        if (node.isFile) {
            filesList.add(generateZipEntry(node.toString()) as String)
        }
        if (node.isDirectory) {
            val subNote = node.list()
            for (filename in subNote) {
                generateFilesList(File(node, filename))
            }
        }
    }

    fun generateDatabaseList(node: File) { // add file only
        if (node.isFile) {
            databasesList.add(databasesGenerateZipEntry(node.toString()) as String)
        }
        if (node.isDirectory) {
            val subNote = node.list()
            for (filename in subNote) {
                generateDatabaseList(File(node, filename))
            }
        }
    }

    private fun generateZipEntry(file: String): String? {
        return file.substring(SOURCE_FOLDER.length + 1, file.length)
    }

    private fun databasesGenerateZipEntry(file: String): String? {
        return file.substring(DATABASES_PATH.length + 1, file.length)
    }
}