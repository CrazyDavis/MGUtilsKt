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

        fun load(context: Context, @DrawableRes resId: Int, shape: Shape, radius: Float? = null, width: Int? = null, height: Int? = null, handler: (Bitmap) -> Unit) {
            val target = object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    if (resource != null) handler(resource)
                }
            }
            loadBitmapByGlide(context, resId, getTransform(context, shape, radius), width, height)?.into(target)
        }

        fun load(context: Context, resId: Int, shape: Shape, radius: Float? = null, handler: (GlideDrawable) -> Unit) {
            val target = object: SimpleTarget<GlideDrawable>() {
                override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>?) {
                    if (resource != null) handler(resource)
                }
            }
            loadDrawableByGilde(context, resId, getTransform(context, shape, radius))?.into(target)
        }

        //直接將資源圖片載入進 imageView
        fun load(view: ImageView, @DrawableRes resId: Int, shape: Shape, radius: Float? = null) {
            loadDrawableByGilde(view.context, resId, getTransform(view.context, shape, radius))?.into(view)
        }

        //從網路撈圖片進 imageView
        fun load(view: ImageView, url: URL, shape: Shape, radius: Float? = null) {
            loadDrawableByGilde(view.context, url.toString(), getTransform(view.context, shape, radius))?.into(view)
        }

        //從網路撈圖片, 返回指定的圖片, 將圖片設定為指定寬高
        fun load(context: Context, url: URL, shape: Shape, radius: Float? = null, handler: (Bitmap) -> Unit) {
            val target = object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    if (resource != null) handler(resource)
                }
            }
            loadBitmapByGlide(context, url.toString(), getTransform(context, shape, radius))?.into(target)
        }

        //傳入uri(本地圖片檔案), 返回指定的圖片, 將圖片設定為指定寬高
        fun load(context: Context, uri: Uri, shape: Shape, radius: Float? = null, width: Int? = null, height: Int? = null, handler: (Bitmap) -> Unit) {
            val target = object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    if (resource != null) handler(resource)
                }
            }
            loadBitmapByGlide(context, uri, getTransform(context, shape, radius), width, height)?.into(target)
        }


        fun getTransform(context: Context, type: Shape, radius: Float? = null): BitmapTransformation? {
            return when (type) {
                Shape.NORMAL ->  null
                Shape.CIRCLE -> MGGlideCircleTransformation(context)
                Shape.ROUND -> MGGlideRoundTransformation(context, radius ?: 0f)
            }
        }




        private fun <T> loadByGlide(context: Context, from: T): DrawableTypeRequest<T>? {
             val b = when (from) {
                is Int -> Glide.with(context).load(from)
                is String -> Glide.with(context).load(from)
                is Int -> Glide.with(context).load(from)
                else -> null
            }
            return b as DrawableTypeRequest<T>?
        }

        private fun <T> loadBitmapByGlide(context: Context, from: T, transform: BitmapTransformation? = null, width: Int? = null, height: Int? = null): BitmapRequestBuilder<T, Bitmap>? {
            val b = loadByGlide(context, from)
            var builder = b?.asBitmap()
                    ?.fitCenter()
                    ?.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    ?.format(DecodeFormat.PREFER_ARGB_8888)
            if (transform != null) {
                builder = builder?.transform(transform)
            }
            return overrideSizeByGlide(builder, width, height)
        }

        private fun <T> loadDrawableByGilde(context: Context, from: T, transform: BitmapTransformation? = null, width: Int? = null, height: Int? = null): DrawableRequestBuilder<T>? {
            var builder = loadByGlide(context, from)?.fitCenter()
            if (transform != null) {
                builder = builder?.transform(transform)
            }
            return overrideSizeByGlide(builder, width, height)
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


    enum class Shape {
        CIRCLE, ROUND, NORMAL
    }

}
