package com.example.drawer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private val path = Path()
    private var isEraserMode = false

    private val paint = Paint().apply {
        color = Color.CYAN
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val eraserPaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 30f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val bitmapPaint = Paint(Paint.DITHER_FLAG)

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (width > 0 && height > 0) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            canvas = Canvas(bitmap!!)
            canvas?.drawColor(Color.TRANSPARENT)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, bitmapPaint)
        }
        canvas.drawPath(path, if (isEraserMode) eraserPaint else paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
            }

            MotionEvent.ACTION_UP -> {
                if (isEraserMode) {
                    canvas?.drawPath(path, eraserPaint)
                } else {
                    canvas?.drawPath(path, paint)
                }
                path.reset()
            }
        }

        invalidate()
        return true
    }

    fun clearCanvas() {
        bitmap?.eraseColor(Color.TRANSPARENT)
        invalidate()
    }

    fun setEraserMode() {
        isEraserMode = true
        path.reset()
    }

    fun setBrushMode() {
        isEraserMode = false
        path.reset()
    }
}
