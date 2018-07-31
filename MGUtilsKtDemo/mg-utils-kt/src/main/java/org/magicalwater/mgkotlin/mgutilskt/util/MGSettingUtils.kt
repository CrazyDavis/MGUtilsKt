package org.magicalwater.mgkotlin.mgutilskt.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by magicalwater on 2018/1/23.
 * 存放本地資料 Preference 的地方
 */
class MGSettingUtils {


    companion object {

        private val SETTING_NAME: String = "SETTING_NAME"

        var prefs: SharedPreferences? = null

        //初始化 SharePreferences
        fun initPrefs(context: Context) {
            prefs = context.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE)
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun <T> put(name: String, value: T) {
        val prefs = prefs
        if (prefs == null) {
            MGLogUtils.d("尚未初始化 SharedPreferences, 無法存入 $name -> $value")
            return
        }
        with(prefs.edit()) {
            when (value) {
                is Long -> putLong(name, value)
                is String -> putString(name, value)
                is Int -> putInt(name, value)
                is Boolean -> putBoolean(name, value)
                is Float -> putFloat(name, value)
                else -> throw IllegalArgumentException("SharedPreferences can't be save this type")
            }.apply()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(name: String, default: T): T = with(prefs) {


        val prefs = prefs
        if (prefs == null) {
            MGLogUtils.d("尚未初始化 SharedPreferences, 無法讀取 $name , 因此返回預設值")
            return default
        }

        with(prefs) {
            val res: Any = when (default) {
                is Long -> getLong(name, default)
                is String -> getString(name, default)
                is Int -> getInt(name, default)
                is Boolean -> getBoolean(name, default)
                is Float -> getFloat(name, default)
                else -> throw IllegalArgumentException("SharedPreferences can't be get this type")
            }
            return res as T
        }

    }

}