package org.magicalwater.mgkotlin.mgutilskt.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by magicalwater on 2018/1/14.
 * 日期工具類, 注意一點事, date裡的time皆是毫秒, 要轉為時間戳記需要再除以1000
 */
class MGDateUtils {

    companion object {

        //將字串轉為Date
        fun convertToDate(text: String, formatPattern: String): Date? {
            return try {
                val parseDate = SimpleDateFormat(formatPattern).parse(text)
                parseDate
            } catch (e: ParseException) {
                //To something
                MGLogUtils.d("解析日期失敗 - text = $text, pattern = $formatPattern: ${e.message}")
                null
            }
        }

        //將date轉為字串
        fun convertToString(date: Date, formatPattern: String): String {
            val sdf = SimpleDateFormat(formatPattern)
            return sdf.format(date)
        }


        //直接設定年月日, 帶回date
        fun getDate(year: Int, month: Int, dayInMonth: Int): Date {
            val calendar = GregorianCalendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, dayInMonth)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            return calendar.time
        }

        //設定精細到分
        fun getDate(year: Int, month: Int, dayInMonth: Int, hour: Int, minute: Int): Date {
            val calendar = GregorianCalendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, dayInMonth)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            return calendar.time
        }


        //得到某天的最後 23:59:59
        fun getDateLastTime(d: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = d
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            return calendar.time
        }


        //判斷兩個date是否在同一天
        fun inSameDay(date1: Date, Date2: Date): Boolean {
            val calendar = Calendar.getInstance()
            calendar.time = date1
            val year1 = calendar.get(Calendar.YEAR)
            val day1 = calendar.get(Calendar.DAY_OF_YEAR)

            calendar.time = Date2
            val year2 = calendar.get(Calendar.YEAR)
            val day2 = calendar.get(Calendar.DAY_OF_YEAR)

            return year1 == year2 && day1 == day2
        }
    }
}