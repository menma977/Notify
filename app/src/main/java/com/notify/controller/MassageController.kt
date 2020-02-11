package com.notify.controller

import android.os.AsyncTask
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

class MassageController {
  class Get : AsyncTask<Void, Void, JSONObject>() {
    override fun doInBackground(vararg params: Void?): JSONObject {
      try {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
          .url("http://malahabel.com/api/notify")
          .method("GET", null)
          .addHeader("X-Requested-With", "XMLHttpRequest")
          .build()
        val response: Response = client.newCall(request).execute()
        val input =
          BufferedReader(InputStreamReader(response.body().byteStream()))

        val inputData: String = input.readLine()
        val convertJSON = JSONObject(inputData)
        input.close()
        return if (response.isSuccessful) {
          JSONObject().put("code", response.code()).put("response", convertJSON)
        } else {
          JSONObject("{code: ${response.code()}, data: 'Sesi anda Sudah Habis'}")
        }
      } catch (e: Exception) {
        return JSONObject("{code: 500, data: 'Terjadi kesalahan saat mencoba terhubung ke server'}")
      }
    }
  }
}