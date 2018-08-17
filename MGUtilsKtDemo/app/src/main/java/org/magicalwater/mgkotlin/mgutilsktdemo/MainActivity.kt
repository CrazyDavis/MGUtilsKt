package org.magicalwater.mgkotlin.mgutilsktdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.magicalwater.mgkotlin.mgutilskt.util.MGImgLoadUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MGImgLoadUtils.load(mImgView, R.drawable.ic_demo2, MGImgLoadUtils.ImageAttr(MGImgLoadUtils.Shape.ROUND, 64f))
    }
}
