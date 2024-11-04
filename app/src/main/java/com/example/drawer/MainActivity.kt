package com.example.drawer

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val canvasView = findViewById<CanvasView>(R.id.canvas_view)
        val eraserButton = findViewById<ImageButton>(R.id.eraser_button)
        val pencilButton = findViewById<ImageButton>(R.id.pencil_button)
        val brushButton = findViewById<ImageButton>(R.id.brush_button)
        val nextFrameButton = findViewById<ImageButton>(R.id.add_frame_button)
        val removeFrameButton = findViewById<ImageButton>(R.id.delete_button)
        val playButton = findViewById<ImageButton>(R.id.play_button)
        val stopButton = findViewById<ImageButton>(R.id.pause_button)


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
        findViewById<ImageButton>(R.id.play_button).visibility = if (isHidden) View.GONE else View.VISIBLE
        findViewById<ImageButton>(R.id.pause_button).visibility = if (isHidden) View.VISIBLE else View.GONE
    }
}
