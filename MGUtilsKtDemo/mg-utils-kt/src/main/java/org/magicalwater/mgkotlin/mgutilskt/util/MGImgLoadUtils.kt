package org.magicalwater.mgkotlin.mgutilskt.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.module.GlideModule
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import java.net.URI
import java.net.URL

/**
 * Created by 志朋 on 2017/12/14.
 * 圖片加載class
 */
class MGImgLoadUtils {

    companion object {

        fun load(context: Context, resId: Int, width: Int? = null, height: Int? = null, handler: (Bitmap) -> Unit) {
            val target = object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    if (resource != null) handler(resource)
                }
            }
            if (width != null && height != null) {
                Glide.with(context).load(resId).asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE).format(DecodeFormat.PREFER_ARGB_8888).override(width, height).into(target)
            } else {
                Glide.with(context).load(resId).asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE).format(DecodeFormat.PREFER_ARGB_8888).into(target)
            }
        }

        fun load(context: Context, resId: Int, handler: (GlideDrawable) -> Unit) {
            val target = object: SimpleTarget<GlideDrawable>() {
                override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>?) {
                    if (resource != null) handler(resource)
                }
            }
            Glide.with(context).load(resId).fitCenter().into(target)
        }

        //直接將資源圖片載入進 imageView
        fun load(view: ImageView, @DrawableRes resId: Int) {
            Glide.with(view.context).load(resId).fitCenter().into(view)
        }


        //從網路撈圖片進 imageView
        fun load(view: ImageView, url: URL) {
            Glide.with(view.context).load(url.toString()).fitCenter().into(view)
        }

        //從網路撈圖片, 返回指定的圖片, 將圖片設定為指定寬高
        fun load(context: Context, url: URL, handler: (Bitmap) -> Unit) {
            val target = object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    if (resource != null) handler(resource)
                }
            }
            Glide.with(context).load(url.toString()).asBitmap().fitCenter().into(target)
        }

        //傳入uri(本地圖片檔案), 返回指定的圖片, 將圖片設定為指定寬高
        fun load(context: Context, uri: Uri, width: Int? = null, height: Int? = null, handler: (Bitmap) -> Unit) {

            val target = object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    if (resource != null) handler(resource)
                }
            }

            if (width != null && height != null) {
                Glide.with(context).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(width, height).into(target)
            } else {
                Glide.with(context).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().format(DecodeFormat.PREFER_ARGB_8888).into(target)
            }

        }

    }


}
