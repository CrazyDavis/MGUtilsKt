package org.magicalwater.mgkotlin.mgutilskt.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import org.magicalwater.mgkotlin.mgutilskt.security.MGSecuritySharedPreference

/**
 * Created by magicalwater on 2018/1/23.
 * 存放本地資料 Preference 的地方
 */
class MGSettingUtils private constructor() {

    val SETTING_NAME: String = "SETTING_NAME"

    companion object {
        val shared: MGSettingUtils = MGSettingUtils()
        private var prefs: MGSecuritySharedPreference? = null
    }

    //初始化 SharePreferences, 第二個參數微當 prefs 已經存在時是否重新實例化
    fun init(context: Context, resetIfExist: Boolean = false): MGSettingUtils {
        if (prefs == null || resetIfExist) {
            prefs = MGSecuritySharedPreference(context, SETTING_NAME, Context.MODE_PRIVATE)
        }
        return this
    }

    /**
     * @param isAsync - 是否異步存入
     * */
    @SuppressLint("CommitPrefEdits")
    fun put(name: String, value: Any?, isAsync: Boolean = true) {
        val prefs = prefs
        if (prefs == null) {
            MGLogUtils.d("尚未初始化 SharedPreferences, 無法存入 $name -> $value")
            return
        }
        if (value == null) {
            remove(name, isAsync)
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
            }
            if (isAsync) apply()
            else commit()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(name: String, default: T): T {
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

    /**
     * @param isAsync - 是否異步刪除
     * */
    fun remove(name: String, isAsync: Boolean = true) {
        val prefs = prefs
        if (prefs == null) {
            MGLogUtils.d("尚未初始化 SharedPreferences, 無法刪除 $name")
            return
        }
        with(prefs.edit()) {
            remove(name)
            if (isAsync) apply()
            else commit()
        }
    }

}