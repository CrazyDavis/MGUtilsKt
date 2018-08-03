package org.magicalwater.mgkotlin.mgutilskt.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import org.magicalwater.mgkotlin.mgextensionkt.px

class MGDrawableUtils {

    companion object {
        val GRADIENT_LINEAR = GradientDrawable.LINEAR_GRADIENT
        val GRADIENT_RADIAL = GradientDrawable.RADIAL_GRADIENT
        val GRADIENT_SWEEP = GradientDrawable.SWEEP_GRADIENT

        val SHAPE_RECTANGLE = GradientDrawable.RECTANGLE
        val SHAPE_OVAL = GradientDrawable.OVAL
        val SHAPE_LINE = GradientDrawable.LINE
    }

    private var mGradient: Gradient = Gradient(GRADIENT_LINEAR, GradientDrawable.Orientation.TOP_BOTTOM, mutableListOf(Color.WHITE))
    private var mColor: Int? = null
    private var mShapeType: Int = SHAPE_RECTANGLE
    private var mRadius: Float = 0f

    //長寬
    private var mSize: Pair<Int, Int>? = null

    private var mStroke: Stroke? = null

    fun setGradient(gradient: Gradient): MGDrawableUtils {
        mGradient = gradient
        return this
    }

    fun setSolid(color: Int?): MGDrawableUtils {
        mColor = color
        return this
    }

    fun setStroke(stroke: Stroke?): MGDrawableUtils {
        mStroke = stroke
        return this
    }

    fun setShape(type: Int): MGDrawableUtils {
        mShapeType = type
        return this
    }

    fun setRadius(radius: Float): MGDrawableUtils {
        mRadius = radius
        return this
    }

    fun setSize(size: Pair<Int,Int>?): MGDrawableUtils {
        mSize = size
        return this
    }

    fun build(): Drawable {
        val gradient = mGradient
        val stroke = mStroke
        val size = mSize
        val color = mColor
        val drawable = GradientDrawable(gradient.angle, gradient.colors.toIntArray())
        drawable.gradientType = gradient.type
        drawable.shape = mShapeType
        drawable.cornerRadius = mRadius
        if (stroke != null) {
            drawable.setStroke(stroke.width, stroke.color,
                    stroke.dashWidth?.toFloat() ?: 0f,
                    stroke.dashGap?.toFloat() ?: 0f)
        }
        if (color != null) {
            drawable.setColor(color)
        }
        if (size != null) {
            drawable.setSize(size.first, size.second)
        }
        return drawable
    }

    data class Stroke(val width: Int, val color: Int, val dashGap: Int? = null, val dashWidth: Int? = null)
    data class Gradient(val type: Int, val angle: GradientDrawable.Orientation, val colors: MutableList<Int>)
}