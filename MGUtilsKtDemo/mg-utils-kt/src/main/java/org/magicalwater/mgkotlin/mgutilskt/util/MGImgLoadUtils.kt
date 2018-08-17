package org.magicalwater.mgkotlin.mgutilskt.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.DrawableRequestBuilder
import com.bumptech.glide.DrawableTypeRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import org.magicalwater.mgkotlin.mgutilskt.glide.MGGlideCircleTransformation
import org.magicalwater.mgkotlin.mgutilskt.glide.MGGlideRoundTransformation
import java.net.URL

/**
 * Created by 志朋 on 2017/12/14.
 * 圖片加載class
 * transform參數可自行帶入
 */
class MGImgLoadUtils {

    companion object {

        /**
         * @param from - 可傳入 drawable resId, URL, String, Uri, 分別代表從資源/網路/網路/檔案拉圖
         * */
        fun loadBitmap(context: Context, from: Any, attr: ImageAttr, handler: (Bitmap) -> Unit) {
            val target = object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    if (resource != null) handler(resource)
                }
            }
            loadBitmapByGlide(context, from, attr)?.into(target)
        }

        fun loadDrawable(context: Context, from: Any, attr: ImageAttr, handler: (GlideDrawable) -> Unit) {
            val target = object: SimpleTarget<GlideDrawable>() {
                override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>?) {
                    if (resource != null) handler(resource)
                }
            }
            loadDrawableByGilde(context, from, attr)?.into(target)
        }

        //直接將資源圖片載入進 imageView
        fun load(view: ImageView, from: Any, attr: ImageAttr) {
            loadDrawableByGilde(view.context, from, attr)?.into(view)
        }

        private fun getTransform(context: Context, type: Shape, radius: Float? = null): BitmapTransformation? {
            return when (type) {
                Shape.NORMAL ->  null
                Shape.CIRCLE -> MGGlideCircleTransformation(context)
                Shape.ROUND -> MGGlideRoundTransformation(context, radius ?: 0f)
            }
        }

        private fun <T> loadByGlide(context: Context, from: T): DrawableTypeRequest<T>? {
            val b = when (from) {
                is Int -> Glide.with(context).load(from)
                is URL -> Glide.with(context).load(from.toString())
                is Int -> Glide.with(context).load(from)
                is String -> Glide.with(context).load(from)
                else -> null
            }
            return b as DrawableTypeRequest<T>?
        }

        private fun <T> loadBitmapByGlide(context: Context, from: T, attr: ImageAttr): BitmapRequestBuilder<T, Bitmap>? {
            val b = loadByGlide(context, from)
            var builder = b?.asBitmap()
                    ?.fitCenter()
                    ?.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    ?.format(DecodeFormat.PREFER_ARGB_8888)
            val transform = getTransform(context, attr.shape, attr.radius)
            if (transform != null) {
                builder = builder?.transform(transform)
            }
            return overrideSizeByGlide(builder, attr.width, attr.height)
        }

        private fun <T> loadDrawableByGilde(context: Context, from: T, attr: ImageAttr): DrawableRequestBuilder<T>? {
            var builder = loadByGlide(context, from)?.fitCenter()
            val transform = getTransform(context, attr.shape, attr.radius)
            if (transform != null) {
                builder = builder?.transform(transform)
            }
            return overrideSizeByGlide(builder, attr.width, attr.height)
        }

        private fun <T> overrideSizeByGlide(builder: DrawableRequestBuilder<T>?, width: Int?, height: Int?): DrawableRequestBuilder<T>? {
            if (width != null && height != null) {
                return builder?.override(width, height)
            }
            return builder
        }

        private fun <T> overrideSizeByGlide(builder: BitmapRequestBuilder<T, Bitmap>?, width: Int?, height: Int?): BitmapRequestBuilder<T, Bitmap>? {
            if (width != null && height != null) {
                return builder?.override(width, height)
            }
            return builder
        }
    }

    class ImageAttr {

        var shape: Shape = Shape.NORMAL
        var radius: Float = 0f
        var width: Int? = null
        var height: Int? = null

        constructor()

        constructor(shape: Shape, radius: Float = 0f) {
            this.shape = shape
            this.radius = radius
        }

        constructor(width: Int?, height: Int?) {
            this.width = width
            this.height = height
        }

        constructor(shape: Shape, radius: Float = 0f, width: Int, height: Int) {
            this.shape = shape
            this.radius = radius
            this.width = width
            this.height = height
        }
    }

    enum class Shape {
        CIRCLE, ROUND, NORMAL
    }

}
