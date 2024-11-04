package com.example.drawer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var canvasView: CanvasView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        canvasView = findViewById(R.id.canvas_view)
        val eraserButton = findViewById<ImageButton>(R.id.eraser_button)
        val pencilButton = findViewById<ImageButton>(R.id.pencil_button)
        val brushButton = findViewById<ImageButton>(R.id.brush_button)
        val nextFrameButton = findViewById<ImageButton>(R.id.add_frame_button)
        val removeFrameButton = findViewById<ImageButton>(R.id.delete_button)
        val playButton = findViewById<ImageButton>(R.id.play_button)
        val stopButton = findViewById<ImageButton>(R.id.pause_button)
        val colorPickerButton = findViewById<ImageButton>(R.id.color_picker_button)

        colorPickerButton.setOnClickListener {
            showColorPickerMenu(it)
        }


        eraserButton.setOnClickListener {
            canvasView.setEraserMode()
        }

        pencilButton.setOnClickListener {
            canvasView.setBrushMode()
        }

        nextFrameButton.setOnClickListener {
            canvasView.saveCurrentFrame()
        }

        removeFrameButton.setOnClickListener {
            canvasView.removeCurrentFrame()
        }

        playButton.setOnClickListener {
            hideControls(true)
            canvasView.startAnimation()
        }

        stopButton.setOnClickListener {
            hideControls(false)
            canvasView.stopAnimation()
        }
    }

    private fun showColorPickerMenu(anchor: View) {
        val inflater = LayoutInflater.from(this)
        val colorPickerView = inflater.inflate(R.layout.color_picker_menu, null)

        val popupWindow = PopupWindow(
            colorPickerView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.elevation = 10f

        val rootView = findViewById<View>(android.R.id.content) as FrameLayout
        val blurBackground = View(this).apply {
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.black_overlay))
            alpha = 0.6f
        }

        rootView.addView(
            blurBackground,
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        blurBackground.bringToFront()

        colorPickerView.findViewById<ImageView>(R.id.color_white).setOnClickListener {
            canvasView.setBrushColor(Color.parseColor("#FFFFFF"))
            popupWindow.dismiss()
        }

        colorPickerView.findViewById<ImageView>(R.id.color_red).setOnClickListener {
            canvasView.setBrushColor(Color.parseColor("#FF3D00"))
            popupWindow.dismiss()
        }

        colorPickerView.findViewById<ImageView>(R.id.color_black).setOnClickListener {
            canvasView.setBrushColor(Color.parseColor("#000000"))
            popupWindow.dismiss()
        }

        colorPickerView.findViewById<ImageView>(R.id.color_blue).setOnClickListener {
            canvasView.setBrushColor(Color.parseColor("#1976D2"))
            popupWindow.dismiss()
        }

        popupWindow.setOnDismissListener {
            rootView.removeView(blurBackground)
        }

        popupWindow.showAtLocation(
            anchor,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            anchor.height + 150
        )
    }

    private fun hideControls(isHidden: Boolean) {
        val visibility = if (isHidden) View.GONE else View.VISIBLE
        findViewById<ImageButton>(R.id.eraser_button).visibility = visibility
        findViewById<ImageButton>(R.id.pencil_button).visibility = visibility
        findViewById<ImageButton>(R.id.brush_button).visibility = visibility
        findViewById<ImageButton>(R.id.add_frame_button).visibility = visibility
        findViewById<ImageButton>(R.id.delete_button).visibility = visibility
        findViewById<ImageButton>(R.id.color_picker_button).visibility = visibility
        findViewById<ImageButton>(R.id.selection_button).visibility = visibility
        findViewById<ImageButton>(R.id.undo_button).visibility = visibility
        findViewById<ImageButton>(R.id.redo_button).visibility = visibility
        findViewById<ImageButton>(R.id.frames_button).visibility = visibility
        findViewById<ImageButton>(R.id.play_button).visibility =
            if (isHidden) View.GONE else View.VISIBLE
        findViewById<ImageButton>(R.id.pause_button).visibility =
            if (isHidden) View.VISIBLE else View.GONE
    }
}
