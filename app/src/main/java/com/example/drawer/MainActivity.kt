package com.example.drawer

import android.os.Bundle
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
        val deleteButton = findViewById<ImageButton>(R.id.delete_button)
        val eraserButton = findViewById<ImageButton>(R.id.eraser_button)
        val pencilButton = findViewById<ImageButton>(R.id.pencil_button)
        val brushButton = findViewById<ImageButton>(R.id.brush_button)

        deleteButton.setOnClickListener {
            canvasView.clearCanvas()
        }

        eraserButton.setOnClickListener {
            canvasView.setEraserMode()
        }

        pencilButton.setOnClickListener {
            canvasView.setBrushMode()
        }
    }
}
