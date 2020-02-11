package com.notify

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.notify.controller.MassageController
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    FirebaseMessaging.getInstance().subscribeToTopic("all")

    Timer().schedule(1000) {
      try {
        val jsonResponse = MassageController.Get().execute().get().getJSONObject("response").getJSONArray("list")
        val goTo = Intent(applicationContext, IndexActivity::class.java).putExtra("data", jsonResponse.toString())
        finishAndRemoveTask()
        startActivity(goTo)
      } catch (e: Exception) {
        val goTo = Intent(applicationContext, IndexActivity::class.java).putExtra("data", "")
        finishAndRemoveTask()
        startActivity(goTo)
      }
    }
  }
}
