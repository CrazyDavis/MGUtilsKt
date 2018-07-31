package org.magicalwater.mgkotlin.mgutilskt.util

import android.content.Context
import android.R.raw
import android.support.annotation.RawRes


/**
 * Created by magicalwater on 2018/2/3.
 * 讀取專案資源下的檔案(例如raw)
 */
class MGResourcesFileUtils {

    companion object {

        //讀取raw下的文字檔案
        fun loadRawText(context: Context, @RawRes rawId: Int): String? {
            return try {
                val res = context.resources
                val ins = res.openRawResource(rawId)
                val b = ByteArray(ins.available())
                ins.read(b)
                String(b)
            } catch (e: Exception) {
                // e.printStackTrace();
                MGLogUtils.w("讀取 Raw 檔案發生錯誤: ${e.message}")
                null
            }

        }
    }
}