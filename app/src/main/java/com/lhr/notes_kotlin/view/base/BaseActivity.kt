package com.lhr.notes_kotlin.view.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lhr.notes_kotlin.view.activity.EditActivity
import timber.log.Timber

open class BaseActivity : AppCompatActivity() {


    override fun attachBaseContext(newBase: Context) {
        //覆寫系統設定fontScale 1.3 to 1.0
        val config = newBase.resources.configuration
        config.fontScale = 1f
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

//    fun showConfirmDialog(msg: String, title: String? = null) {
//        MessageDialogFragment.newInstance(msg, title).show(supportFragmentManager, "")
//    }

    private var logLifeCycle = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (logLifeCycle) Timber.tag("ActivityLife").d("onCreate $this")

//        imageBack = findViewById<ImageView>(R.id.imageBack)

    }

    override fun onStart() {
        super.onStart()
        if (logLifeCycle) Timber.tag("ActivityLife").d("onStart $this")
    }

    override fun onResume() {
        super.onResume()
        if (logLifeCycle) Timber.tag("ActivityLife").d("onResume $this")
    }

    override fun onPause() {
        super.onPause()
        if (logLifeCycle) Timber.tag("ActivityLife").d("onPause $this")
    }

    override fun onStop() {
        super.onStop()
        if (logLifeCycle) Timber.tag("ActivityLife").d("onStop $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (logLifeCycle) Timber.tag("ActivityLife").d("onDestroy $this")
    }

}