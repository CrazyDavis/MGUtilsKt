package org.magicalwater.mgkotlin.mgutilsktdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.magicalwater.mgkotlin.mgutilskt.util.MGImgLoadUtils
import org.magicalwater.mgkotlin.mgutilskt.util.MGRichTextUtils
import org.magicalwater.mgkotlin.mgutilskt.util.MGVersionUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MGImgLoadUtils.load(mImgView, R.drawable.ic_demo2, MGImgLoadUtils.ImageAttr(MGImgLoadUtils.Shape.ROUND, 64f))

        mTitleLabel.text = MGRichTextUtils()
                .appendString("第一段一般文字\n")
                .appendString("第二段粗體文字\n")
                .setFontType(MGRichTextUtils.RichFontType.BOLD)
                .appendString("第三段刪除文字\n")
                .setTextDelete(true)
                .appendString("第四段自訂大小文字\n")
                .setTextSize(120)
                .build()


        //比較版號
        val isNew = MGVersionUtils.compareIsNew("0.0.1", "0.0.1.0.0.0.0.1")
        println("版號是否比較新: $isNew")
    }
}
