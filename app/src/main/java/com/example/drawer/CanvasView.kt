package com.example.drawer

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private val path: Path = Path()
    private var isEraserMode: Boolean = false

    private val previousFrames: MutableList<Bitmap> = mutableListOf()
    private var isPlaying: Boolean = false
    private var currentFrameIndex: Int = 0

    private val paint: Paint = Paint().apply {
        color = Color.CYAN
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val eraserPaint: Paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 30f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val bitmapPaint: Paint = Paint(Paint.DITHER_FLAG)
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val animationRunnable: Runnable = object : Runnable {
        override fun run() {
            if (isPlaying && previousFrames.isNotEmpty()) {
                bitmap = previousFrames[currentFrameIndex].copy(Bitmap.Config.ARGB_8888, true)
                canvas = Canvas(bitmap!!)
                invalidate()

                currentFrameIndex = (currentFrameIndex + 1) % previousFrames.size
                handler.postDelayed(this, 100)
            }
        }
    }

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

        previousFrames.lastOrNull()?.let { previousBitmap ->
            val paintTransparent = Paint().apply {
                alpha = 100
            }
            canvas.drawBitmap(previousBitmap, 0f, 0f, paintTransparent)
        }

        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, bitmapPaint)
        }
        canvas.drawPath(path, if (isEraserMode) eraserPaint else paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isPlaying) return false

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

    fun saveCurrentFrame() {
        bitmap?.let {
            previousFrames.add(it.copy(it.config, true))
        }
        clearCanvas()
    }

    fun removeCurrentFrame() {
        if (previousFrames.isNotEmpty()) {
            bitmap = previousFrames.removeAt(previousFrames.lastIndex).copy(Bitmap.Config.ARGB_8888, true)
            canvas = Canvas(bitmap!!)
            invalidate()
        }
    }

    fun startAnimation() {
        if (previousFrames.isNotEmpty() && !isPlaying) {
            isPlaying = true
            currentFrameIndex = 0
            handler.post(animationRunnable)
        }
    }

    fun stopAnimation() {
        isPlaying = false
        handler.removeCallbacks(animationRunnable)
        if (previousFrames.isNotEmpty()) {
            bitmap = previousFrames.last().copy(Bitmap.Config.ARGB_8888, true)
            canvas = Canvas(bitmap!!)
            invalidate()
        }
    }
}
