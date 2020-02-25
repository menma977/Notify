package com.notify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DetailActivity : AppCompatActivity() {

  private lateinit var title: TextView
  private lateinit var date: TextView
  private lateinit var description: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)

    title = findViewById(R.id.titleTextView)
    date = findViewById(R.id.dateTextView)
    description = findViewById(R.id.descriptionTextView)

    title.text = intent.getStringExtra("title")
    date.text = intent.getStringExtra("date")
    description.text = intent.getStringExtra("description")
  }

  override fun onBackPressed() {
    super.onBackPressed()
    finishAndRemoveTask()
  }
}
