package org.magicalwater.mgkotlin.mgutilskt.util

import android.app.Activity
import android.app.AlarmManager
import android.content.Context.ALARM_SERVICE
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlin.reflect.KClass


/**
 * Created by magicalwater on 2018/2/3.
 * 重啟app工具
 */
class MGAppLaunchUtils {

    companion object {

        /**
         * 重啟app
         * @param context - 當前所在的activity
         * @param activity - 重啟到哪個畫面
         * @param delay - 關閉app後多久後重啟
         * */
        fun restartApplication(context: Context, activity: Class<out Activity>, delay: Long = 1000) {

            //設定重啟的intent
            val intent = Intent(context, activity)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

            //鬧鐘管理, 用於1秒後重啟app
            val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + delay, restartIntent)

            //殺掉進程(自己)
            android.os.Process.killProcess(android.os.Process.myPid())

            //關閉整個app
            System.exit(0)
        }
    }

}