package org.magicalwater.mgkotlin.mgutilskt.util

import android.graphics.pdf.PdfDocument.PageInfo
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import kotlin.reflect.KClass


/**
 * Created by 志朋 on 2017/12/11.
 */
class MGFgtUtils {

    companion object {

        fun isFgtExist(manager: FragmentManager, tag: String): Boolean {
            return manager.findFragmentByTag(tag) == null
        }

        fun getNowShowFgt(manager: FragmentManager, tag: String): Fragment? {
            return manager.findFragmentByTag(tag)
        }

        fun getNowShowFgt(manager: FragmentManager, layoutID: Int): Fragment? {
            return manager.findFragmentById(layoutID)
        }

        @Throws(NullPointerException::class)
        fun <T : Fragment> getFgt(manager: FragmentManager, tag: String, fragmentClass: KClass<T>): Fragment? {
            var fragment = manager.findFragmentByTag(tag)
            if (fragment == null) {
                try {
                    fragment = fragmentClass.java.newInstance() as Fragment
                } catch (e: java.lang.InstantiationException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

            }
            return fragment
        }


//    fun getFragemnt(fragmentManager: FragmentManager, tag: String, fragmentClass: Class<*>, pageInfo: PageInfo): Fragment? {
//
//        val fragment = getFragemnt(fragmentManager, tag, fragmentClass)
//
//        if (fragment is ConnectDataHandler) {
//            (fragment as ConnectDataHandler).connectDataHandler(pageInfo)
//        }
//
//        return fragment
//    }

    }

}