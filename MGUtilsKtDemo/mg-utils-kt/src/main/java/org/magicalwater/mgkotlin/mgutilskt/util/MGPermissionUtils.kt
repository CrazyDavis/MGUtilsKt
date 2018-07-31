package org.magicalwater.mgkotlin.mgutilskt.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Created by magicalwater on 2018/1/22.
 * 此類別不建議直接使用, 配合 MGBaseAty 封裝好的方式調用會更清楚
 */
class MGPermissionUtils {

    //確認是否第一次要求權限的 key, 需要配合 SettingUtils 使用
    val KEY_FIRST_REQUEST = "MG_FIRST_REQUEST"

    /**
     * 判斷是否有權限
     * PackageManager.PERMISSION_GRANTED 授予
     * PackageManager.PERMISSION_DENIED 缺少
     */
    fun checkPermissions(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 6.0以上申請權限，必須傳入activity
     *
     * @param activity
     * @param permission
     * @param requestCode
     * @param listener
     */
    fun requestPermissions(activity: Activity, permission: String, requestCode: Int, delegate: PermissionCheckDelegate?) {
        if (Build.VERSION.SDK_INT >= 23) {
            val checkCallPhonePermission = ContextCompat.checkSelfPermission(activity.applicationContext, permission)
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                rqeuestPermission(activity, permission, requestCode, delegate)
            } else {
                //權限已有, 直接調用
                delegate?.permissionStatus(PermissionStatus.GRANTED, permission, requestCode)
            }
        } else {
            //6.0以下不用檢測
            delegate?.permissionStatus(PermissionStatus.GRANTED, permission, requestCode)
        }
    }

    /**
     * 要求權限, 檢查系統是否跳出提示
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun rqeuestPermission(activity: Activity, permission: String, requestCode: Int, delegate: PermissionCheckDelegate?) {
        //先檢查系統是否會跳出提示讓使用者選擇授予與否
        //但此方法有坑, app第一次要求, 此方法會回傳 false, 但實際上會跳提示使用者授予權限
        //所以須先檢查app是否第一次開啟並且要求權限
        val prompt = activity.shouldShowRequestPermissionRationale(permission)

        val settingUtils = MGSettingUtils()
        val isFirstRequest = settingUtils.get(KEY_FIRST_REQUEST, true)

        if (!isFirstRequest && !prompt) {
            //將第一次開啟要求權限的設定改為false
            settingUtils.put(KEY_FIRST_REQUEST, false)
            //使用者不授予, 且系統也不跳提示, 傳回去看如何處理(跳提示框等等)
            delegate?.permissionStatus(PermissionStatus.REJECT, permission, requestCode)
            return
        }

        //剩下就是系統會跳出提示, 這邊傳回等待使用者選擇
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        delegate?.permissionStatus(PermissionStatus.WAIT_USER, permission, requestCode)
    }

    interface PermissionCheckDelegate {
        fun permissionStatus(status: PermissionStatus, permission: String, requestCode: Int)
    }


    enum class PermissionStatus {
        GRANTED, //擁有
        WAIT_USER, //等待使用者選擇, 在 activity 的 result 處等待回傳
        REJECT //使用者不授予, 且系統也不跳提示
    }
}