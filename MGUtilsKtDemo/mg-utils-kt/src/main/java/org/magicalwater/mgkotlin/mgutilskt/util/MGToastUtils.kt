package org.magicalwater.mgkotlin.mgutilskt.util

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

/**
 * Created by 志朋 on 2017/12/10.
 */
class MGToastUtils {

    companion object {
        fun show(fragment: Fragment, message: CharSequence) {
            fragment.toast(message)
        }

        fun show(activity: Activity, message: CharSequence) {
            activity.toast(message)
        }

        fun show(context: Context, message: CharSequence) {
            context.toast(message)
        }
    }

}

