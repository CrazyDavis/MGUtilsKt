package org.magicalwater.mgkotlin.mgutilskt.glide

import android.content.Context
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

//glide擴展, 圓角圖片, 傳入的radius是px
class MGGlideRoundTransformation(context: Context, radius: Float): BitmapTransformation(context) {

    private val mRadius = radius

    override fun getId(): String = javaClass.name + mRadius

    override fun transform(pool: BitmapPool?, toTransform: Bitmap?, outWidth: Int, outHeight: Int): Bitmap {
        return roundCrop(pool, toTransform) ?: emptyBitmap(outWidth, outHeight)
    }

    private fun roundCrop(pool: BitmapPool?, source: Bitmap?): Bitmap? {
        source ?: return null
        val result = pool?.get(source.width, source.height, Bitmap.Config.ARGB_8888) ?:
        Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint()
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint)
        return result
    }

    private fun emptyBitmap(outWidth: Int, outHeight: Int): Bitmap {
        val w = outWidth
        val h = outHeight
        val conf = Bitmap.Config.ARGB_8888 // see other conf types
        return Bitmap.createBitmap(w, h, conf)
    }

}