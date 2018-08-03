package org.magicalwater.mgkotlin.mgutilskt.util

import android.text.TextUtils
import android.os.Build
import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


/**
 * 工具參照
 * https://github.com/afinal/SecuritySharedPreference/blob/master/SecuritySharedPreference/app/src/main/java/com/domain/securitysharedpreference/MGEncryptUtil.java
 *
 * aes加密解密工具
 * */
class MGEncryptUtils() {

    private lateinit var mKey: String

    companion object {
        val shared = MGEncryptUtils()
    }

    fun initKey(context: Context) {
        //獲得裝置唯一辨識序列號當作密鑰
        val serialNo = getDeviceSerialNumber(context)
        //加密隨機字串生成AES key
        mKey = sha("$serialNo#\$ERDTS\$D%F^Gojikbh")!!.substring(0, 16)
    }

    /**
     * Gets the hardware serial number of this device.
     *
     * @return serial number or Settings.Secure.ANDROID_ID if not available.
     */
    @SuppressLint("HardwareIds")
    private fun getDeviceSerialNumber(context: Context): String {
        // We're using the Reflection API because Build.SERIAL is only available
        // since API Level 9 (Gingerbread, Android 2.3).
        return try {
            val deviceSerial = Build::class.java.getField("SERIAL").get(null) as String
            if (TextUtils.isEmpty(deviceSerial)) {
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            } else {
                deviceSerial
            }
        } catch (ignored: Exception) {
            // Fall back  to Android_ID
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }
    }

    /**
     * SHA加密
     * @param strText 明文
     * @return
     */
    private fun sha(strText: String?): String? {
        var strResult: String? = null
        // 是否為有效字串
        if (strText != null && strText.isNotEmpty()) {
            try {
                // SHA 加密開始
                val messageDigest = MessageDigest.getInstance("SHA-256")
                // 傳入加密的字串
                messageDigest.update(strText.toByteArray())
                val byteBuffer = messageDigest.digest()
                val strHexString = StringBuffer()
                for (i in byteBuffer.indices) {
                    val hex = Integer.toHexString(0xff and byteBuffer[i].toInt())
                    if (hex.length == 1) {
                        strHexString.append('0')
                    }
                    strHexString.append(hex)
                }
                strResult = strHexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        return strResult
    }


    /**
     * AES128加密
     * @param plainText 明文
     * @return
     */
    fun encrypt(plainText: String): String? {
        return try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val keyspec = SecretKeySpec(mKey.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, keyspec)
            val encrypted = cipher.doFinal(plainText.toByteArray())
            Base64.encodeToString(encrypted, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    /**
     * AES128解密
     * @param cipherText 密文
     * @return
     */
    fun decrypt(cipherText: String): String? {
        return try {
            val encrypted1 = Base64.decode(cipherText, Base64.NO_WRAP)
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val keyspec = SecretKeySpec(mKey.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, keyspec)
            val original = cipher.doFinal(encrypted1)
            String(original)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
}