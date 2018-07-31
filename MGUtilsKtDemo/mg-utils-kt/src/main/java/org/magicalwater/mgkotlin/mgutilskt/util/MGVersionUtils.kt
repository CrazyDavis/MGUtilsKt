package org.magicalwater.mgkotlin.mgutilskt.util

import kotlin.math.min

class MGVersionUtils {
    companion object {
        fun compareIsNew(ori: String, new: String): Boolean {
            val oriArray = ori.split(".")
            val newArray = new.split(".")
            val minLength = min(oriArray.count(), newArray.count())
            var isNew = false
            (0 until minLength).forEach {
                if (newArray[it].toInt() > oriArray[it].toInt()) {
                    isNew = true
                }

                if (isNew) return@forEach
            }
            //假如版本號碼完全一樣, 檢查 new的長度是否比ori長, 若是比較長也是有新版本
            if (!isNew) {
                isNew = newArray.count() > oriArray.count()
            }
            return isNew
        }
    }
}