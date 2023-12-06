package com.lhr.notes_kotlin.util.manager

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


object ZipManager {

    /**
     * 將檔案所有紀錄的檔案壓縮至備份資料夾
     * @param sourceFolderPath 儲存紀錄的資料夾路徑
     * @param zipFilePath 壓縮路徑
     */
    fun zipFolder(sourceFolderPath: String, zipFilePath: String) {
        val sourceFolder = File(sourceFolderPath)
        val zipFile = File(zipFilePath)

        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { zipOutputStream ->
            zip(sourceFolder, sourceFolder, zipOutputStream)
        }
    }

    /**
     * 儲存壓縮好的檔案
     * @param folderToZip 已被壓縮的檔案
     * @param baseFolder 壓縮路徑
     * @param zipOutputStream
     */
    private fun zip(folderToZip: File, baseFolder: File, zipOutputStream: ZipOutputStream) {
        val fileList = folderToZip.listFiles()

        for (file in fileList) {
            if (file.isDirectory) {
                zip(file, baseFolder, zipOutputStream)
            } else {
                val relativePath = file.path.substring(baseFolder.path.length + 1)
                val entry = ZipEntry(relativePath)
                zipOutputStream.putNextEntry(entry)

                FileInputStream(file).use { inputStream ->
                    BufferedInputStream(inputStream).use { bufferedInputStream ->
                        val data = ByteArray(1024)
                        var count: Int
                        while (bufferedInputStream.read(data, 0, 1024).also { count = it } != -1) {
                            zipOutputStream.write(data, 0, count)
                        }
                    }
                }
                zipOutputStream.closeEntry()
            }
        }
    }


    /**
     * 解壓縮到指定目錄
     * @param zipFilePath 被解壓縮的檔案
     * @param destinationPath 指定的目錄
     */
    fun unzipFile(zipFilePath: String, destinationPath: String) {
        val buffer = ByteArray(1024)

        try {
            // 創建目標目錄
            val destDir = File(destinationPath)
            if (!destDir.exists()) {
                destDir.mkdirs()
            }
            // 打開壓縮文件
            val zipInputStream = ZipInputStream(FileInputStream(zipFilePath))
            // 逐個讀取壓縮項，並解壓到目標目錄
            var zipEntry: ZipEntry? = zipInputStream.nextEntry
            while (zipEntry != null) {
                val newFile = File(destDir, zipEntry.name)
                if (zipEntry.isDirectory) {
                    // 如果是目錄，創建目錄
                    newFile.mkdirs()
                } else {
                    // 如果是文件，創建父目錄並寫入文件內容
                    File(newFile.parent).mkdirs()

                    val fileOutputStream = newFile.outputStream()
                    var len: Int
                    while (zipInputStream.read(buffer).also { len = it } > 0) {
                        fileOutputStream.write(buffer, 0, len)
                    }
                    fileOutputStream.close()
                }
                zipEntry = zipInputStream.nextEntry
            }
            zipInputStream.closeEntry()
            zipInputStream.close()
            println("解壓縮完成")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}