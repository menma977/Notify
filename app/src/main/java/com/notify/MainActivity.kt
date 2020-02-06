package com.notify

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var contentLinearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contentLinearLayout = findViewById(R.id.content)
        contentLinearLayout.removeAllViews()

        val body = LinearLayout(this)
        body.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        body.orientation = LinearLayout.VERTICAL
        body.setBackgroundResource(R.color.Primary)

        val header = LinearLayout(this)
        header.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        header.orientation = LinearLayout.HORIZONTAL

        val logo = ImageView(this)
        logo.layoutParams = LinearLayout.LayoutParams(
            0,
            50,
            0.2F
        )
        logo.setImageResource(R.mipmap.ic_launcher_foreground)

        val title = TextView(this)
        title.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1F
        )

    }
}
