package com.notify

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.notify.controller.MassageController
import org.json.JSONArray

class IndexActivity : AppCompatActivity() {

  private lateinit var jsonData: JSONArray
  private lateinit var reload: FloatingActionButton
  private lateinit var mainContent: LinearLayout

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_index)

    reload = findViewById(R.id.reloadData)
    mainContent = findViewById(R.id.mainContent)
    mainContent.removeAllViews()

    val data = intent.getSerializableExtra("data").toString()
    jsonData = if (data.isNotEmpty()) {
      JSONArray(data)
    } else {
      try {
        val jsonResponse = MassageController.Get().execute().get().getJSONObject("response").getJSONArray("list")
        jsonResponse
      } catch (error: Exception) {
        Toast.makeText(this, "Bad Connection pleas reload again", Toast.LENGTH_SHORT).show()
        JSONArray().put(null)
      }
    }

    if (jsonData.length() > 0) {
      for (i in 0 until jsonData.length()) {
        val content = generateModel(
          jsonData.getJSONObject(i)["description"].toString(),
          jsonData.getJSONObject(i)["full_description"].toString(),
          jsonData.getJSONObject(i)["status"].toString().toInt()
        )
        mainContent.addView(content)
      }
    }

    reload.setOnClickListener {
      val goTo = Intent(this, MainActivity::class.java)
      finishAndRemoveTask()
      startActivity(goTo)
    }
  }

  private fun generateModel(titleScript: String, descriptionScript: String, status: Int): View {
    val structure = LinearLayout(applicationContext)
    val structureStyle = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT
    )
    structureStyle.setMargins(5, 5, 5, 5)
    structure.layoutParams = structureStyle
    structure.orientation = LinearLayout.VERTICAL
    structure.elevation = 10F
    when (status) {
      0 -> {
        structure.setBackgroundResource(R.color.Danger)
      }
      1 -> {
        structure.setBackgroundResource(R.color.Warning)
      }
      else -> {
        structure.setBackgroundResource(R.color.Info)
      }
    }

    val head = LinearLayout(applicationContext)
    val headStyle = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT
    )
    head.layoutParams = headStyle
    head.orientation = LinearLayout.VERTICAL
    head.gravity = Gravity.CENTER
    head.orientation = LinearLayout.HORIZONTAL

    val logo = ImageView(applicationContext)
    val logoStyle = LinearLayout.LayoutParams(
      80,
      150,
      0.2F
    )
    logo.layoutParams = logoStyle
    logo.scaleType = ImageView.ScaleType.FIT_XY
    logo.setImageResource(R.mipmap.ic_launcher_foreground)
    head.addView(logo)

    val title = TextView(applicationContext)
    val titleStyle = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.WRAP_CONTENT,
      1F
    )
    titleStyle.marginEnd = 20
    title.layoutParams = titleStyle
    title.textSize = 18F
    title.gravity = Gravity.END
    title.setTypeface(title.typeface, Typeface.BOLD)
    title.setTextColor(Color.WHITE)
    title.text = titleScript
    head.addView(title)

    val body = TextView(applicationContext)
    val bodyStyle = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.WRAP_CONTENT,
      1F
    )
    bodyStyle.setMargins(20, 20, 20, 20)
    body.layoutParams = bodyStyle
    body.textSize = 14F
    body.gravity = Gravity.START
    body.setTextColor(Color.WHITE)
    body.text = descriptionScript

    structure.addView(head)
    structure.addView(body)

    return structure
  }
}
