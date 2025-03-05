package com.oxad.fitox.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

class DarkBackgroundView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGradientBackground(canvas)
        drawAbstractShapes(canvas)
    }

    private fun drawGradientBackground(canvas: Canvas) {
        val gradient = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            intArrayOf(Color.BLACK, Color.DKGRAY, Color.parseColor("#121212")),
            null,
            Shader.TileMode.CLAMP
        )
        paint.shader = gradient
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun drawAbstractShapes(canvas: Canvas) {
        paint.shader = null

        // Draw a subtle glowing circle
        paint.color = Color.parseColor("#222222")
        canvas.drawCircle(width * 0.3f, height * 0.3f, width * 0.2f, paint)

        // Draw a semi-transparent arc
        paint.color = Color.parseColor("#333333")
        paint.alpha = 80
        canvas.drawArc(
            width * 0.5f, height * 0.5f, width.toFloat(), height.toFloat(),
            0f, 180f, true, paint
        )

        // Soft overlay for depth effect
        val overlayGradient = RadialGradient(
            width * 0.5f, height * 0.5f, width * 0.6f,
            intArrayOf(Color.TRANSPARENT, Color.parseColor("#1A1A1A")),
            null, Shader.TileMode.CLAMP
        )
        paint.shader = overlayGradient
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}
