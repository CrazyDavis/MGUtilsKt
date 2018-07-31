package org.magicalwater.mgkotlin.mgutilskt.util

import java.lang.reflect.InvocationHandler
import java.util.*

/**
 * Created by magicalwater on 2018/1/16.
 */
class MGCalendarUtils {


    companion object {

        //獲取當前年月日
        fun getNow(handler: (year: Int, month: Int, day: Int) -> Unit) {
            val c = Calendar.getInstance()
            handler(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
        }

        //獲得某個 Date 的年月日
        fun getByDate(date: Date, handler: (CalendarData) -> Unit) {
            val c = Calendar.getInstance()
            c.time = date
            val calendar = CalendarData(c.get(Calendar.YEAR),  c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND))
            handler(calendar)
        }
    }


    data class CalendarData(val year: Int, val month: Int, val dayInMonth: Int,
                            val hour: Int, val minute: Int, val second: Int)
}