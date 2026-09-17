package com.proverbs.widget

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#1B1B24"))
            setPadding(64, 64, 64, 64)
        }

        val title = TextView(this).apply {
            text = "Proverbs Widget"
            setTextColor(Color.WHITE)
            textSize = 22f
        }

        val body = TextView(this).apply {
            text = "Long-press your home screen, choose Widgets, find Proverbs, " +
                "and drag it to your home screen. It shows a random verse and " +
                "refreshes automatically once a day — tap it any time for a new one."
            setTextColor(Color.parseColor("#C9C9D6"))
            textSize = 15f
            setPadding(0, 32, 0, 0)
        }

        root.addView(title)
        root.addView(body)
        setContentView(root)
    }
}
