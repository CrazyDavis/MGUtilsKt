package org.magicalwater.mgkotlin.mgutilskt.util

import android.util.Log
import org.magicalwater.mgkotlin.mgutilskt.util.MGThreadUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.ArrayList
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class MGZipUtils {

    companion object {
        val TAG = "ZIP"

        /**
         * 解壓zip到指定的路徑
         * @param zipFileString zip的名稱
         * @param outPathString 要解壓路徑
         * @throws Exception
         * */
        @Throws(Exception::class)
        fun unZipFolder(zipFileString: String, outPathString: String, endHandler: () -> Unit) {
            unZipFolder(FileInputStream(zipFileString), outPathString, endHandler)
        }

        @Throws(Exception::class)
        fun unZipFolder(inputStream: InputStream, outPathString: String, endHandler: () -> Unit) {
            MGThreadUtils.inSub {
                val inZip = ZipInputStream(inputStream)
                var zipEntry: ZipEntry? = inZip.nextEntry
                var szName: String
                while (zipEntry != null) {
                    szName = zipEntry.name
                    if (zipEntry.isDirectory) {
                        val folder = File(outPathString + File.separator + szName)
                        folder.mkdirs()
                    } else {
                        Log.e(TAG, outPathString + File.separator + szName)
                        val file = File(outPathString + File.separator + szName)
                        if (!file.exists()) {
                            Log.e(TAG, "Create the file:" + outPathString + File.separator + szName)
                            file.parentFile.mkdirs()
                            file.createNewFile()
                        }
                        // 獲取文件的輸出流
                        val out = FileOutputStream(file)
                        val buffer = ByteArray(1024)
                        var len: Int = inZip.read(buffer)
                        while (len != -1) {
                            out.write(buffer, 0, len)
                            out.flush()
                            len = inZip.read(buffer)
                        }
                        out.close()
                    }
                    zipEntry = inZip.nextEntry
                }
                inZip.close()

                endHandler()
            }
        }

        /**
         * 壓縮文件和文件夾
         * @param srcFileString - 要壓縮的文件或文件夾
         * @param zipFileString 解壓完成的zip路徑
         * @throws Exception
         */
        @Throws(Exception::class)
        fun zipFolder(srcFileString: String, zipFileString: String) {
            //創建zip
            val outZip = ZipOutputStream(FileOutputStream(zipFileString))
            //創建文件
            val file = File(srcFileString)
            //壓縮
            zipFiles(file.parent + File.separator, file.name, outZip)
            outZip.finish()
            outZip.close()
        }

        /**
         * 壓縮文件
         * @param folderString
         * @param fileString
         * @param zipOutputSteam
         * @throws Exception
         */
        @Throws(Exception::class)
        private fun zipFiles(folderString: String, fileString: String, zipOutputSteam: ZipOutputStream?) {
            if (zipOutputSteam == null) return
            val file = File(folderString + fileString)
            if (file.isFile) {
                val zipEntry = ZipEntry(fileString)
                val inputStream = FileInputStream(file)
                zipOutputSteam.putNextEntry(zipEntry)
                val buffer = ByteArray(4096)
                var len: Int = inputStream.read(buffer)
                while (len != -1) {
                    zipOutputSteam.write(buffer, 0, len)
                    len = inputStream.read(buffer)
                }
                zipOutputSteam.closeEntry()
            } else {
                //文件夾
                val fileList = file.list()
                //沒有仔文件和壓縮
                if (fileList.isEmpty()) {
                    val zipEntry = ZipEntry(fileString + File.separator)
                    zipOutputSteam.putNextEntry(zipEntry)
                    zipOutputSteam.closeEntry()
                }
                //子文件
                for (i in fileList.indices) {
                    zipFiles(folderString, fileString + File.separator + fileList[i], zipOutputSteam)
                }
            }
        }

        /**
         * 返回zip的文件输入流
         * @param zipFileString zip的名稱
         * @param fileString zip的文件名
         * @return InputStream
         * @throws Exception
         */
        @Throws(Exception::class)
        fun upZip(zipFileString: String, fileString: String): InputStream {
            val zipFile = ZipFile(zipFileString)
            val zipEntry = zipFile.getEntry(fileString)
            return zipFile.getInputStream(zipEntry)
        }

        /**
         * 返回ZIP中的文件列表（文件和文件夹）
         * @param zipFileString zip的名稱
         * @param bContainFolder 是否包含文件夾
         * @param bContainFile 是否包含文件夾
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun getFileList(zipFileString: String, bContainFolder: Boolean, bContainFile: Boolean): List<File> {
            val fileList = ArrayList<File>()
            val inZip = ZipInputStream(FileInputStream(zipFileString))
            var zipEntry: ZipEntry = inZip.nextEntry
            var szName: String
            while (zipEntry != null) {
                szName = zipEntry.name
                if (zipEntry.isDirectory) {
                    // 獲取文件名
                    szName = szName.substring(0, szName.length - 1)
                    val folder = File(szName)
                    if (bContainFolder) {
                        fileList.add(folder)
                    }
                } else {
                    val file = File(szName)
                    if (bContainFile) {
                        fileList.add(file)
                    }
                }
                zipEntry = inZip.nextEntry
            }
            inZip.close()
            return fileList
        }

    }
}
        