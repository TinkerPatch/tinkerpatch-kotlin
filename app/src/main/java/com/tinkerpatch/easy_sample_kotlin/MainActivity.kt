package com.tinkerpatch.easy_sample_kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.tencent.tinker.lib.util.TinkerLog
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals
import com.tinkerpatch.sdk.TinkerPatch
import com.tinkerpatch.sdk.server.callback.ConfigRequestCallback
import java.util.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    Log.e(TAG, "I am on onCreate classloader1:" + MainActivity::class.java!!.getClassLoader().toString())
    //test resource change
    Log.e(TAG, "I am on onCreate string:" + resources.getString(R.string.test_resource))
    //        Log.e(TAG, "I am on patch onCreate");

    val requestPatchButton = findViewById(R.id.requestPatch) as Button

    //immediately 为 true, 每次强制访问服务器更新
    requestPatchButton.setOnClickListener { TinkerPatch.with().fetchPatchUpdate(true) }

    val requestConfigButton = findViewById(R.id.requestConfig) as Button

    //immediately 为 true, 每次强制访问服务器更新
    requestConfigButton.setOnClickListener {
      TinkerPatch.with().fetchDynamicConfig(object : ConfigRequestCallback {

        override fun onSuccess(configs: HashMap<String, String>) {
          TinkerLog.w(TAG, "request config success, config:" + configs)
        }

        override fun onFail(e: Exception) {
          TinkerLog.w(TAG, "request config failed, exception:" + e)
        }
      }, true)
    }

    val cleanPatchButton = findViewById(R.id.cleanPatch) as Button

    cleanPatchButton.setOnClickListener { TinkerPatch.with().cleanAll() }

    val killSelfButton = findViewById(R.id.killSelf) as Button

    killSelfButton.setOnClickListener {
      ShareTinkerInternals.killAllOtherProcess(applicationContext)
      android.os.Process.killProcess(android.os.Process.myPid())
    }
  }

  override fun onResume() {
    Log.e(TAG, "I am on onResume")
    super.onResume()

  }

  override fun onPause() {
    Log.e(TAG, "I am on onPause")
    super.onPause()
  }

  companion object {
    private val TAG = "Tinker.MainActivity"
  }
}
