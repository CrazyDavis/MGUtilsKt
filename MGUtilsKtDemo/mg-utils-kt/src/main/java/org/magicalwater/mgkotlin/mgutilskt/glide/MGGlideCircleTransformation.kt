package org.magicalwater.mgkotlin.mgutilskt.glide

import android.content.Context
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import android.graphics.Bitmap


//glide擴展, 轉換圓形圖片
class MGGlideCircleTransformation(context: Context): BitmapTransformation(context) {

    override fun getId(): String = javaClass.name

    override fun transform(pool: BitmapPool?, toTransform: Bitmap?, outWidth: Int, outHeight: Int): Bitmap {
        return circleCorp(pool, toTransform) ?: emptyBitmap(outWidth, outHeight)
    }

    private fun circleCorp(pool: BitmapPool?, source: Bitmap?): Bitmap? {
        source ?: return null

        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squared = Bitmap.createBitmap(source, x, y, size, size)
        val result = pool?.get(size, size, Bitmap.Config.ARGB_8888) ?:
                Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(result)
        val paint = Paint()
        paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        return result
    }


    private fun emptyBitmap(outWidth: Int, outHeight: Int): Bitmap {
        val w = outWidth
        val h = outHeight
        val conf = Bitmap.Config.ARGB_8888 // see other conf types
        return Bitmap.createBitmap(w, h, conf)
    }

}