package org.magicalwater.mgkotlin.mgutilskt.security

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import org.magicalwater.mgkotlin.mgutilskt.util.MGEncryptUtils
import android.R.attr.key
import android.R.attr.key
import android.os.Build
import android.annotation.TargetApi
import android.R.id.edit

class MGSecuritySharedPreference(context: Context, name: String, mode: Int): SharedPreferences {

    private var mSharedPreferences: SharedPreferences =
            if (TextUtils.isEmpty(name)) {
                PreferenceManager.getDefaultSharedPreferences(context)
            } else {
                context.getSharedPreferences(name, mode)
            }

    init {
        MGEncryptUtils.shared.initKey(context)
    }

    /**
     * encrypt function
     * @return cipherText base64
     */
    private fun encryptPreference(plainText: String): String {
        return MGEncryptUtils.shared.encrypt(plainText) ?: ""
    }

    /**
     * decrypt function
     * @return plainText
     */
    private fun decryptPreference(cipherText: String): String {
        return MGEncryptUtils.shared.decrypt(cipherText) ?: ""
    }

    override fun contains(key: String?): Boolean {
        return mSharedPreferences.contains(encryptPreference(key ?:""))
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        val encryptValue = mSharedPreferences.getString(encryptPreference(key ?: ""), null) ?: return defValue
        return decryptPreference(encryptValue).toBoolean()
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun getInt(key: String?, defValue: Int): Int {
        val encryptValue = mSharedPreferences.getString(encryptPreference(key ?: ""), null) ?: return defValue
        return Integer.parseInt(decryptPreference(encryptValue))
    }

    override fun getAll(): MutableMap<String, Any> {
        val encryptMap = mSharedPreferences.all
        val decryptMap = mutableMapOf<String, Any>()
        for ((key, cipherText) in encryptMap) {
            if (cipherText != null) {
                decryptMap[key] = cipherText.toString()
            }
        }
        return decryptMap
    }

    override fun edit(): SharedPreferences.Editor {
        return SecurityEditor()
    }

    override fun getLong(key: String?, defValue: Long): Long {
        val encryptValue = mSharedPreferences.getString(encryptPreference(key ?: ""), null) ?: return defValue
        return decryptPreference(encryptValue).toLong()
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        val encryptValue = mSharedPreferences.getString(encryptPreference(key ?: ""), null) ?: return defValue
        return decryptPreference(encryptValue).toFloat()
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        val encryptSet = mSharedPreferences.getStringSet(encryptPreference(key ?: ""), null) ?: return defValues ?: mutableSetOf()
        val decryptSet = mutableSetOf<String>()
        for (encryptValue in encryptSet) {
            decryptSet.add(decryptPreference(encryptValue))
        }
        return decryptSet
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun getString(key: String?, defValue: String?): String {
        val encryptValue = mSharedPreferences.getString(encryptPreference(key ?: ""), null)
        return if (encryptValue == null) defValue ?: "" else decryptPreference(encryptValue)
    }


    /**
     * 自動加密editor
     */
    internal inner class SecurityEditor internal constructor() : SharedPreferences.Editor {

        private val mEditor: SharedPreferences.Editor = mSharedPreferences.edit()

        override fun putString(key: String, value: String?): SharedPreferences.Editor {
            mEditor.putString(encryptPreference(key), encryptPreference(value ?: ""))
            return this
        }

        override fun putStringSet(key: String, values: Set<String>): SharedPreferences.Editor {
            val encryptSet = mutableSetOf<String>()
            for (value in values) {
                encryptSet.add(encryptPreference(value))
            }
            mEditor.putStringSet(encryptPreference(key), encryptSet)
            return this
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor {
            mEditor.putString(encryptPreference(key), encryptPreference(Integer.toString(value)))
            return this
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor {
            mEditor.putString(encryptPreference(key), encryptPreference(java.lang.Long.toString(value)))
            return this
        }

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor {
            mEditor.putString(encryptPreference(key), encryptPreference(java.lang.Float.toString(value)))
            return this
        }

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
            mEditor.putString(encryptPreference(key), encryptPreference(java.lang.Boolean.toString(value)))
            return this
        }

        override fun remove(key: String): SharedPreferences.Editor {
            mEditor.remove(encryptPreference(key))
            return this
        }

        /**
         * Mark in the editor to remove all values from the preferences.
         * @return this
         */
        override fun clear(): SharedPreferences.Editor {
            mEditor.clear()
            return this
        }

        /**
         * @return - 回傳是否提交成功
         */
        override fun commit(): Boolean {
            return mEditor.commit()
        }

        /**
         * Unlike commit(), which writes its preferences out to persistent storage synchronously,
         * apply() commits its changes to the in-memory SharedPreferences immediately but starts
         * an asynchronous commit to disk and you won't be notified of any failures.
         */
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        override fun apply() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                mEditor.apply()
            } else {
                commit()
            }
        }
    }

}