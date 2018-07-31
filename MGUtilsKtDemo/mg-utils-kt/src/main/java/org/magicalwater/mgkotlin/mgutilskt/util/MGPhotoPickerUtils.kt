package org.magicalwater.mgkotlin.mgutilskt.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import java.io.File

/**
 * Created by magicalwater on 2018/1/28.
 *
 */
class MGPhotoPickerUtils {


    companion object {

        /**
         * 選擇圖片, 從相機或圖庫
         * 這裡有個坑, 當有設置 EXTRA_OUTPUT 的 uri 時
         *  - 選擇圖片庫返回圖片uri: 在 onActivityResult 的 data 有值
         *  - 選擇相機拍照: 在 onActivityResult 的 data 無值
         * 因此此處建議別帶入 uri
         * @param uri - 相片儲存uri, 可選
         * @return
         */
        fun getPhotoSelectIntent(uri: Uri?): Intent {
            val take = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            take.addCategory(Intent.CATEGORY_DEFAULT)
            take.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            val pics = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val chose = Intent.createChooser(pics, "選擇圖片")
            chose.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(take))
            return chose
        }

        /**
         * 圖片裁減, 由於使用第三方app裁減了之後, 為權限問題無法儲存在此app目錄下
         * 因此判斷outputUri是否為null, 當是的時候返回Bitmap
         * @param inputUri - 需要裁減的圖片
         * @param outputUri - 裁剪後儲存位置
         * @param width 裁減寬度
         * @param height 裁減高度
         * @return
         */
        fun getImageCropIntent(inputUri: Uri, outputUri: Uri?, width: Int, height: Int): Intent {
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(inputUri, "image/*")
            // 設置在開啟的Intent中設置顯示的view可裁剪
            intent.putExtra("crop", "true")
            intent.putExtra("scale", true) // 去黑邊
            intent.putExtra("scaleUpIfNeeded", true) // 去黑邊
            // aspectX aspectY 裁減寬高比例
            intent.putExtra("aspectX", width) // 輸出是x方向的比例
            intent.putExtra("aspectY", height)
            // outputX outputY 輸出圖片寬高, 以下數字別再更動, 否則會造成卡死
            intent.putExtra("outputX", width) // 输出X方向的像素
            intent.putExtra("outputY", height)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            intent.putExtra("noFaceDetection", true)

            //當 outputUri null, 表示不保存圖片, 那麼便將 bitmap 返回
            if (outputUri == null) {
                intent.putExtra("return-data", true) // 返回圖片數據
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
                intent.putExtra("return-data", false) // 不返回圖片數據
            }

            return intent
        }



        //图片裁剪所需的Uri类似：content:// 的形式
        fun getContentUri(context: Context, file: File): Uri? {
            val filePath = file.absolutePath
            val cursor = context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf( MediaStore.Images.Media._ID ),
                    MediaStore.Images.Media.DATA + "=? ",
                    arrayOf( filePath ), null)

            if (cursor != null && cursor.moveToFirst()) {
                val id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.MediaColumns._ID))
                val baseUri = Uri.parse("content://media/external/images/media")
                return Uri.withAppendedPath(baseUri, "" + id)
            } else {
                if (file.exists()) {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.DATA, filePath)
                    return context.contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                } else {
                    return null
                }
            }
        }

    }


}