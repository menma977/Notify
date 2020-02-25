package com.notify

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.notify.controller.MassageController
import org.json.JSONArray


class IndexActivity : AppCompatActivity() {

  private lateinit var jsonData: JSONArray
  private lateinit var reload: FloatingActionButton
  private lateinit var mainContent: LinearLayout
  private lateinit var configSound: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_index)

    reload = findViewById(R.id.reloadData)
    mainContent = findViewById(R.id.mainContent)
    configSound = findViewById(R.id.configSound)
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
          jsonData.getJSONObject(i)["updated_at"].toString(),
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

    configSound.setOnClickListener {
      val intent = Intent()
      when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
          intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
          intent.putExtra(Settings.EXTRA_APP_PACKAGE, this.packageName)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
          intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
          intent.putExtra("app_package", this.packageName)
          intent.putExtra("app_uid", this.applicationInfo.uid)
        }
        else -> {
          intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
          intent.addCategory(Intent.CATEGORY_DEFAULT)
          intent.data = Uri.parse("package:" + this.packageName)
        }
      }
      this.startActivity(intent)
    }
  }

  private fun generateModel(dateScript: String, titleScript: String, descriptionScript: String, status: Int): View {
    val structure = LinearLayout(applicationContext)
    val structureStyle = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT
    )
    structureStyle.setMargins(5, 5, 5, 5)
    structure.layoutParams = structureStyle
    structure.orientation = LinearLayout.VERTICAL
    structure.elevation = 20F
    when (status) {
      0 -> {
        structure.setBackgroundResource(R.drawable.massage_danger)
      }
      1 -> {
        structure.setBackgroundResource(R.drawable.massage_warning)
      }
      else -> {
        structure.setBackgroundResource(R.drawable.massage_info)
      }
    }
    structure.setOnClickListener {
      val goTo = Intent(applicationContext, DetailActivity::class.java)
      goTo.putExtra("title", titleScript)
      goTo.putExtra("description", descriptionScript)
      goTo.putExtra("date", dateScript)
      startActivity(goTo)
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
      60,
      60,
      0.2F
    )
    logoStyle.setMargins(10, 10, 10, 10)
    logo.layoutParams = logoStyle
    logo.scaleType = ImageView.ScaleType.FIT_XY
    logo.setImageResource(R.mipmap.ic_launcher_round)
    head.addView(logo)

    val title = TextView(applicationContext)
    val titleStyle = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.WRAP_CONTENT,
      1F
    )
    titleStyle.marginEnd = 5
    title.layoutParams = titleStyle
    title.textSize = 18F
    title.gravity = Gravity.END
    title.setTypeface(title.typeface, Typeface.BOLD)
    title.setTextColor(Color.BLACK)
    title.text = titleScript
    head.addView(title)

    val date = TextView(applicationContext)
    val dateStyle = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.WRAP_CONTENT,
      1F
    )
    dateStyle.marginEnd = 20
    date.layoutParams = dateStyle
    date.textSize = 12F
    date.gravity = Gravity.END
    date.setTextColor(Color.BLACK)
    date.text = dateScript
    head.addView(date)

    val body = TextView(applicationContext)
    val bodyStyle = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.WRAP_CONTENT,
      1F
    )
    bodyStyle.setMargins(150, 50, 20, 50)
    body.layoutParams = bodyStyle
    body.textSize = 14F
    body.gravity = Gravity.END
    body.setTextColor(Color.BLACK)
    body.text = descriptionScript

    structure.addView(head)
    structure.addView(body)

    return structure
  }
}
