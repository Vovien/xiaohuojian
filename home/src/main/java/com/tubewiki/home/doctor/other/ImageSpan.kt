package com.tubewiki.home.doctor.other

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.text.style.ImageSpan
import java.lang.ref.WeakReference

class ImageSpan(context: Context, bitmap: Bitmap) : ImageSpan(context, bitmap) {

    private var drawableRef: WeakReference<Drawable>? = null

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val textPaint = paint as TextPaint
        val fontMetrics = textPaint.fontMetrics
        val lineTop = y + fontMetrics.top
        val b = getCachedDrawable()
        val dy = lineTop + (bottom - lineTop) * 0.5f - b.bounds.height() * 0.55f
        canvas.save()
        canvas.translate(x, dy)
        b.draw(canvas)
        canvas.restore()
    }

    private fun getCachedDrawable(): Drawable {
        return drawableRef?.get() ?: drawable.also {
            drawableRef = WeakReference(it)
        }
    }

}