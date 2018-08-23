package org.magicalwater.mgkotlin.mgutilskt.util

import kotlin.math.max
import kotlin.math.min

class MGVersionUtils {
    companion object {
        fun compareIsNew(ori: String, new: String): Boolean {
            val oriArray = ori.split(".").toMutableList()
            val newArray = new.split(".").toMutableList()
//            val minLength = min(oriArray.size, newArray.size)
            val maxLength = max(oriArray.size, newArray.size)

            //比較短的那邊補上缺少的長度, 內容為0
            if (maxLength > oriArray.size) {
                oriArray.addAll(
                        List(maxLength - oriArray.size) { "0" }
                )
            } else if (maxLength > newArray.size) {
                newArray.addAll(
                        List(maxLength - newArray.size) { "0" }
                )
            }

            (0 until maxLength).forEach {
                val oriInt = oriArray[it].toInt()
                val newInt = newArray[it].toInt()
                when {
                    oriInt > newInt -> return false
                    newInt > oriInt -> return true
                    else -> {}
                }
//                println("跑迴圈 $it")
            }
            return false
        }
    }
}