package org.magicalwater.mgkotlin.mgutilskt.util

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.*
import org.magicalwater.mgkotlin.mgextensionkt.px


/**
 * Created by magicalwater on 2018/1/27.
 * 富文本構建
 */
class MGRichTextUtils {


    //設置字體樣式, 從上而下分別是, 正常, 粗體, 斜體, 粗斜體
    companion object {

        /**
         * 參數說明
         * color - 字體顏色
         * size - 字體大小(px)
         * relativeSize - 字體縮放倍數
         * delete - 刪除線
         *
         * 其餘還有下劃線, 斜體等尚未加上
         */
        val RICHTEXT_STRING = "string"
        val RICHTEXT_COLOR = "color"
        val RICHTEXT_SIZE = "size"
        val RICHTEXT_RSIZE = "relativesize"
        val RICHTEXT_DELETE = "delete"
        val RICHTEXT_FONT = "font"

        //自行相關參數
        val FONT_NORMAL = "NORMAL"
        val FONT_BOLD = "BOLD"
        val FONT_ITALIC = "ITALIC"
        val FONT_BOLD_ITALIC = "BOLD_ITALIC"
    }

    //組合多個屬性字串
    fun comb(vararg attr: RichAttr): SpannableStringBuilder {
        return comb(attr.toList())
    }


    fun comb(attr: List<RichAttr>): SpannableStringBuilder {
        var richList: MutableList<Map<String,Any>> = mutableListOf()

        attr.forEach {
            var attrMap: MutableMap<String, Any> = mutableMapOf()

            attrMap[MGRichTextUtils.RICHTEXT_STRING] = it.text
            attrMap[MGRichTextUtils.RICHTEXT_SIZE] = it.size
            attrMap[MGRichTextUtils.RICHTEXT_COLOR] = it.color

            attrMap[MGRichTextUtils.RICHTEXT_FONT] = when (it.font) {
                RichFontType.NORMAL -> FONT_NORMAL
                RichFontType.BOLD -> FONT_BOLD
                RichFontType.ITALIC -> FONT_ITALIC
                RichFontType.BOLD_ITALIC -> FONT_BOLD_ITALIC
            }

            if (it.delete) {
                //隨便設置值即可
                attrMap[MGRichTextUtils.RICHTEXT_DELETE] = true
            }

            richList.add(attrMap)
        }

        return getSpannableString(richList)
    }

    /**
     * 根據傳入的 Map 的 List 組成富文本
     * 結構組成
     *  內層 Map(屬性組) - 一個屬性組是一段文字
     *  外層 List - 將所有屬性組連接起來就是富文本
     * @param list
     * @return
     */
    private fun getSpannableString(list: List<Map<String, Any>>): SpannableStringBuilder {

        list.indices
        val ssb = SpannableStringBuilder("")
        var position = 0
        list.indices
                .asSequence()
                .map { list[it] }
                .forEach {
                    try {
                        val st = it[RICHTEXT_STRING] as String
                        ssb.append(st)
                        val len = st.length

                        if (it.containsKey(RICHTEXT_COLOR)) {
                            val color = (it[RICHTEXT_COLOR] as Int).toInt()
                            ssb.setSpan(ForegroundColorSpan(color), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                        }

                        if (it.containsKey(RICHTEXT_SIZE)) {
                            val size = (it[RICHTEXT_SIZE] as Int).toInt()
                            ssb.setSpan(AbsoluteSizeSpan(size), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }

                        if (it.containsKey(RICHTEXT_RSIZE)) {
                            val size = (it[RICHTEXT_RSIZE] as Float).toFloat()
                            ssb.setSpan(RelativeSizeSpan(size), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }

                        if (it.containsKey(RICHTEXT_DELETE)) {
                            ssb.setSpan(StrikethroughSpan(), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }

                        if (it.containsKey(RICHTEXT_FONT)) {
                            val typeface = when (it[RICHTEXT_FONT]) {
                                FONT_NORMAL -> android.graphics.Typeface.NORMAL
                                FONT_BOLD -> android.graphics.Typeface.BOLD
                                FONT_ITALIC -> android.graphics.Typeface.ITALIC
                                FONT_BOLD_ITALIC -> android.graphics.Typeface.BOLD_ITALIC
                                else -> android.graphics.Typeface.NORMAL
                            }
                            ssb.setSpan(StyleSpan(typeface), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }

                        //              android.text.style.RelativeSizeSpan
                        position += len

                    } catch (e: Exception) {
                        return ssb
                    }
                }

        return ssb
    }


    data class RichAttr(var text: String,
                        var size: Int = 12.px,
                        var color: Int = Color.BLACK,
                        var delete: Boolean = false,
                        var font: RichFontType = RichFontType.NORMAL,
                        var scale: Float? = null)


    //字體類型
    enum class RichFontType {
        NORMAL, BOLD, ITALIC, BOLD_ITALIC
    }
}