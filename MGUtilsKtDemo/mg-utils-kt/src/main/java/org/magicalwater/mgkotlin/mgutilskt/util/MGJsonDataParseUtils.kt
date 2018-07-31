package org.magicalwater.mgkotlin.mgutilskt.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.reflect.KClass

/**
 * Created by magicalwater on 2018/1/13.
 * 解析所有有關json的東西, 無論是序列化或者反序列化
 */
class MGJsonDataParseUtils {

    companion object {
        //反序列化, 將json變成物件
        fun <T: Any> deserialize(json: String, deserialize: KClass<out T>): T? {

            val objectMapper = jacksonObjectMapper()
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)

            //這邊使用 try catch 以免發生例外
            var ins: Any? = null
            try {
                ins = objectMapper.readValue(json, deserialize.java)
                MGLogUtils.w("解析成功 - ${deserialize.simpleName ?: "名稱null"}")
            } catch (e: Exception) {
                MGLogUtils.w("解析時發生錯誤, 原因: ${e.message}")
                e.printStackTrace()
            }

            return ins as? T
        }

        //序列化, 將物件變成json字串
        fun serialize(data: Any): String {
            val objectMapper = jacksonObjectMapper()
            return objectMapper.writeValueAsString(data) ?: ""
        }
    }
}